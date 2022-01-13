package com.example.parkingapp.feature_transactions.presentation.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingapp.R
import com.example.parkingapp.databinding.TransactionItemBinding
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private lateinit var parkingTickets: List<ParkingTicket>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<TransactionItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.transaction_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parkingTicket = parkingTickets[position]
        holder.binding.parkingTicket = parkingTicket
    }

    override fun getItemCount(): Int {
        return parkingTickets.size
    }

    fun setParkingTickets(parkingTickets: List<ParkingTicket>){
        this.parkingTickets = parkingTickets
    }

    class ViewHolder(val binding: TransactionItemBinding) : RecyclerView.ViewHolder(binding.root)
}