package com.example.navi_warehouse.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import java.util.Arrays;
import java.util.List;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class WarehouseDatabase extends RoomDatabase {
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
            ItemDao dao = db.itemDao();

            if (dao.countItems() == 0) { // Only populate if the database is empty
                List<Item> sampleItems = Arrays.asList(
                        new Item(0, "Super Drill", "DRILL_001", 199.99, "Medium", 3.5, "Tools", "Section A"),
                        new Item(0, "Smart Watch", "WATCH_002", 299.99, "Small", 0.1, "Electronics", "Section B"),
                        new Item(0, "Bluetooth Speaker", "SPEAKER_003", 149.99, "Small", 1.0, "Electronics", "Section C"),
                        new Item(0, "Electric Screwdriver", "SCREWDRIVER_004", 89.99, "Small", 0.6, "Tools", "Section A")
                );

                for (Item item : sampleItems) {
                    dao.insert(item);
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
