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

        // Create Map
        warehouseMapModel = WarehouseMapSimpleExample.createSimpleMap(30)
        val mapView: CustomMapView = view.findViewById(R.id.custom_map_view)
        mapView.setWarehouseMapModel(warehouseMapModel)

        // Passing routine ID
        val pathNodeIds = arguments?.getStringArrayList(ARG_PATH_NODE_IDS)

        if (pathNodeIds != null && warehouseMapModel != null) {
            val pathNodes = pathNodeIds.mapNotNull { id -> warehouseMapModel!!.getNode(id) }
            mapView.setHighlightedPath(pathNodes) // 高亮路径
        }

    }

    companion object {
        private const val ARG_PATH_NODE_IDS = "path_node_ids"

        fun newInstance(pathNodeIds: ArrayList<String>): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_PATH_NODE_IDS, pathNodeIds)
            fragment.arguments = args
            return fragment
        }
    }

}