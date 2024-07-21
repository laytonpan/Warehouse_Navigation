package com.example.navi_warehouse.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.navi_warehouse.Item.Item
import com.example.navi_warehouse.Database.WarehouseDatabase


class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val itemDao = WarehouseDatabase.getDatabase(application).itemDao()

    // Expose the list of items as LiveData
    val allItems: LiveData<List<Item>> = itemDao.getAllItems()
}