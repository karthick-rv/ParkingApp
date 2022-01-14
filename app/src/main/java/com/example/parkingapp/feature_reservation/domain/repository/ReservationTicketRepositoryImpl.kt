package com.example.parkingapp.feature_reservation.domain.repository

import com.example.parkingapp.feature_reservation.data.data_source.ReservationTicketDao
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket

class ReservationTicketRepositoryImpl(private val reservationTicketDao: ReservationTicketDao): ReservationTicketRepository {
    override suspend fun insert(reservationTicket: ReservationTicket): Long {
        return reservationTicketDao.insert(reservationTicket)
    }

    override suspend fun delete(reservationTicket: ReservationTicket) {
        reservationTicketDao.delete(reservationTicket)
    }

    override suspend fun getAllTickets(): List<ReservationTicket> {
        return reservationTicketDao.getAll()
    }

    override suspend fun get(ticketId: Long): ReservationTicket {
        return reservationTicketDao.get(ticketId)
    }

    override suspend fun getTicketsByVehicleType(vehicleType: String): List<ReservationTicket> {
        return reservationTicketDao.getByVehicleType(vehicleType)
    }
}