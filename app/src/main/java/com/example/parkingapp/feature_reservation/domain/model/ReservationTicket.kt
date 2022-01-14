package com.example.parkingapp.feature_reservation.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import java.io.Serializable

@Entity
data class ReservationTicket(
    @PrimaryKey(autoGenerate = true) val ticketId : Long,
    val vehicleType: VehicleType,
    val vehicleNum: String,
    val parkingSpaceName: String,
    val date: String,
    val amountPaid: Float,
):Serializable