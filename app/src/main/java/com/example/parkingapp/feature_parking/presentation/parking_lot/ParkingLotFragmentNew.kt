package com.example.parkingapp.feature_parking.presentation.parking_lot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentParkingLotNewBinding

class ParkingLotFragmentNew: Fragment() {

    private lateinit var binding: FragmentParkingLotNewBinding
    private val parkingLotAdapter by lazy {
        ParkingLotRecyclerViewAdapter()
    }
    private val args by navArgs<ParkingLotFragmentNewArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParkingLotNewBinding.inflate(inflater, container, false)
        val itemList = generateParkingData()
        setupViews(itemList)
        return binding.root
    }

    private fun setupViews(itemList: List<ParkingSpaceRecyclerViewItem>) {
        binding.parkingLotRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = createLayoutManager()
            adapter = parkingLotAdapter
            parkingLotAdapter.items = itemList
        }
    }

    private fun createLayoutManager(): GridLayoutManager {
        val layoutManager = GridLayoutManager(requireContext(), 5)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (parkingLotAdapter.getItemViewType(position)) {
                    R.layout.item_parking_space -> 1
                    R.layout.item_floor_name -> 5
                    else -> 0
                }
            }
        }
        return layoutManager
    }

    private fun generateParkingData(): List<ParkingSpaceRecyclerViewItem> {
        val itemList = mutableListOf<ParkingSpaceRecyclerViewItem>()
        args.parkingLot.floors.forEach {
            itemList.add(ParkingSpaceRecyclerViewItem.FloorItem("Floor " + it.name))
            it.parkingSpaces.forEach { parkingSpace ->
                itemList.add(ParkingSpaceRecyclerViewItem.ParkingSpaceItem(parkingSpace))
            }
        }
        return itemList.toList()
    }

}