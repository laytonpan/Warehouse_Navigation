package com.example.navi_warehouse.OrderTest;

import com.example.navi_warehouse.Order.Order;
import com.example.navi_warehouse.Item.Item;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderTest {

    @Test
    public void testOrderCreation() {
        List<Item> items = Arrays.asList(
                new Item(0, "Super Drill", "DRILL_001", 199.99, "Medium", 3.5, "Tools", 10, 20),
                new Item(0, "Smart Watch", "WATCH_002", 299.99, "Small", 0.1, "Electronics", 15, 25)
        );

        Order order = new Order("Order 1", items);

        assertNotNull(order);
        assertEquals("Order 1", order.getOrderId());
        assertEquals(2, order.getItems().size());
    }
}
