package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.model.ParkingSpaceUnavailableException
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.model.VehicleAlreadyExistException
import com.example.parkingapp.feature_parking.domain.util.DateUtil
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository


class ParkVehicle(
    private val parkingSpaceRepository: ParkingSpaceRepository,
    private val parkingTicketRepository: ParkingTicketRepository,
    val reservationTicketRepository: ReservationTicketRepository
) {

    @Throws(ParkingSpaceUnavailableException::class)
    suspend operator fun invoke(
        vehicle: Vehicle,
        parkingLotManager: ParkingLotManager
    ): ParkingTicket {

        val parkingSpaceWithVehicle =
            parkingSpaceRepository.getSpaceByVehicleNum(vehicle.vehicleNum)

        if (parkingSpaceWithVehicle !== null) {
            throw VehicleAlreadyExistException("Vehicle already exists. Vehicle Number is unique number assigned to your vehicle")
        }

        val parkingSpace =
            GetAvailableParkingSpace(parkingSpaceRepository, reservationTicketRepository)(
                parkingLotManager,
                vehicle
            )

        parkingSpace?.let {
            return addParkingTicket(vehicle, it)
        }
            ?: throw ParkingSpaceUnavailableException("Parking space not available for ${vehicle.type}")
    }

    private suspend fun addParkingTicket(
        vehicle: Vehicle,
        it: ParkingSpace
    ): ParkingTicket {
        val parkingTicket = ParkingTicket(
            ticketId = 0,
            vehicleNum = vehicle.vehicleNum,
            parkingSpaceName = it.name,
            vehicleType = vehicle.type,
            parkedTime = DateUtil.getCurrentDateTime(),
            pickUpTime = null
        )
        it.free = false
        it.vehicleNum = vehicle.vehicleNum
        val ticketId = parkingTicketRepository.insert(parkingTicket)
        it.parkingTicketNum = ticketId
        parkingSpaceRepository.insertSpace(it)
        return parkingTicketRepository.get(ticketId)
    }
}