package com.example.parkingapp.feature_fee_collection.domain.model


data class PaymentDetail(
    val firstHourFee: Float,
    val remainingHourFee: Float,
    val totalFee: Float,
    val parkedDuration: ParkedDuration
)
