package com.example.parkingapp.feature_transactions.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket
import com.example.parkingapp.feature_transactions.domain.model.TransactionData
import com.example.parkingapp.feature_transactions.presentation.transactions.TransactionRecyclerViewItem
import com.example.parkingapp.feature_transactions.presentation.transactions.TransactionsFragment

class GetTransactionData(val parkingTicketRepository: ParkingTicketRepository, private val reservationTicketRepository: ReservationTicketRepository) {

    suspend operator fun invoke(vehicleType: String): TransactionData{

        val tickets= mutableListOf<TransactionRecyclerViewItem>()

        val parkingTickets = mutableListOf<ParkingTicket>()
        val reservationTickets = mutableListOf<ReservationTicket>()

        if(vehicleType == TransactionsFragment.ALL_VEHICLE_TYPE){
            parkingTickets.addAll(parkingTicketRepository.getAllTickets())
            reservationTickets.addAll(reservationTicketRepository.getAllTickets())
        } else{
            parkingTickets.addAll(parkingTicketRepository.getTicketsByVehicleType(vehicleType))
            reservationTickets.addAll(reservationTicketRepository.getTicketsByVehicleType(vehicleType))
        }


        val unParkedParkingTickets = parkingTickets.filter { ticket -> ticket.pickUpTime !=null }

        var totalAmount = 0F
        unParkedParkingTickets.forEach {
            it.amountPaid?.let { amount -> totalAmount += amount }
            tickets.add(TransactionRecyclerViewItem.ParkingTicketItem(it))
        }

        reservationTickets.forEach {
            totalAmount+=it.amountPaid
            tickets.add(TransactionRecyclerViewItem.ReservationTicketItem(it))
        }

        return TransactionData(totalAmount, tickets)
    }
}