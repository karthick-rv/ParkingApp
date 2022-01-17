package com.example.parkingapp.feature_reservation.domain.use_case

import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository

class UnReserveParkingSpace(val repository: ReservationTicketRepository) {

    suspend operator fun invoke(vehicle: Vehicle): Boolean {

        val tickets = repository.getAllTickets()

        val ticketList = tickets.filter { reservationTicket -> reservationTicket.ticketId == vehicle.reservationTicketNum?.toLong() && reservationTicket.vehicleNum == vehicle.vehicleNum }

        if(ticketList.isNotEmpty()){
            val reservationTicket = ticketList[0]
            repository.delete(reservationTicket)
            return true
        }
        return false
    }
}