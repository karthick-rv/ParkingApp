package com.example.parkingapp.feature_fee_collection.domain.util

import com.example.parkingapp.feature_parking.domain.util.VehicleType


private const val FIRST_HOUR_FEE = 100F
private const val REMAINING_HOUR_FEE = 60F

private const val CAR_EXTRA = 50F
private const val BUS_EXTRA = 100F


sealed class ParkingFee(
    val baseAmount: Float,
    val vehicleType: VehicleType,
    val discountPercentage: Float
) {
    class FirstHourFee(vehicleType: VehicleType, discountPercentage: Float = 0F) :
        ParkingFee(FIRST_HOUR_FEE, vehicleType, discountPercentage)

    class RemainingHourFee(vehicleType: VehicleType, discountPercentage: Float = 0F) :
        ParkingFee(REMAINING_HOUR_FEE, vehicleType, discountPercentage)
}


fun ParkingFee.getFees(): Float {
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