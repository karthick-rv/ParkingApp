package com.example.parkingapp.feature_fee_collection.presentation.payment


sealed class PaymentDetailEvent{
    data class ApplyCoupon(val couponCode: String): PaymentDetailEvent()
    object PayParkingFees: PaymentDetailEvent()
}
