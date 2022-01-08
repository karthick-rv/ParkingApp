package com.example.parkingapp.feature_fee_collection.data.repository

import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket

interface ParkingTicketRepository {

    suspend fun insert(parkingTicket: ParkingTicket)

    suspend fun delete(parkingTicket: ParkingTicket)

    suspend fun getAll(): List<ParkingTicket>

    suspend fun get(parkingSpaceName: Int): ParkingTicket
}