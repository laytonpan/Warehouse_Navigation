package com.example.navi_warehouse.Order;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.navi_warehouse.Item.Item;

import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "order_id")
    public String orderId;

    @ColumnInfo(name = "items")
    public List<Item> items;

    public Order(String orderId, List<Item> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Integer> getShelfIds() {
        return items.stream()
                .map(Item::getShelfId) // Extract shelfId from each item
                .distinct()
                .collect(Collectors.toList());
    }
}