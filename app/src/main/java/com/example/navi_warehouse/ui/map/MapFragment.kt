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

        // ✅ pick finished button
        binding.pickFinishedButton.setOnClickListener {
            recordCurrentItem(PickStatus.PICKED)
            moveToNextTarget()
        }

        // ✅ missed/damaged button
        binding.damagedOrMissedButton.setOnClickListener {
            recordCurrentItem(PickStatus.MISSED)
            moveToNextTarget()
        }
    }

    // ⏺ 记录当前拣选物品状态
    private fun recordCurrentItem(status: PickStatus) {
        val pathId = latestPathIds?.getOrNull(currentTargetIndex) ?: return
        val shelfId = pathId.removePrefix("Shelf").toIntOrNull() ?: return
        val item = OrderFragment.selectedItems.firstOrNull { it.shelfId == shelfId } ?: return

        pickedItems.add(ItemStatus(item, status))
    }

    // Jump to next target or Finished
    private fun moveToNextTarget() {
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
        val item = OrderFragment.selectedItems.firstOrNull { it.shelfId == shelfId }

        val label = if (item != null) {
            "Current Target: ${item.name} ($currentId)"
        } else {
            "Current Target: $currentId"
        }
        binding.targetInfoText.text = label

        // Highlight the rest routine
        val remainingNodes = pathIds.subList(currentTargetIndex - 1, pathIds.size)
            .mapNotNull { warehouseMapModel?.getNode(it) }
        binding.customMapView.setHighlightedPath(remainingNodes)
    }

    // Finish pick up
    private fun completeOrder() {

        // Add to Order History
        val completedOrder =
            CompletedOrder(
                System.currentTimeMillis().toString(),   // id (String)
                System.currentTimeMillis(),              // timestamp (Long)
                pickedItems.toList()                     // items (List<ItemStatus>)
            )

        OrderHistoryManager.addOrder(completedOrder)

        // Clear relevance data
        pickedItems.clear()
        OrderFragment.selectedItems.clear()
        latestPathIds = null
        currentTargetIndex = 1

        // Hidden bottom control area
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
