package com.example.navi_warehouse.ui.orderhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navi_warehouse.OrderHistory.OrderHistoryManager
import com.example.navi_warehouse.databinding.FragmentOrderHistoryBinding

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.fitsSystemWindows = true


        // Setup back button (physical back button)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Navigate back to previous screen
                    findNavController().navigateUp()
                }
            }
        )

        // Setup RecyclerView for displaying order history
        adapter = OrderHistoryAdapter()
        binding.orderHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderHistoryRecyclerView.adapter = adapter

        // Load order history data
        adapter.submitList(OrderHistoryManager.getAllOrders())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
