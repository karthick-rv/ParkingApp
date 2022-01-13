package com.example.parkingapp.feature_fee_collection.data.repository

import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket

interface ParkingTicketRepository {

    suspend fun insert(parkingTicket: ParkingTicket): Long

    suspend fun delete(parkingTicket: ParkingTicket)

    suspend fun getAllTickets(): List<ParkingTicket>

    suspend fun get(ticketId: Long): ParkingTicket

    suspend fun getTicketsByVehicleType(vehicleType: String): List<ParkingTicket>
}