package com.example.navi_warehouse.Order;

import java.util.List;

public class CompletedOrder {
    private final String id;
    private final long timestamp;
    private final List<ItemStatus> items;

    public CompletedOrder(String id, long timestamp, List<ItemStatus> items) {
        this.id = id;
        this.timestamp = timestamp;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<ItemStatus> getItems() {
        return items;
    }
}
