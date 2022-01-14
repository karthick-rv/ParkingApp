package com.example.parkingapp.feature_parking.presentation.parking_lot

import com.example.parkingapp.feature_parking.domain.model.ParkingSpace

sealed class ParkingSpaceRecyclerViewItem {

    class FloorItem(val name: String): ParkingSpaceRecyclerViewItem()

    class ParkingSpaceItem(val parkingSpace: ParkingSpace): ParkingSpaceRecyclerViewItem()

}