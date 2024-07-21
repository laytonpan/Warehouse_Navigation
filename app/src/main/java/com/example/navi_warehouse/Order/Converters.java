package com.example.navi_warehouse.Order;

import androidx.room.TypeConverter;

import com.example.navi_warehouse.Item.Item;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromItemList(List<Item> items) {
        Gson gson = new Gson();
        return gson.toJson(items);
    }

    @TypeConverter
    public static List<Item> toItemList(String itemsString) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Item>>() {}.getType();
        return gson.fromJson(itemsString, listType);
    }
}