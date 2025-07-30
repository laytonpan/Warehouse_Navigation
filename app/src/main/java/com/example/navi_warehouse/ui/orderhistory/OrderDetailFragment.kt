package com.example.navi_warehouse.ui.orderhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navi_warehouse.OrderHistory.CompletedOrder
import com.example.navi_warehouse.OrderHistory.OrderHistoryManager
import com.example.navi_warehouse.databinding.FragmentOrderDetailBinding
import com.example.navi_warehouse.ui.orderdetail.OrderDetailAdapter

class OrderDetailFragment : Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        val orderId = arguments?.getString("orderId")
        val completedOrder: CompletedOrder? = OrderHistoryManager
            .getAllOrders()
            .firstOrNull { it.id == orderId }

        if (completedOrder == null) {
            binding.orderBasicInfoText.text = "Order not found."
            return
        }

        val formattedTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(java.util.Date(completedOrder.timestamp))

        binding.orderBasicInfoText.text = "Order ID: ${completedOrder.id}\nTime: $formattedTime\nItems: ${completedOrder.items.size}"

        adapter = OrderDetailAdapter()
        binding.itemStatusRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.itemStatusRecyclerView.adapter = adapter
        adapter.submitList(completedOrder.items)

    }

    // Return logic
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
