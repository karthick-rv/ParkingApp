package com.example.parkingapp.feature_parking.presentation.parking_lot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.R
import com.example.parkingapp.databinding.ItemFloorNameBinding
import com.example.parkingapp.databinding.ItemParkingSpaceBinding
import java.lang.IllegalArgumentException

class ParkingLotRecyclerViewAdapter: RecyclerView.Adapter<ParkingLotRecyclerViewHolder>() {

    var items = mutableListOf<ParkingSpaceRecyclerViewItem>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParkingLotRecyclerViewHolder {
        return when(viewType){
            R.layout.item_floor_name -> {
                ParkingLotRecyclerViewHolder.FloorNameViewHolder(
                    ItemFloorNameBinding.inflate(LayoutInflater.from(parent.context), parent,false ))
            }

            R.layout.item_parking_space ->ParkingLotRecyclerViewHolder.ParkingSpaceViewHolder(
                ItemParkingSpaceBinding.inflate(LayoutInflater.from(parent.context), parent,false ))

            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: ParkingLotRecyclerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when(holder){
            is ParkingLotRecyclerViewHolder.FloorNameViewHolder -> holder.bind(items[position] as ParkingSpaceRecyclerViewItem.FloorItem)
            is ParkingLotRecyclerViewHolder.ParkingSpaceViewHolder -> holder.bind(items[position] as ParkingSpaceRecyclerViewItem.ParkingSpaceItem)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is ParkingSpaceRecyclerViewItem.FloorItem -> R.layout.item_floor_name
            is ParkingSpaceRecyclerViewItem.ParkingSpaceItem -> R.layout.item_parking_space
        }
    }
}