package com.example.parkingapp.feature_fee_collection.domain.model

data class ParkingCoupon(
    val couponCode: String,
    val firstHourFeeDiscountPercentage: Float = 50F,
    val remainingHoursFeeDiscountPercentage: Float = 10F
)
