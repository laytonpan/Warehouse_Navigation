package com.example.navi_warehouse

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.navi_warehouse.Database.WarehouseDatabase
import com.example.navi_warehouse.Database.WarehouseDatabase.populateInitialData
import com.example.navi_warehouse.Map.WarehouseMapModel
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample
import com.example.navi_warehouse.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var warehouseMapModel: WarehouseMapModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database with sample data (For testing)
        lifecycleScope.launch(Dispatchers.IO) {
            WarehouseDatabase.clearDatabase(applicationContext)
            populateInitialData(applicationContext)
        }

        val navView: BottomNavigationView = binding.navView

        // ✅ Set the toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_order,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.mapFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // ✅ Custom nav logic to pop back and ensure root fragment for each tab
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_order,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.mapFragment -> {
                    navController.popBackStack(item.itemId, false)
                    navController.navigate(item.itemId)
                    true
                }
                else -> false
            }
        }

        // Load the WarehouseMapModel
        loadWarehouseMapModel()
    }

    private fun loadWarehouseMapModel() {
        Executors.newSingleThreadExecutor().execute {
            warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(300)
            Log.d("MainActivity", "Warehouse map model initialized!")
        }
    }

    fun getWarehouseMapModel(): WarehouseMapModel? {
        return warehouseMapModel
    }
}
