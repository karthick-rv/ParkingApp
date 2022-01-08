package com.example.parkingapp.feature_parking.presentation.parking_lot

import com.example.parkingapp.feature_parking.domain.model.Vehicle

sealed class ParkingLotEvent{
    data class Park(val vehicle: Vehicle) : ParkingLotEvent()
    data class UnPark(val vehicle: Vehicle): ParkingLotEvent()
    object ShowParkingLot : ParkingLotEvent()
}
