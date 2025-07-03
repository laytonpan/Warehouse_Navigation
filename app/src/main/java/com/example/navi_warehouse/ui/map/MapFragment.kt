package com.example.navi_warehouse.ui.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.navi_warehouse.Map.CustomMapView
import com.example.navi_warehouse.Map.WarehouseMapModel
import com.example.navi_warehouse.R
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample
import com.example.navi_warehouse.databinding.FragmentMapBinding
import com.example.navi_warehouse.ui.order.OrderFragment

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var warehouseMapModel: WarehouseMapModel? = null

    companion object {
        // Stores the list of path node IDs for highlighting
        var latestPathIds: List<String>? = null

        // Stores the originating tab for potential navigation handling
        var lastOriginTabId: Int = R.id.navigation_dashboard

        // Index of the current target in the path list
        var currentTargetIndex: Int = 1 // Start from the first shelf (skip Entrance)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Reset index every time fragment is created
        currentTargetIndex = 1

        // Initialize map and bind
        warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(30)
        val mapView: CustomMapView = binding.customMapView
        mapView.setWarehouseMapModel(warehouseMapModel)

        // Highlight if path exists
        val pathNodeIds = latestPathIds
        if (!pathNodeIds.isNullOrEmpty()) {
            val pathNodes = pathNodeIds.mapNotNull { id -> warehouseMapModel?.getNode(id) }
            mapView.setHighlightedPath(pathNodes)
            binding.mapControlContainer.visibility = View.VISIBLE
        } else {
            // If no path, hide the control area
            binding.mapControlContainer.visibility = View.GONE
        }

        // Show first target
        updateCurrentTarget()

        binding.pickFinishedButton.setOnClickListener {
            currentTargetIndex += 1 // Continue to the next item
            updateCurrentTarget()
        }

        binding.damagedOrMissedButton.setOnClickListener {
            currentTargetIndex += 1 // Skip to the next item
            updateCurrentTarget()
        }
    }


    // Update the label and highlighted path based on the current target index
    private fun updateCurrentTarget() {
        val pathIds = latestPathIds ?: return
        val index = currentTargetIndex

        if (index in pathIds.indices) {
            val currentId = pathIds[index]

            // Try to find the corresponding item from the order list
            val item = OrderFragment.selectedItems
                .firstOrNull { it.shelfId.toString() == currentId.removePrefix("Shelf") }

            // Format display string
            val label = if (item != null) {
                "Current Target: ${item.name} ($currentId)"
            } else {
                "Current Target: $currentId"
            }

            // Update UI label
            binding.targetInfoText.text = label

            // Show sub-path from previous node to remaining destinations
            val subPath = pathIds.subList(index - 1, pathIds.size)
            val nodes = subPath.mapNotNull { warehouseMapModel?.getNode(it) }
            binding.customMapView.setHighlightedPath(nodes)

        } else {
            // When all items are picked, display message and hide control panel
            binding.targetInfoText.text = "All items picked!"
            binding.mapControlContainer.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
