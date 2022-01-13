package com.example.parkingapp.feature_transactions.domain.model

import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket

data class TransactionData(
    val totalAmount: Float,
    val transactions: List<ParkingTicket>
)