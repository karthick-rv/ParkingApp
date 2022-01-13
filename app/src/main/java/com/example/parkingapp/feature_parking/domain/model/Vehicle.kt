package com.example.parkingapp.feature_parking.domain.model

import com.example.parkingapp.feature_parking.domain.util.VehicleType

data class Vehicle (
    val vehicleNum : String,
    val model: String,
    val parkingSpaceNum: String,
    val type: VehicleType,
    val parkingTicketNum: String? = null,
)