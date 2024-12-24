package com.example.navi_warehouse.ui.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.navi_warehouse.Map.CustomMapView
import com.example.navi_warehouse.Map.WarehouseMapModel
import com.example.navi_warehouse.R
import com.example.navi_warehouse.Map.WarehouseMapSimpleExample

class MapFragment : Fragment() {
    private var warehouseMapModel: WarehouseMapModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a simple warehouse map model using WarehouseMapExample
        warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(30)

        // Draw the warehouse map on the view
        val mapView: CustomMapView = view.findViewById(R.id.custom_map_view)
        mapView.setWarehouseMapModel(warehouseMapModel)
    }

}