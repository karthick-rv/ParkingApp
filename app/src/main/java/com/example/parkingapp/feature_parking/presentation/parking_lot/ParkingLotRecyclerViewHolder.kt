package com.example.parkingapp.feature_parking.presentation.parking_lot

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.parkingapp.databinding.ItemFloorNameBinding
import com.example.parkingapp.databinding.ItemParkingSpaceBinding

sealed class ParkingLotRecyclerViewHolder(binding:ViewBinding): RecyclerView.ViewHolder(binding.root) {


    class FloorNameViewHolder(private val binding:ItemFloorNameBinding): ParkingLotRecyclerViewHolder(binding){

        fun bind(floorItem: ParkingSpaceRecyclerViewItem.FloorItem){
            binding.floorName.text = floorItem.name
        }

    }


    class ParkingSpaceViewHolder(private val binding:ItemParkingSpaceBinding): ParkingLotRecyclerViewHolder(binding){

        fun bind(parkingSpaceItem: ParkingSpaceRecyclerViewItem.ParkingSpaceItem){
            binding.apply {
                tvParkingSpaceName.text = parkingSpaceItem.parkingSpace.name
                tvParkingSpaceType.text = parkingSpaceItem.parkingSpace.type.toString()
                tvTicketNum.text = parkingSpaceItem.parkingSpace.parkingTicketNum.toString()
            }
        }

    }
}