package com.example.parkingapp.feature_fee_collection.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import java.util.*

@Entity
data class ParkingTicket(
    @PrimaryKey val ticketId: String,
    val vehicleNum: String,
    val vehicleType: VehicleType,
    val parkingSpaceName: String,
    val parkedTime: Date,
    val pickUpTime: Date,
)