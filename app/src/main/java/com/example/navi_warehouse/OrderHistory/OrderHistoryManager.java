package com.example.navi_warehouse.OrderHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the list of completed orders (order history).
 */
public class OrderHistoryManager {

    // Static list to store completed orders
    private static final List<CompletedOrder> orderHistoryList = new ArrayList<>();

    /**
     * Adds a completed order to the history list.
     *
     * @param order the completed order to add
     */
    public static void addOrder(CompletedOrder order) {
        orderHistoryList.add(order);
    }

    /**
     * Returns a read-only copy of all completed orders.
     *
     * @return list of completed orders
     */
    public static List<CompletedOrder> getAllOrders() {
        return Collections.unmodifiableList(orderHistoryList);
    }

    /**
     * Clears all stored history orders.
     */
    public static void clearHistory() {
        orderHistoryList.clear();
    }

    /**
     * Returns the most recent completed order, or null if none exist.
     *
     * @return the latest CompletedOrder or null
     */
    public static CompletedOrder getLatestOrder() {
        if (orderHistoryList.isEmpty()) return null;
        return orderHistoryList.get(orderHistoryList.size() - 1);
    }

    /**
     * Finds a completed order by its ID.
     *
     * @param id order ID
     * @return the matched CompletedOrder or null
     */
    public static CompletedOrder findOrderById(String id) {
        for (CompletedOrder order : orderHistoryList) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }
}
