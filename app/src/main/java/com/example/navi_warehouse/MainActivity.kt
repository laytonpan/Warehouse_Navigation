package com.example.navi_warehouse

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.navi_warehouse.Database.WarehouseDatabase
import com.example.navi_warehouse.Database.WarehouseDatabase.populateInitialData
import com.example.navi_warehouse.Map.WarehouseMapModel
import com.example.navi_warehouse.Map.WarehouseMapParser
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample
import com.example.navi_warehouse.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
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
        navView.setupWithNavController(navController)

        // Load the WarehouseMapModel
        loadWarehouseMapModel()
    }

    private fun loadWarehouseMapModel() {
        Executors.newSingleThreadExecutor().execute {
            // Dynamically generate the map model using the WarehouseMapSimpleExample
            warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(300)
            Log.d("MainActivity", "Warehouse map model initialized!")
        }
    }

    fun getWarehouseMapModel(): WarehouseMapModel? {
        return warehouseMapModel
    }
}



