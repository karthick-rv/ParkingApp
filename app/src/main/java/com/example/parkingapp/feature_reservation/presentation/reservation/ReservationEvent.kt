package com.example.parkingapp.feature_reservation.presentation.reservation

import com.example.parkingapp.feature_parking.domain.model.Vehicle

sealed class ReservationEvent{

    data class CalculateFees(val vehicle: Vehicle, val reservationDate: String): ReservationEvent()

    object ReserveParkingSpace: ReservationEvent()

    data class UnReserveParkingSpace(val vehicle: Vehicle): ReservationEvent()
}
