package com.example.parkingapp.feature_fee_collection.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository

class PayFees(val parkingTicketRepository: ParkingTicketRepository, val parkingSpaceRepository: ParkingSpaceRepository) {

    suspend operator fun invoke(parkingTicket: ParkingTicket): Boolean {

        parkingTicketRepository.insert(parkingTicket)

        val parkingSpace = parkingSpaceRepository.getSpaceBy(parkingTicket.parkingSpaceName)

        parkingSpaceRepository.deleteSpace(parkingSpace)

        return true
    }

}