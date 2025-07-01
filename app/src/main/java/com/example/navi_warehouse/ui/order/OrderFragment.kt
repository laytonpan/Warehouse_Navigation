package com.example.navi_warehouse.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navi_warehouse.Item.Item
import com.example.navi_warehouse.Item.ItemAdapter
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample
import com.example.navi_warehouse.Navigation.DijkstraNavigator
import com.example.navi_warehouse.ui.map.MapFragment
import com.example.navi_warehouse.Order.Order
import com.example.navi_warehouse.databinding.FragmentOrderBinding
import androidx.navigation.fragment.findNavController
import com.example.navi_warehouse.R


class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ItemAdapter

    companion object {
        @JvmField
        var selectedItems: MutableList<Item> = mutableListOf()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemAdapter(requireContext())
        adapter.setReadOnly(true) // enable read-only mode for Order page
        adapter.setItems(selectedItems)
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRecyclerView.adapter = adapter

        // Disable selection interactions (view-only mode)
        adapter.setOnSelectionChangedListener(null)

        // Display item count
        binding.orderItemCountText.text = "Total: ${selectedItems.size} item(s)"

        // Generate path button
        binding.generatePathButton.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(requireContext(), "No items selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val order = Order("TempOrder", selectedItems)
            val mapModel = WarehouseMapSimpleExample.createSimpleMap(30)

            val start = mapModel.getNode("Entrance")!!
            val end = mapModel.getNode("Exit")!!

            val shelfNodes = selectedItems.mapNotNull { item ->
                val nodeId = "Shelf${item.shelfId}"
                Log.d("OrderFragment", "Mapped shelfId ${item.shelfId} -> nodeId: $nodeId")
                mapModel.getNode(nodeId)
            }

            // Include exit as last target for proper path routing
            val fullTargetList = shelfNodes + end

            val path = DijkstraNavigator.calculateShortestPathMultiDestination(
                start,
                fullTargetList
            )

            val nodeIds = path.map { it.id }

            MapFragment.latestPathIds = nodeIds
            MapFragment.lastOriginTabId = R.id.navigation_order

            val navView = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view)
            navView.selectedItemId = R.id.mapFragment
        }

        // Clear order Button
        binding.clearOrderButton.setOnClickListener {
            selectedItems.clear()
            adapter.setItems(selectedItems)
            binding.orderItemCountText.text = "Total: 0 item(s)"
            Toast.makeText(requireContext(), "Order cleared!", Toast.LENGTH_SHORT).show()

            // Clear path in MapFragment
            MapFragment.latestPathIds = null
            MapFragment.lastOriginTabId = R.id.navigation_order

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
