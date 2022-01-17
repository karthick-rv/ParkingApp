package com.example.parkingapp.feature_transactions.domain.model

import com.example.parkingapp.feature_transactions.presentation.transactions.TransactionRecyclerViewItem

data class TransactionData(
    val totalAmount: Float,
    val transactions: List<TransactionRecyclerViewItem>
)