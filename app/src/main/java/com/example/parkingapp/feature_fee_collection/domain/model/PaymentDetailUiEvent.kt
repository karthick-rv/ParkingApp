package com.example.parkingapp.feature_fee_collection.domain.model

sealed class PaymentDetailUiEvent {
    data class CouponSuccess(val msg: String, val paymentDetail: PaymentDetail): PaymentDetailUiEvent()
    data class CouponError(val errorMsg: String): PaymentDetailUiEvent()
    data class PaymentResult(val isSuccess: Boolean, val amountPaid: Float? =null): PaymentDetailUiEvent()
    data class CalculationSuccess(val paymentDetail: PaymentDetail) : PaymentDetailUiEvent()
}