package com.example.navi_warehouse.Order;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insertOrder(Order order);

    @Query("SELECT * FROM orders WHERE order_id = :orderId")
    LiveData<Order> getOrderById(String orderId);

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();
}