package com.example.navi_warehouse.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.navi_warehouse.R;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private Context context;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dashboard, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            priceTextView = itemView.findViewById(R.id.itemPriceTextView);
        }
    }
}



