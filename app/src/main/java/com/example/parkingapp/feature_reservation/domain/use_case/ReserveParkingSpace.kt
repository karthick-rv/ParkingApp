package com.example.parkingapp.feature_reservation.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingSpaceUnavailableException
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.use_case.GetAvailableParkingSpace
import com.example.parkingapp.feature_parking.domain.use_case.ParkingLotManager
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket
import com.example.parkingapp.feature_reservation.domain.util.ReservationFee
import com.example.parkingapp.feature_reservation.domain.util.getFees
import kotlin.jvm.Throws

class ReserveParkingSpace(val repository: ReservationTicketRepository, val parkingSpaceRepository: ParkingSpaceRepository) {

    @Throws(ParkingSpaceUnavailableException::class)
    suspend operator fun invoke(vehicle: Vehicle, reservationDate: String, parkingLotManager: ParkingLotManager): ReservationTicket {

        val amountToPay = ReservationFee(2000F, vehicle.type, 0F).getFees()

        val parkingSpace = GetAvailableParkingSpace(parkingSpaceRepository, repository)(parkingLotManager, vehicle)

        parkingSpace?.let {
            val reservationTicket = ReservationTicket(ticketId = 0, vehicle.type, vehicle.vehicleNum,it.name, reservationDate, amountToPay)
            val ticketId = repository.insert(reservationTicket)
            return repository.get(ticketId)
        }?: throw ParkingSpaceUnavailableException("Parking space for ${vehicle.type} unavailable to reserve")
    }
}