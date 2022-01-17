package com.example.parkingapp.feature_parking.domain.model

import com.example.parkingapp.feature_parking.domain.util.VehicleType
import java.lang.Exception

data class Vehicle (
    val vehicleNum : String,
    val model: String,
    val parkingSpaceNum: String,
    val type: VehicleType,
    val parkingTicketNum: String? = null,
    val reservationTicketNum: String? = null
)

class VehicleAlreadyExistException(private val unavailableMessage: String): Exception(unavailableMessage)

class VehicleNotAvailableException(private val unavailableMessage: String): Exception(unavailableMessage)