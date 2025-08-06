package com.example.navi_warehouse.ui.order

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navi_warehouse.Item.Item
import com.example.navi_warehouse.Item.ItemAdapter
import com.example.navi_warehouse.Map.WarehouseMapModel
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample
import com.example.navi_warehouse.Navigation.DijkstraNavigator
import com.example.navi_warehouse.Order.CurrentOrderManager
import com.example.navi_warehouse.R
import com.example.navi_warehouse.databinding.FragmentOrderBinding
import com.example.navi_warehouse.ui.map.MapFragment

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ItemAdapter

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
        adapter.setReadOnly(true)

        val currentOrder = CurrentOrderManager.getInstance().currentOrder
        val itemQuantities = getItemQuantityMap(currentOrder.items)

        adapter.setItemQuantities(itemQuantities)
        binding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderRecyclerView.adapter = adapter

        val totalCount = itemQuantities.values.sum()
        binding.orderItemCountText.text = "Total: $totalCount item(s)"

        binding.orderIdTextView.text = "Current Order: ${currentOrder.orderId}"

        binding.generatePathButton.setOnClickListener {
            val items = currentOrder.items
            if (items.isEmpty()) {
                Toast.makeText(requireContext(), "No items selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mapModel = WarehouseMapSimpleExample.createSimpleMap(30)
            val nodeIdSequence = listOf("Entrance") + items.map { "Shelf${it.shelfId}" } + listOf("Exit")
            val fullPath = mutableListOf<WarehouseMapModel.Node>()

            Log.d("OrderFragment", "=== Generating Full Path ===")
            items.forEachIndexed { index, item ->
                Log.d("OrderFragment", "Item[$index]: ${item.name} (Shelf${item.shelfId})")
            }
            Log.d("OrderFragment", "NodeIdSequence: $nodeIdSequence")

            for (i in 0 until nodeIdSequence.size - 1) {
                val from = mapModel.getNode(nodeIdSequence[i])
                val to = mapModel.getNode(nodeIdSequence[i + 1])
                if (from != null && to != null) {
                    val segment = DijkstraNavigator.calculateShortestPath(from, to)
                    if (fullPath.isNotEmpty() && segment.isNotEmpty()) {
                        segment.removeFirst()
                    }
                    fullPath.addAll(segment)
                }
            }

            MapFragment.latestPathIds = fullPath.map { it.id }
            MapFragment.lastOriginTabId = R.id.navigation_order

            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.mapFragment

            Log.d("OrderFragment", "=== Final Full Path ===")
            fullPath.forEachIndexed { index, node ->
                Log.d("OrderFragment", "[$index] ${node.id}")
            }
        }

        binding.orderActionMenuButton.setOnClickListener { view ->
            val popup = PopupMenu(requireContext(), view)
            popup.menuInflater.inflate(R.menu.order_action_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_clear_order -> {
                        CurrentOrderManager.getInstance().setItems(emptyList())
                        adapter.setItemQuantities(emptyMap())
                        binding.orderItemCountText.text = "Total: 0 item(s)"
                        MapFragment.latestPathIds = null
                        MapFragment.lastOriginTabId = R.id.navigation_order
                        Toast.makeText(requireContext(), "Order cleared!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_new_order -> {
                        CurrentOrderManager.getInstance().resetOrder()
                        binding.orderIdTextView.text = "Current Order: ${CurrentOrderManager.getInstance().currentOrder.orderId}"
                        adapter.setItemQuantities(emptyMap())
                        binding.orderItemCountText.text = "Total: 0 item(s)"
                        MapFragment.latestPathIds = null
                        MapFragment.lastOriginTabId = R.id.navigation_order
                        Toast.makeText(requireContext(), "New order created!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentOrder = CurrentOrderManager.getInstance().currentOrder
        if (currentOrder.items.isEmpty() && currentOrder.orderId.isBlank()) {
            CurrentOrderManager.getInstance().resetOrder()
        }
        updateOrderIdText()

        Log.d("OrderFragment", "CurrentOrder Items: " +
                CurrentOrderManager.getInstance().currentOrder.items.joinToString { it.name })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getItemQuantityMap(items: List<Item>): Map<Item, Int> {
        val map = mutableMapOf<Item, Int>()
        for (item in items) {
            map[item] = map.getOrDefault(item, 0) + 1
        }
        return map
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_order_history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_order_history -> {
                findNavController().navigate(R.id.navigation_order_history)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateOrderIdText() {
        val orderId = CurrentOrderManager.getInstance().currentOrder.orderId
        binding.orderIdTextView.text = "Current Order: $orderId"
    }
}
