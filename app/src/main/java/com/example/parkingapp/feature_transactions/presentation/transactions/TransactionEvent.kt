package com.example.parkingapp.feature_transactions.presentation.transactions


sealed class TransactionEvent{
    data class GetTransactionForVehicleType(val vehicleType: String): TransactionEvent()
    object GetAllTransaction: TransactionEvent()
}
