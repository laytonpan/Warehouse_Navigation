package com.example.navi_warehouse.ui.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navi_warehouse.OrderHistory.CompletedOrder
import com.example.navi_warehouse.databinding.ItemOrderHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    private val orders = mutableListOf<CompletedOrder>()

    fun submitList(newOrders: List<CompletedOrder>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: CompletedOrder) {
            // Format timestamp
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(order.timestamp))

            binding.orderIdText.text = "Order ID: ${order.id}"
            binding.orderTimestampText.text = "Time: $formattedDate"
            binding.itemCountText.text = "Items: ${order.items.size}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }
}
