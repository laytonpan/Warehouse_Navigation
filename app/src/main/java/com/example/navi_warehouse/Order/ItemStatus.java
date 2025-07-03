package com.example.navi_warehouse.Order;

import com.example.navi_warehouse.Item.Item;

public class ItemStatus {
    private final Item item;
    private final PickStatus status;

    public ItemStatus(Item item, PickStatus status) {
        this.item = item;
        this.status = status;
    }

    public Item getItem() {
        return item;
    }

    public PickStatus getStatus() {
        return status;
    }
}
