package com.example.navi_warehouse.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.room.TypeConverters;

import com.example.navi_warehouse.Item.Item;
import com.example.navi_warehouse.Item.ItemDao;
import com.example.navi_warehouse.Order.Converters;
import com.example.navi_warehouse.Order.Order;
import com.example.navi_warehouse.Order.OrderDao;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Item.class, Order.class}, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class WarehouseDatabase extends RoomDatabase {
    public abstract OrderDao orderDao();
    public abstract ItemDao itemDao();

    private static volatile WarehouseDatabase INSTANCE;

    public static WarehouseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WarehouseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WarehouseDatabase.class, "warehouse_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void populateInitialData(Context context) {
        Thread thread = new Thread(() -> {
            WarehouseDatabase db = WarehouseDatabase.getDatabase(context);
            ItemDao itemDao = db.itemDao();
            OrderDao orderDao = db.orderDao();

            if (itemDao.countItems() == 0) { // Only populate if the database is empty
                List<Item> sampleItems = Arrays.asList(
                        new Item(0, "Super Drill", "DRILL_001", 199.99, "Medium", 3.5, "Tools", 1,2),
                        new Item(0, "Smart Watch", "WATCH_002", 299.99, "Small", 0.1, "Electronics", 3,4),
                        new Item(0, "Bluetooth Speaker", "SPEAKER_003", 149.99, "Small", 1.0, "Electronics", 5,6),
                        new Item(0, "Electric Screwdriver", "SCREWDRIVER_004", 89.99, "Small", 0.6, "Tools", 8,9)
                );

                for (Item item : sampleItems) {
                    itemDao.insert(item);
                }
            }

            List<Order> orders = orderDao.getAllOrders().getValue();
            if (orders == null || orders.isEmpty()) {
                List<Item> items = itemDao.getAllItems().getValue();
                if (items != null && !items.isEmpty()) {
                    orderDao.insertOrder(new Order("Order 1", items));
                }
            }
        });
        thread.start();
    }

    public static void clearDatabase(Context context) {
        WarehouseDatabase db = getDatabase(context);
        db.itemDao().deleteAllItems();
    }
}
