package com.example.navi_warehouse.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.navi_warehouse.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.example.navi_warehouse.ui.order.OrderFragment;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private final Set<Item> selectedItems = new HashSet<>();
    private final Context context;
    private boolean readOnly = false;

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<Item> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.valueOf(item.getPrice()));
        holder.addToOrderButton.setText(selectedItems.contains(item) ? "✔ ADDED" : "➕ ADD");

        // Only show or enable the button if not read-only
        holder.addToOrderButton.setVisibility(readOnly ? View.GONE : View.VISIBLE);

        holder.addToOrderButton.setOnClickListener(v -> {
            if (readOnly) return; // Disable interaction if read-only

            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
                OrderFragment.selectedItems.remove(item);
                Toast.makeText(context, "Removed：" + item.getName(), Toast.LENGTH_SHORT).show();
            } else {
                selectedItems.add(item);
                OrderFragment.selectedItems.add(item);
                Toast.makeText(context, "Added：" + item.getName(), Toast.LENGTH_SHORT).show();
            }
            notifyItemChanged(position);

            if (selectionChangedListener != null) {
                selectionChangedListener.onSelectionChanged(selectedItems.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public Button addToOrderButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            priceTextView = itemView.findViewById(R.id.itemPriceTextView);
            addToOrderButton = itemView.findViewById(R.id.addToOrderButton);
        }
    }


    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    private OnSelectionChangedListener selectionChangedListener;

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }
}
