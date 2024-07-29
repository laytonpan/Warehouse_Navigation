package com.example.navi_warehouse.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.navi_warehouse.R
import com.example.navi_warehouse.Item.Item
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // show item position
        val items = listOf(
            Item(0, "Super Drill", "DRILL_001", 199.99, "Medium", 3.5, "Tools", 10, 20),
            Item(0, "Smart Watch", "WATCH_002", 299.99, "Small", 0.1, "Electronics", 15, 25)
        )

        for (item in items) {
            val position = LatLng(item.locationX.toDouble(), item.locationY.toDouble())
            map.addMarker(MarkerOptions().position(position).title(item.name))
        }

        // Move the camera to the first item
        if (items.isNotEmpty()) {
            val firstItemPosition = LatLng(items[0].locationX.toDouble(), items[0].locationY.toDouble())
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemPosition, 15f))
        }
    }
}
