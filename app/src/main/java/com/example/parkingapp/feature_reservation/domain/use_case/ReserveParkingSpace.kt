package com.example.parkingapp.feature_reservation.domain.use_case

import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket
import com.example.parkingapp.feature_reservation.domain.util.ReservationFee
import com.example.parkingapp.feature_reservation.domain.util.getFees

class ReserveParkingSpace(val repository: ReservationTicketRepository) {

    suspend operator fun invoke(vehicle: Vehicle, reservationDate: String): ReservationTicket {

        val amountToPay = ReservationFee(2000F, vehicle.type, 0F).getFees()

        val reservationTicket = ReservationTicket(ticketId = 0, vehicle.type, vehicle.vehicleNum,"", reservationDate, amountToPay)

        val ticketId = repository.insert(reservationTicket)

        return repository.get(ticketId)
    }
}