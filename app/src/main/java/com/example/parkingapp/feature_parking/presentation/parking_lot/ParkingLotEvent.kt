package com.example.parkingapp.feature_parking.presentation.parking_lot

import com.example.parkingapp.feature_parking.domain.model.LoadingInfo
import com.example.parkingapp.feature_parking.domain.model.Vehicle

sealed class ParkingLotEvent{
    data class Park(val vehicle: Vehicle, val isReserved: Boolean) : ParkingLotEvent()
    data class UnPark(val vehicle: Vehicle, val isReserved: Boolean): ParkingLotEvent()
    data class ShowParkingLot(val loadingInfo: LoadingInfo) : ParkingLotEvent()
}
