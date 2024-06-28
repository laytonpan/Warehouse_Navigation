package com.example.navi_warehouse.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navi_warehouse.Database.ItemAdapter
import com.example.navi_warehouse.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAdapter = ItemAdapter(requireContext())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }

        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            itemAdapter.setItems(items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
