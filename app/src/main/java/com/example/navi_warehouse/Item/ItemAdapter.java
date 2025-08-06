package com.example.navi_warehouse.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navi_warehouse.Order.CurrentOrderManager;
import com.example.navi_warehouse.Order.Order;
import com.example.navi_warehouse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter for displaying items and their selected quantities.
 * Supports conditional removal on zero based on removeOnZero flag.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private final Context context;
    private boolean readOnly = false;
    private boolean removeOnZero = false;  // controls removal behavior

    // Track quantity for each item
    private Map<Item, Integer> itemQuantities = new HashMap<>();

    // Selection changed listener for updating UI elsewhere
    private OnSelectionChangedListener selectionChangedListener;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * When true, decreasing quantity to zero removes the item from the list.
     * Otherwise, quantity is set to zero and item remains displayed.
     */
    public void setRemoveOnZero(boolean removeOnZero) {
        this.removeOnZero = removeOnZero;
    }

    /**
     * Set the full items list without changing quantities.
     */
    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Initialize adapter with all items and selected quantities map.
     */
    public void setItemsWithQuantities(List<Item> allItems, List<Item> selectedItems) {
        this.items = new ArrayList<>(allItems);
        Map<Item, Integer> quantityMap = new HashMap<>();
        for (Item item : selectedItems) {
            quantityMap.put(item, quantityMap.getOrDefault(item, 0) + 1);
        }
        this.itemQuantities = quantityMap;
        notifyDataSetChanged();
    }

    /**
     * Directly set only the quantities map and update item list accordingly.
     */
    public void setItemQuantities(Map<Item, Integer> quantities) {
        this.itemQuantities = new HashMap<>(quantities);
        this.items = new ArrayList<>(quantities.keySet());
        notifyDataSetChanged();
    }

    public Map<Item, Integer> getItemQuantities() {
        return itemQuantities;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        int quantity = itemQuantities.getOrDefault(item, 0);

        holder.nameTextView.setText(item.getName());
        holder.quantityTextView.setText(String.valueOf(quantity));
        holder.decreaseButton.setEnabled(quantity > 0 && !readOnly);
        holder.increaseButton.setEnabled(!readOnly);

        holder.increaseButton.setOnClickListener(v -> {
            if (readOnly || !hasActiveOrder()) return;
            int currentQty = itemQuantities.getOrDefault(item, 0);
            itemQuantities.put(item, currentQty + 1);
            notifyItemChanged(position);
            updateGlobalOrderItems();
            if (selectionChangedListener != null) {
                selectionChangedListener.onSelectionChanged(getTotalSelectedCount());
            }
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (readOnly || !hasActiveOrder()) return;
            int currentQty = itemQuantities.getOrDefault(item, 0);
            if (currentQty > 0) {
                int newQty = currentQty - 1;
                if (newQty == 0) {
                    if (removeOnZero) {
                        itemQuantities.remove(item);
                        items.remove(item);
                    } else {
                        itemQuantities.put(item, 0);
                    }
                } else {
                    itemQuantities.put(item, newQty);
                }
                notifyDataSetChanged();
                updateGlobalOrderItems();
                if (selectionChangedListener != null) {
                    selectionChangedListener.onSelectionChanged(getTotalSelectedCount());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    private int getTotalSelectedCount() {
        return itemQuantities.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Checks if a current order exists. If not, shows a toast prompting to create/select one.
     */
    private boolean hasActiveOrder() {
        Order order = CurrentOrderManager.getInstance().getCurrentOrder();
        boolean valid = order != null && order.getOrderId() != null && !order.getOrderId().isEmpty();
        if (!valid) {
            Toast.makeText(context, "Please create or select an order first!", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    // Sync selectedItems list to global order
    private void updateGlobalOrderItems() {
        List<Item> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : itemQuantities.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }
        CurrentOrderManager.getInstance().setItems(result);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView quantityTextView;
        Button increaseButton;
        Button decreaseButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}
