package com.example.navi_warehouse.Item;

import android.content.Context;
import android.util.Log;
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
import com.example.navi_warehouse.ui.order.OrderFragment;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private final Context context;
    private boolean readOnly = false;

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

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();

        Log.d("ItemAdapter", "setItems called, item count = " + items.size());

    }

    // Set full quantities from outside (used by OrderFragment to initialize with existing selection)
    public void setItemQuantities(Map<Item, Integer> quantities) {
        this.itemQuantities = new HashMap<>(quantities);
        this.items = new ArrayList<>(quantities.keySet());
        notifyDataSetChanged();
    }

    public Map<Item, Integer> getItemQuantities() {
        return itemQuantities;
    }

    public int getTotalSelectedCount() {
        return itemQuantities.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        int quantity = itemQuantities.getOrDefault(item, 0);

        holder.nameTextView.setText(item.getName());
        holder.quantityTextView.setText(String.valueOf(quantity));
        holder.decreaseButton.setEnabled(quantity > 0);

        if (readOnly) {
            holder.increaseButton.setEnabled(false);
            holder.decreaseButton.setEnabled(false);
        }

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
                    itemQuantities.remove(item);
                    items.remove(item);
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

    // Sync selectedItems list from quantity map
    private void updateOrderFragmentSelectedItems() {
        List<Item> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : itemQuantities.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }
        CurrentOrderManager.getInstance().setItems(result);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView quantityTextView;
        public Button increaseButton;
        public Button decreaseButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionChangedListener = listener;

    }


    private boolean hasActiveOrder() {
        Order order = CurrentOrderManager.getInstance().getCurrentOrder();
        boolean valid = order != null && order.getOrderId() != null && !order.getOrderId().isEmpty();
        if (!valid) {
            Toast.makeText(context, "Please create or select an order first!", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }


    public void loadItemsFromOrder(Order order) {
        Map<Item, Integer> quantityMap = new HashMap<>();

        for (Item item : order.getItems()) {
            quantityMap.put(item, quantityMap.getOrDefault(item, 0) + 1);
        }

        this.itemQuantities = quantityMap;
        this.items = new ArrayList<>(quantityMap.keySet());

        notifyDataSetChanged();

        Log.d("ItemAdapter", "loadItemsFromOrder - quantity map: " + quantityMap);

    }

    private void updateGlobalOrderItems() {
        List<Item> result = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : itemQuantities.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }
        CurrentOrderManager.getInstance().getCurrentOrder().items = result;
    }

    public void setItemsWithQuantities(List<Item> allItems, List<Item> orderItems) {
        this.items = new ArrayList<>(allItems);

        Map<Item, Integer> quantityMap = new HashMap<>();
        for (Item item : orderItems) {
            quantityMap.put(item, quantityMap.getOrDefault(item, 0) + 1);
        }

        this.itemQuantities = quantityMap;
        notifyDataSetChanged();

        // Debug
        Log.d("ItemAdapter", "setItemsWithQuantities: total items = " + items.size() +
                ", quantities = " + quantityMap.size());
    }


}
