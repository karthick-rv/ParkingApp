package com.example.parkingapp.feature_transactions.presentation.transactions

import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket

sealed class TransactionRecyclerViewItem{

    class ParkingTicketItem(val parkingTicket: ParkingTicket): TransactionRecyclerViewItem()

    class ReservationTicketItem(val reservationTicket: ReservationTicket): TransactionRecyclerViewItem()

}
