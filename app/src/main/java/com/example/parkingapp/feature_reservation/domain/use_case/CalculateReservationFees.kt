package com.example.parkingapp.feature_reservation.domain.use_case

import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_reservation.domain.util.ReservationFee
import com.example.parkingapp.feature_reservation.domain.util.getFees

class CalculateReservationFees() {

    operator fun invoke(vehicle:Vehicle) : Float{

        return ReservationFee(2000F, vehicle.type, 0F).getFees()

    }

}