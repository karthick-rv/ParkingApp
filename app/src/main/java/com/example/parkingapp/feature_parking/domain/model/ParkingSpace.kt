package com.example.parkingapp.feature_parking.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import java.io.Serializable
import java.lang.Exception

@Entity
data class ParkingSpace(
    val type: VehicleType,
    @PrimaryKey val name: String,
    val floorName: Char,
    var free: Boolean,
    var vehicleNum: String?,
    var parkingTicketNum: Long? = null,
    var isReserved: Boolean = false,
    var reservationTicketNum: Float? = null
): Serializable


class ParkingSpaceUnavailableException(private val unavailableMessage: String): Exception(unavailableMessage)