package com.example.navi_warehouse.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.navi_warehouse.Map.WarehouseMapModel
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample
import com.example.navi_warehouse.OrderHistory.CompletedOrder
import com.example.navi_warehouse.Order.ItemStatus
import com.example.navi_warehouse.OrderHistory.OrderHistoryManager
import com.example.navi_warehouse.Order.PickStatus
import com.example.navi_warehouse.R
import com.example.navi_warehouse.databinding.FragmentMapBinding
import com.example.navi_warehouse.ui.order.OrderFragment

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var warehouseMapModel: WarehouseMapModel? = null

    companion object {
        var latestPathIds: List<String>? = null
        var lastOriginTabId: Int = R.id.navigation_dashboard
        var currentTargetIndex: Int = 1 // skip entrance
        val pickedItems: MutableList<ItemStatus> = mutableListOf()
        val shelfVisitCount: MutableMap<Int, Int> = mutableMapOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // create map model and bind to view
        warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(30)
        binding.customMapView.setWarehouseMapModel(warehouseMapModel)

        // draw path
        latestPathIds?.let { ids ->
            val nodes = ids.mapNotNull { warehouseMapModel?.getNode(it) }
            binding.customMapView.setHighlightedPath(nodes)
            binding.mapControlContainer.visibility = View.VISIBLE
        } ?: run {
            binding.mapControlContainer.visibility = View.GONE
        }

        // init first target
        updateCurrentTarget()

        // pick finished button
        binding.pickFinishedButton.setOnClickListener {
            recordCurrentItem(PickStatus.PICKED)
            moveToNextTarget()
        }

        // missed/damaged button
        binding.damagedOrMissedButton.setOnClickListener {
            recordCurrentItem(PickStatus.MISSED)
            moveToNextTarget()
        }
    }

    private fun recordCurrentItem(status: PickStatus) {
        val pathId = latestPathIds?.getOrNull(currentTargetIndex) ?: return
        val shelfId = pathId.removePrefix("Shelf").toIntOrNull() ?: return

        val visitIndex = shelfVisitCount.getOrDefault(shelfId, 0)
        val itemsAtShelf = OrderFragment.selectedItems.filter { it.shelfId == shelfId }

        if (visitIndex >= itemsAtShelf.size) return

        val item = itemsAtShelf[visitIndex]

        pickedItems.add(ItemStatus(item, status))

        // Update the visit count for this shelf
        shelfVisitCount[shelfId] = visitIndex + 1
    }


    private fun moveToNextTarget() {
        // Get current shelf info
        val pathIds = latestPathIds ?: return
        if (currentTargetIndex >= pathIds.size) {
            completeOrder()
            return
        }

        val currentId = pathIds[currentTargetIndex]
        val shelfId = currentId.removePrefix("Shelf").toIntOrNull()

        if (shelfId != null) {
            val visitIndex = shelfVisitCount.getOrDefault(shelfId, 0)
            val itemsAtShelf = OrderFragment.selectedItems.filter { it.shelfId == shelfId }

            // 🛑 If more items on the same shelf, do NOT advance
            if (visitIndex < itemsAtShelf.size) {
                updateCurrentTarget()
                return
            }
        }

        // ✅ Otherwise, move to the next target node
        currentTargetIndex++
        updateCurrentTarget()
    }


    // Update target
    private fun updateCurrentTarget() {
        val pathIds = latestPathIds ?: return
        if (currentTargetIndex >= pathIds.size) {
            completeOrder()
            return
        }

        val currentId = pathIds[currentTargetIndex]
        val shelfId = currentId.removePrefix("Shelf").toIntOrNull()
        val visitIndex = shelfId?.let { shelfVisitCount.getOrDefault(it, 0) } ?: 0

        val itemsAtShelf = shelfId?.let {
            OrderFragment.selectedItems.filter { item -> item.shelfId == it }
        } ?: emptyList()

        // Build target label: either item name or passing notice
        val label = if (itemsAtShelf.isNotEmpty() && visitIndex < itemsAtShelf.size) {
            "Current Target: ${itemsAtShelf[visitIndex].name} ($currentId)"
        } else {
            "Passing through: $currentId"
        }
        binding.targetInfoText.text = label

        // Highlight the remaining path
        val remainingNodes = pathIds.subList(currentTargetIndex, pathIds.size)
            .mapNotNull { warehouseMapModel?.getNode(it) }
        binding.customMapView.setHighlightedPath(remainingNodes)

        // Always show the control buttons, even if no items to pick
        binding.mapControlContainer.visibility = View.VISIBLE

        val hasItemToPick = itemsAtShelf.isNotEmpty() && visitIndex < itemsAtShelf.size

        // Set button text and state depending on whether picking is needed
        if (hasItemToPick) {
            binding.pickFinishedButton.text = "✔ PICK FINISHED"
            binding.damagedOrMissedButton.text = "✘ DAMAGED / MISSED"
            binding.pickFinishedButton.isEnabled = true
            binding.damagedOrMissedButton.isEnabled = true
        } else {
            binding.pickFinishedButton.text = "Next"
            binding.damagedOrMissedButton.text = "Next"
            binding.pickFinishedButton.isEnabled = true
            binding.damagedOrMissedButton.isEnabled = true
        }
    }



    // Finish pick up
    private fun completeOrder() {
        // Add to Order History
        val completedOrder = CompletedOrder(
            System.currentTimeMillis().toString(),   // id (String)
            System.currentTimeMillis(),              // timestamp (Long)
            pickedItems.toList()                     // items (List<ItemStatus>)
        )

        OrderHistoryManager.addOrder(completedOrder)

        // 🧪 Log pickedItems for debugging
        Log.d("MapFragment", "Picked Items:")
        pickedItems.forEachIndexed { index, itemStatus ->
            Log.d(
                "MapFragment",
                "[$index] ${itemStatus.item.name} - shelf ${itemStatus.item.shelfId} - ${itemStatus.status}"
            )
        }

        // Clear relevance data
        pickedItems.clear()
        OrderFragment.selectedItems.clear()
        latestPathIds = null
        currentTargetIndex = 1
        shelfVisitCount.clear()

        // Hide bottom control area
        binding.mapControlContainer.visibility = View.GONE
        binding.targetInfoText.text = "All items picked!"
        Toast.makeText(requireContext(), "Order completed!", Toast.LENGTH_SHORT).show()

        Log.d("MapFragment", "Order added. Total history count: ${OrderHistoryManager.getAllOrders().size}")
    }


    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}
