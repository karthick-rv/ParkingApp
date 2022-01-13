package com.example.parkingapp.feature_transactions.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_transactions.domain.model.TransactionData
import com.example.parkingapp.feature_transactions.presentation.transactions.TransactionsFragment

class GetTransactionData(val parkingTicketRepository: ParkingTicketRepository) {

    suspend operator fun invoke(vehicleType: String): TransactionData{

        val tickets: List<ParkingTicket> = if(vehicleType == TransactionsFragment.ALL_VEHICLE_TYPE)
            parkingTicketRepository.getAllTickets()
        else
            parkingTicketRepository.getTicketsByVehicleType(vehicleType)

        val unParkedTickets = tickets.filter { ticket -> ticket.pickUpTime !=null }

        var totalAmount = 0F
        unParkedTickets.forEach {
            it.amountPaid?.let { amount -> totalAmount += amount }
        }
        return TransactionData(totalAmount, unParkedTickets)
    }
}