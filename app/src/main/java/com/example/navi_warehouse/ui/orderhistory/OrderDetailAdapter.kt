package com.example.navi_warehouse.ui.orderdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navi_warehouse.Order.ItemStatus
import com.example.navi_warehouse.Order.PickStatus
import com.example.navi_warehouse.databinding.FragmentOrderDetailListBinding

class OrderDetailAdapter : RecyclerView.Adapter<OrderDetailAdapter.ItemViewHolder>() {

    private val items = mutableListOf<ItemStatus>()

    fun submitList(newItems: List<ItemStatus>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: FragmentOrderDetailListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemStatus: ItemStatus) {
            binding.itemNameText.text = itemStatus.item.name
            binding.itemShelfText.text = "Shelf: ${itemStatus.item.shelfId}"

            val statusText = when (itemStatus.status) {
                PickStatus.PICKED -> "✔ Picked"
                PickStatus.MISSED -> "❌ Missed"
                PickStatus.DAMAGED -> "⚠ Damaged"
            }

            binding.itemStatusText.text = statusText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = FragmentOrderDetailListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
