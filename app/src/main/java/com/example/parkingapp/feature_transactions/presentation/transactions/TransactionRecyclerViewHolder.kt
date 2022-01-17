package com.example.parkingapp.feature_transactions.presentation.transactions

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.parkingapp.databinding.TransactionItemBinding
import com.example.parkingapp.databinding.TransactionReservationItemBinding

sealed class TransactionRecyclerViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root){


    class ParkingTicketViewHolder(private val binding: TransactionItemBinding): TransactionRecyclerViewHolder(binding){

        fun bind(parkingTicketItem: TransactionRecyclerViewItem.ParkingTicketItem){
                binding.parkingTicket = parkingTicketItem.parkingTicket
        }
    }


    class ReservationTicketViewHolder(private val binding: TransactionReservationItemBinding): TransactionRecyclerViewHolder(binding){

        fun bind(reservationTicketItem: TransactionRecyclerViewItem.ReservationTicketItem){
                binding.reservationTicket = reservationTicketItem.reservationTicket
        }
    }
}
