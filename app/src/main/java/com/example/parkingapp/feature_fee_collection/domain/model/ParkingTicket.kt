package com.example.parkingapp.feature_fee_collection.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import java.io.Serializable

@Entity
data class ParkingTicket(
    @PrimaryKey(autoGenerate = true) val ticketId: Long,
    val vehicleNum: String,
    val vehicleType: VehicleType,
    val parkingSpaceName: String,
    val parkedTime: String,
    val pickUpTime: String?,
    val duration: String? = null,
    val amountPaid: Float? = null,
    val couponApplied: Boolean = false
): Serializable




/*
*
* Reserve Parking Space -> Vehicle Num, Vehicle Type, Parking Date ->
* 1. Allot the parking Space
* 2. Create Reservation Ticket
* 3. Add Reservation Ticket to the database
*
* Park on Reserved Space -> Reservation Ticket Num, Vehicle Num, Vehicle Type ->
* 1.check reserved date is today and other validation -> Succeeds -> Parked on that specific reserved space
*
*
* UnPark from Reserved Space -> Reservation Ticket num , Vehicle num -> Succeeds ->
* UnPark the vehicle, But not delete the ticket from database
*
*
*
* If the vehicle has been parked more than the reserved date and time, calculate parking fees based on time
*
*
*
*
* */