package com.example.parkingapp.feature_fee_collection.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket

class GetTicketDetails(val repository: ParkingTicketRepository) {

    suspend operator fun invoke(ticketId: Long): ParkingTicket {
        return repository.get(ticketId)
    }

}