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
import com.example.navi_warehouse.Map.WarehouseMapParser
import com.example.navi_warehouse.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.mapFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val parser = WarehouseMapParser()

        try {

            val elements = parser.parseSvg(this, R.raw.warehouse_map)

            for (element in elements) {
                Log.d(
                    "WarehouseMap", "Element ID: " + element.id +
                            ", x: " + element.x +
                            ", y: " + element.y +
                            ", width: " + element.width +
                            ", height: " + element.height
                )
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


}