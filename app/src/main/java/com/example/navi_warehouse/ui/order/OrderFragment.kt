package com.example.navi_warehouse.ui.order

import android.os.Bundle
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
            val shelfNodes = selectedItems.mapNotNull { item -> mapModel.getNode(item.shelfId.toString()) }
            val path = DijkstraNavigator.calculateShortestPathMultiDestination(
                mapModel.getNode("Entrance")!!,
                shelfNodes
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
