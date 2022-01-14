package com.example.parkingapp.feature_reservation.data.repository

import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket

interface ReservationTicketRepository {

    suspend fun insert(reservationTicket: ReservationTicket): Long

    suspend fun delete(reservationTicket: ReservationTicket)

    suspend fun getAllTickets(): List<ReservationTicket>

    suspend fun get(ticketId: Long): ReservationTicket

    suspend fun getTicketsByVehicleType(vehicleType: String): List<ReservationTicket>

}