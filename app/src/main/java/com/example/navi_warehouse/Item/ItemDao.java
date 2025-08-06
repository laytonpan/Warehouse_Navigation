package com.example.navi_warehouse.Item;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for Item entities.
 * Queries now use consistent parameter naming matching the Item fields.
 */
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

    @Query("SELECT COUNT(*) FROM items")
    int countItems();

    @Query("DELETE FROM items")
    void deleteAllItems();

    // Retrieve items by shelf ID, row position, and column position
    @Query("SELECT * FROM items WHERE shelf_id = :shelfId AND row_position = :rowPosition AND column_position = :columnPosition")
    LiveData<List<Item>> getItemsForPosition(int shelfId, int rowPosition, int columnPosition);

    // Retrieve all items in a specific shelf ordered by row and column
    @Query("SELECT * FROM items WHERE shelf_id = :shelfId ORDER BY row_position, column_position")
    LiveData<List<Item>> getAllItemsInShelf(int shelfId);
}
