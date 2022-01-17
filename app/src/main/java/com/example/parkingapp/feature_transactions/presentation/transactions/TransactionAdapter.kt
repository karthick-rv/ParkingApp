package com.example.parkingapp.feature_transactions.presentation.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.R
import com.example.parkingapp.databinding.ItemFloorNameBinding
import com.example.parkingapp.databinding.ItemParkingSpaceBinding
import com.example.parkingapp.databinding.TransactionItemBinding
import com.example.parkingapp.databinding.TransactionReservationItemBinding
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingLotRecyclerViewHolder
import com.example.parkingapp.feature_parking.presentation.parking_lot.ParkingSpaceRecyclerViewItem
import java.lang.IllegalArgumentException

class TransactionAdapter : RecyclerView.Adapter<TransactionRecyclerViewHolder>() {

    private lateinit var tickets: List<TransactionRecyclerViewItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRecyclerViewHolder {
        return when(viewType){
            R.layout.transaction_item -> {
                TransactionRecyclerViewHolder.ParkingTicketViewHolder(
                    TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent,false ))
            }

            R.layout.transaction_reservation_item -> TransactionRecyclerViewHolder.ReservationTicketViewHolder(
                TransactionReservationItemBinding.inflate(LayoutInflater.from(parent.context), parent,false ))

            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: TransactionRecyclerViewHolder, position: Int) {
        when(holder){
            is TransactionRecyclerViewHolder.ParkingTicketViewHolder -> holder.bind(tickets[position] as TransactionRecyclerViewItem.ParkingTicketItem)
            is TransactionRecyclerViewHolder.ReservationTicketViewHolder -> holder.bind(tickets[position] as TransactionRecyclerViewItem.ReservationTicketItem)
        }
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    fun setParkingTickets(parkingTickets: List<TransactionRecyclerViewItem>){
        this.tickets = parkingTickets
    }

    override fun getItemViewType(position: Int): Int {
        return when(tickets[position]){
            is TransactionRecyclerViewItem.ParkingTicketItem -> R.layout.transaction_item
            is TransactionRecyclerViewItem.ReservationTicketItem -> R.layout.transaction_reservation_item
        }
    }
}