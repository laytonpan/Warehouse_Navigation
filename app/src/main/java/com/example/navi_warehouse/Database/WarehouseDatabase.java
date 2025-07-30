package com.example.navi_warehouse.Database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.navi_warehouse.Item.Item;
import com.example.navi_warehouse.Item.ItemDao;
import com.example.navi_warehouse.Order.Converters;
import com.example.navi_warehouse.Order.Order;
import com.example.navi_warehouse.Order.OrderDao;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Item.class, Order.class}, version = 5, exportSchema = false)
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
                            .fallbackToDestructiveMigration() // Automatically recreates the database
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
                        // Shelf 1
                        new Item(0, "Super Drill", "DRILL_001", 199.99, "Medium", 3.5, "Tools", 1, 1, 1),
                        new Item(0, "Smart Watch", "WATCH_002", 299.99, "Small", 0.1, "Electronics", 1, 1, 2),
                        new Item(0, "Mini Fan", "FAN_019", 49.99, "Small", 0.4, "Home", 1, 1, 3),

                        // Shelf 2
                        new Item(0, "Bluetooth Speaker", "SPEAKER_003", 149.99, "Small", 1.0, "Electronics", 2, 2, 1),
                        new Item(0, "Electric Screwdriver", "SCREWDRIVER_004", 89.99, "Small", 0.6, "Tools", 2, 2, 2),
                        new Item(0, "Smart Glasses", "GLASSES_020", 249.99, "Small", 0.3, "Electronics", 2, 2, 3),

                        // Shelf 3
                        new Item(0, "Cordless Vacuum", "VACUUM_005", 249.99, "Large", 4.5, "Home Appliances", 3, 3, 1),
                        new Item(0, "Gaming Headset", "HEADSET_006", 199.99, "Small", 0.8, "Electronics", 3, 3, 2),
                        new Item(0, "Smart Scale", "SCALE_021", 69.99, "Medium", 1.2, "Health", 3, 3, 3),

                        // Shelf 4
                        new Item(0, "Robot Cleaner", "CLEANER_007", 399.99, "Large", 5.0, "Home Appliances", 4, 4, 1),
                        new Item(0, "Mechanical Keyboard", "KEYBOARD_008", 149.99, "Small", 1.2, "Electronics", 4, 4, 2),
                        new Item(0, "USB Hub", "USB_022", 29.99, "Small", 0.2, "Accessories", 4, 4, 3),

                        // Shelf 5
                        new Item(0, "Smart Thermostat", "THERMO_009", 179.99, "Small", 0.3, "Electronics", 5, 1, 1),
                        new Item(0, "LED Desk Lamp", "LAMP_010", 59.99, "Small", 0.5, "Home", 5, 1, 2),
                        new Item(0, "Smart Air Monitor", "AIR_023", 89.99, "Small", 0.6, "Health", 5, 1, 3),

                        // Shelf 6
                        new Item(0, "Portable Power Bank", "POWER_011", 49.99, "Small", 0.2, "Electronics", 6, 2, 1),
                        new Item(0, "Wireless Mouse", "MOUSE_012", 39.99, "Small", 0.1, "Electronics", 6, 2, 2),
                        new Item(0, "Smart Tag", "TAG_024", 19.99, "Tiny", 0.05, "Accessories", 6, 2, 3),

                        // Shelf 7
                        new Item(0, "Electric Kettle", "KETTLE_013", 89.99, "Medium", 1.0, "Home Appliances", 7, 1, 1),
                        new Item(0, "Rice Cooker", "COOKER_014", 129.99, "Medium", 3.0, "Home Appliances", 7, 1, 2),
                        new Item(0, "Water Filter", "FILTER_025", 59.99, "Medium", 2.0, "Home", 7, 1, 3),

                        // Shelf 8
                        new Item(0, "Smart Plug", "PLUG_015", 29.99, "Small", 0.2, "Electronics", 8, 2, 1),
                        new Item(0, "WiFi Extender", "EXTENDER_016", 59.99, "Small", 0.4, "Electronics", 8, 2, 2),

                        // Shelf 9
                        new Item(0, "Surge Protector", "SURGE_017", 69.99, "Medium", 0.8, "Electronics", 9, 3, 1),
                        new Item(0, "Security Camera", "CAMERA_018", 149.99, "Small", 0.6, "Security", 9, 3, 2)
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