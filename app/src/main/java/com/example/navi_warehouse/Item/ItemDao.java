package com.example.navi_warehouse.Item;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * FROM items WHERE id = :id")
    LiveData<Item> getItemById(int id);

    // Method to count the number of items in the database
    @Query("SELECT COUNT(*) FROM items")
    int countItems();

    // Delete all the item in the class
    @Query("DELETE FROM items")
    void deleteAllItems();
}
