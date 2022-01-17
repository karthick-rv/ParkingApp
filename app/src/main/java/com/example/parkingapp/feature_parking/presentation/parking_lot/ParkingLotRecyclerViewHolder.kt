package com.example.parkingapp.feature_parking.presentation.parking_lot

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.parkingapp.R
import com.example.parkingapp.databinding.ItemFloorNameBinding
import com.example.parkingapp.databinding.ItemParkingSpaceBinding

@SuppressLint("SetTextI18n")
sealed class ParkingLotRecyclerViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {


    class FloorNameViewHolder(private val binding: ItemFloorNameBinding) :
        ParkingLotRecyclerViewHolder(binding) {


        fun bind(floorItem: ParkingSpaceRecyclerViewItem.FloorItem) {
            binding.floorName.text = "Floor ${floorItem.name}"
        }
    }

    class ParkingSpaceViewHolder(private val binding: ItemParkingSpaceBinding) :
        ParkingLotRecyclerViewHolder(binding) {

        fun bind(parkingSpaceItem: ParkingSpaceRecyclerViewItem.ParkingSpaceItem) {
            binding.apply {
                if (!parkingSpaceItem.parkingSpace.free)
                    parkingSpaceLayout.setBackgroundResource(R.drawable.rectangle_shape_grey)
                if (parkingSpaceItem.parkingSpace.isReserved) {
                    parkingSpaceLayout.setBackgroundResource(R.drawable.rectangle_shape_reserved)
                    reserved.visibility = View.VISIBLE
                }
                tvParkingSpaceName.text = parkingSpaceItem.parkingSpace.name
                tvParkingSpaceType.text = parkingSpaceItem.parkingSpace.type.toString()
                tvVehicleName.text = parkingSpaceItem.parkingSpace.vehicleNum

                if (parkingSpaceItem.parkingSpace.isReserved) {
                    var text = parkingSpaceItem.parkingSpace.reservationTicketNum?.toInt().toString()
                    if(!parkingSpaceItem.parkingSpace.free)
                        text += " PARKED "
                    tvTicketNum.text = text
                }
                else if (parkingSpaceItem.parkingSpace.parkingTicketNum != null) {
                    tvTicketNum.text = "No- ${parkingSpaceItem.parkingSpace.parkingTicketNum.toString()}"
                } else {
                    tvTicketNum.visibility = View.GONE
                }
            }

        }
    }

}
