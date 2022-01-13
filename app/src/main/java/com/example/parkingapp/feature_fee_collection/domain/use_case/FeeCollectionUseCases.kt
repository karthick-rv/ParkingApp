package com.example.parkingapp.feature_fee_collection.domain.use_case

data class FeeCollectionUseCases(
    val getTicketDetails: GetTicketDetails,
    val calculateFees: CalculateFees,
    val payFees: PayFees
)
