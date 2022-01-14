package com.example.parkingapp.feature_reservation.domain.util

import com.example.parkingapp.feature_parking.domain.util.VehicleType

private const val CAR_EXTRA = 250F
private const val BUS_EXTRA = 300F

data class ReservationFee(
    val baseAmount: Float,
    val vehicleType: VehicleType,
    val discountPercentage: Float
)

fun ReservationFee.getFees(): Float {
    val amount = amountForVehicleType(vehicleType, baseAmount)
    val discountAmount = (amount * discountPercentage) / 100
    return amount - discountAmount
}


private fun amountForVehicleType(vehicleType: VehicleType, baseAmount: Float): Float {
    return when (vehicleType) {  // TODO(Check Vehicle Type can be converted into sealed class from enum)
        VehicleType.BIKE -> baseAmount
        VehicleType.CAR -> baseAmount + CAR_EXTRA
        VehicleType.BUS -> baseAmount + BUS_EXTRA
    }
}