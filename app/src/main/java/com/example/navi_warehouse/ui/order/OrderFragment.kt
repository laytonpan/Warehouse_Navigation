package com.example.navi_warehouse.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.example.navi_warehouse.Map.WarehouseMapModel
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

            // Create the warehouse map model
            val mapModel = WarehouseMapSimpleExample.createSimpleMap(30)
            val startNode = mapModel.getNode("Entrance") ?: return@setOnClickListener
            val exitNode = mapModel.getNode("Exit") ?: return@setOnClickListener

            // Construct the shelf node sequence from selected items
            val shelfNodeIds = selectedItems.map { "Shelf${it.shelfId}" }
            val nodeIdSequence = listOf("Entrance") + shelfNodeIds + listOf("Exit")

            val fullPath = mutableListOf<WarehouseMapModel.Node>()

            Log.d("OrderFragment", "=== Generating Full Path ===")

            // Print selected items and their shelfIds
            selectedItems.forEachIndexed { index, item ->
                Log.d("OrderFragment", "Item[$index]: ${item.name} (Shelf${item.shelfId})")
            }

            // Print nodeIdSequence for routing
            Log.d("OrderFragment", "NodeIdSequence (with Entrance and Exit): $nodeIdSequence")

            for (i in 0 until nodeIdSequence.size - 1) {
                val from = mapModel.getNode(nodeIdSequence[i])
                val to = mapModel.getNode(nodeIdSequence[i + 1])
                if (from != null && to != null) {
                    val segment = DijkstraNavigator.calculateShortestPath(from, to)
                    if (fullPath.isNotEmpty() && segment.isNotEmpty()) {
                        segment.removeFirst() // Avoid duplication of nodes
                    }
                    fullPath.addAll(segment)
                }
            }

            val nodeIds = fullPath.map { it.id }

            // Set to MapFragment
            MapFragment.latestPathIds = nodeIds
            MapFragment.lastOriginTabId = R.id.navigation_order

            // Navigate to map
            val navView = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view)
            navView.selectedItemId = R.id.mapFragment

            Log.d("OrderFragment", "=== Final Full Path ===")
            fullPath.forEachIndexed { index, node ->
                Log.d("OrderFragment", "[$index] ${node.id}")
            }
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


    // Order History
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_order_history, menu)
    }

    // Jump to order history page
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_order_history -> {
                findNavController().navigate(R.id.navigation_order_history)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
