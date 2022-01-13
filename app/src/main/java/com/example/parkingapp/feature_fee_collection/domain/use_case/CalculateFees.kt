package com.example.parkingapp.feature_fee_collection.domain.use_case

import com.example.parkingapp.feature_fee_collection.domain.model.ParkedDuration
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingCoupon
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_fee_collection.domain.model.PaymentDetail
import com.example.parkingapp.feature_fee_collection.domain.util.CouponCodes
import com.example.parkingapp.feature_fee_collection.domain.util.ParkingFee
import com.example.parkingapp.feature_fee_collection.domain.util.getFees
import com.example.parkingapp.feature_parking.domain.util.DateUtil

class CalculateFees() {

     operator fun invoke(ticket: ParkingTicket,duration: ParkedDuration, parkingCoupon: ParkingCoupon?): PaymentDetail {

        var hours = duration.hours
        if (duration.minutes > 0) hours++

        val firstHourFee = getFirstHourParkingFee(ticket, parkingCoupon)
        var remainingHourFees = 0F

        if (--hours > 0) {
            for (i in 1..hours) {
                remainingHourFees += getRemainingHourParkingFee(ticket, parkingCoupon)
            }
        }
        val totalFee = firstHourFee + remainingHourFees

        return PaymentDetail(firstHourFee, remainingHourFees, totalFee,  duration)
    }


    private fun getFirstHourParkingFee(ticket: ParkingTicket, parkingCoupon: ParkingCoupon?): Float{
        return parkingCoupon?.let {
            ParkingFee.FirstHourFee(ticket.vehicleType, it.firstHourFeeDiscountPercentage).getFees()
        }?: ParkingFee.FirstHourFee(ticket.vehicleType).getFees()
    }

    private fun getRemainingHourParkingFee(ticket: ParkingTicket, parkingCoupon: ParkingCoupon?): Float{
        return parkingCoupon?.let {
            ParkingFee.RemainingHourFee(ticket.vehicleType, it.remainingHoursFeeDiscountPercentage).getFees()
        }?: ParkingFee.RemainingHourFee(ticket.vehicleType).getFees()
    }

    fun isValidCoupon(couponCode: String): Boolean {
        CouponCodes.values().forEach {
            if(it.toString() == couponCode)
            return true
        }
        return false
    }
}