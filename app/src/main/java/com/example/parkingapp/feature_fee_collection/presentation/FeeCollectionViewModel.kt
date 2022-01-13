package com.example.parkingapp.feature_fee_collection.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_fee_collection.domain.model.*
import com.example.parkingapp.feature_fee_collection.domain.use_case.FeeCollectionUseCases
import com.example.parkingapp.feature_fee_collection.presentation.parking_ticket.ParkingTicketFragment
import com.example.parkingapp.feature_fee_collection.presentation.payment.PaymentDetailEvent
import com.example.parkingapp.feature_parking.domain.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeeCollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val feeCollectionUseCases: FeeCollectionUseCases
) : ViewModel() {

    private lateinit var ticket: ParkingTicket
    private lateinit var pickupTime: String
    private lateinit var duration: ParkedDuration
    private var couponApplied: Boolean = false
    private var totalAmount: Float = 0F

    private val _paymentDetailFlow: MutableSharedFlow<PaymentDetailUiEvent> = MutableSharedFlow()
    val paymentDetailFlow: SharedFlow<PaymentDetailUiEvent> = _paymentDetailFlow

    init {
        savedStateHandle.get<Long>(ParkingTicketFragment.PARKING_TICKET_ID)?.let {
            viewModelScope.launch {
                ticket = feeCollectionUseCases.getTicketDetails(it)
                pickupTime = DateUtil.getCurrentDateTime()
                duration = DateUtil.getDuration(ticket.parkedTime, pickupTime)
                val paymentDetail = feeCollectionUseCases.calculateFees(ticket, duration, null)
                totalAmount = paymentDetail.totalFee
                _paymentDetailFlow.emit(PaymentDetailUiEvent.CalculationSuccess(paymentDetail))
            }
        }
    }


    fun onEvent(paymentDetailEvent: PaymentDetailEvent){
        when(paymentDetailEvent){
            is PaymentDetailEvent.ApplyCoupon -> {
                validateAndApplyCoupon(paymentDetailEvent)
            }
            is PaymentDetailEvent.PayParkingFees -> {
                viewModelScope.launch {
                    val parkingTicket = ticket.copy(pickUpTime = pickupTime,duration = duration.toString(),amountPaid = totalAmount, couponApplied = couponApplied )
                    val result = feeCollectionUseCases.payFees(parkingTicket)
                    _paymentDetailFlow.emit(PaymentDetailUiEvent.PaymentResult(result, amountPaid = totalAmount))
                }
            }
        }
    }

    private fun validateAndApplyCoupon(paymentDetailEvent: PaymentDetailEvent.ApplyCoupon) {
        val couponCode = paymentDetailEvent.couponCode
        if (feeCollectionUseCases.calculateFees.isValidCoupon(couponCode)) {
            val parkingCoupon = ParkingCoupon(couponCode)
            val paymentDetail = feeCollectionUseCases.calculateFees(ticket, duration, parkingCoupon)
            totalAmount = paymentDetail.totalFee
            couponApplied = true
            viewModelScope.launch {
                _paymentDetailFlow.emit(
                    PaymentDetailUiEvent.CouponSuccess(
                        "Coupon Applied Successfully",
                        paymentDetail
                    )
                )
            }
        } else {
            viewModelScope.launch {
                _paymentDetailFlow.emit(
                    PaymentDetailUiEvent.CouponError(
                        "Coupon Code not valid "
                    )
                )
            }
        }
    }

}