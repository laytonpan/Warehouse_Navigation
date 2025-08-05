package com.example.navi_warehouse.Order;

import com.example.navi_warehouse.Item.Item;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderManager {
    private static CurrentOrderManager instance;
    private Order currentOrder;
    private int orderCount = 1;

    private CurrentOrderManager() {
        currentOrder = new Order(generateOrderId(), new ArrayList<>());
    }

    public static synchronized CurrentOrderManager getInstance() {
        if (instance == null) {
            instance = new CurrentOrderManager();
        }
        return instance;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void resetOrder() {
        orderCount++;
        currentOrder = new Order(generateOrderId(), new ArrayList<>());
    }

    public void addItem(Item item) {
        currentOrder.getItems().add(item);
    }

    public void removeItem(Item item) {
        currentOrder.getItems().remove(item);
    }

    public void setItems(List<Item> items) {
        currentOrder.items = items;
    }

    private String generateOrderId() {
        return "Order " + orderCount;
    }
}
