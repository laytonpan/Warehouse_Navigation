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
import com.example.navi_warehouse.Order.CurrentOrderManager
import com.example.navi_warehouse.OrderHistory.CompletedOrder
import com.example.navi_warehouse.Order.ItemStatus
import com.example.navi_warehouse.OrderHistory.OrderHistoryManager
import com.example.navi_warehouse.Order.PickStatus
import com.example.navi_warehouse.R
import com.example.navi_warehouse.databinding.FragmentMapBinding

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

        warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(30)
        binding.customMapView.setWarehouseMapModel(warehouseMapModel)

        latestPathIds?.let { ids ->
            val nodes = ids.mapNotNull { warehouseMapModel?.getNode(it) }
            binding.customMapView.setHighlightedPath(nodes)
            binding.mapControlContainer.visibility = View.VISIBLE
        } ?: run {
            binding.mapControlContainer.visibility = View.GONE
        }

        updateCurrentTarget()

        binding.pickFinishedButton.setOnClickListener {
            recordCurrentItem(PickStatus.PICKED)
            moveToNextTarget()
        }

        binding.damagedOrMissedButton.setOnClickListener {
            recordCurrentItem(PickStatus.MISSED)
            moveToNextTarget()
        }

        Log.d("DashboardFragment", "CurrentOrder Items: " +
                CurrentOrderManager.getInstance().currentOrder.items.joinToString { it.name })

    }

    private fun recordCurrentItem(status: PickStatus) {
        val pathId = latestPathIds?.getOrNull(currentTargetIndex) ?: return
        val shelfId = pathId.removePrefix("Shelf").toIntOrNull() ?: return

        val visitIndex = shelfVisitCount.getOrDefault(shelfId, 0)
        val itemsAtShelf = CurrentOrderManager.getInstance().currentOrder.items.filter { it.shelfId == shelfId }

        if (visitIndex >= itemsAtShelf.size) return

        val item = itemsAtShelf[visitIndex]
        pickedItems.add(ItemStatus(item, status))
        shelfVisitCount[shelfId] = visitIndex + 1
    }

    private fun moveToNextTarget() {
        val pathIds = latestPathIds ?: return

        if (currentTargetIndex >= pathIds.size) {
            completeOrder()
            return
        }

        val currentId = pathIds[currentTargetIndex]
        val shelfId = currentId.removePrefix("Shelf").toIntOrNull()

        if (shelfId != null) {
            val visitIndex = shelfVisitCount.getOrDefault(shelfId, 0)
            val itemsAtShelf = CurrentOrderManager.getInstance().currentOrder.items.filter { it.shelfId == shelfId }

            if (visitIndex < itemsAtShelf.size) {
                updateCurrentTarget()
                return
            }
        }

        val isLastTarget = currentTargetIndex == pathIds.lastIndex
        currentTargetIndex++

        if (currentTargetIndex >= pathIds.size) {
            completeOrder()
        } else {
            updateCurrentTarget()
        }
    }

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
            CurrentOrderManager.getInstance().currentOrder.items.filter { item -> item.shelfId == it }
        } ?: emptyList()

        val label = if (itemsAtShelf.isNotEmpty() && visitIndex < itemsAtShelf.size) {
            "Current Target: ${itemsAtShelf[visitIndex].name} ($currentId)"
        } else {
            "Passing through: $currentId"
        }
        binding.targetInfoText.text = label

        val remainingNodes = pathIds.subList(currentTargetIndex, pathIds.size)
            .mapNotNull { warehouseMapModel?.getNode(it) }
        binding.customMapView.setHighlightedPath(remainingNodes)

        binding.mapControlContainer.visibility = View.VISIBLE

        val hasItemToPick = itemsAtShelf.isNotEmpty() && visitIndex < itemsAtShelf.size
        val isLastItem = (currentTargetIndex == pathIds.lastIndex) && (visitIndex + 1 >= itemsAtShelf.size)

        if (hasItemToPick) {
            binding.pickFinishedButton.text = "✔ PICK FINISHED"
            binding.damagedOrMissedButton.text = "✘ DAMAGED / MISSED"
        } else if (isLastItem) {
            binding.pickFinishedButton.text = "✅ ORDER FINISHED"
            binding.damagedOrMissedButton.text = "✅ ORDER FINISHED"
        } else {
            binding.pickFinishedButton.text = "Next"
            binding.damagedOrMissedButton.text = "Next"
        }
    }

    private fun completeOrder() {
        val completedOrder = CompletedOrder(
            System.currentTimeMillis().toString(),
            System.currentTimeMillis(),
            pickedItems.toList()
        )

        OrderHistoryManager.addOrder(completedOrder)

        Log.d("MapFragment", "Picked Items:")
        pickedItems.forEachIndexed { index, itemStatus ->
            Log.d("MapFragment", "[$index] ${itemStatus.item.name} - shelf ${itemStatus.item.shelfId} - ${itemStatus.status}")
        }

        pickedItems.clear()
        latestPathIds = null
        currentTargetIndex = 1
        shelfVisitCount.clear()

        binding.mapControlContainer.visibility = View.GONE
        binding.targetInfoText.text = "All items picked!"
        Toast.makeText(requireContext(), "Order completed!", Toast.LENGTH_SHORT).show()

        CurrentOrderManager.getInstance().resetOrder()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
