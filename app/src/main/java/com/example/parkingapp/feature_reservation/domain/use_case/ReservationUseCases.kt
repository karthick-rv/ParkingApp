package com.example.parkingapp.feature_reservation.domain.use_case

data class ReservationUseCases(
    val reserveParkingSpace: ReserveParkingSpace,
    val calculateReservationFees: CalculateReservationFees,
    val unReserveParkingSpace: UnReserveParkingSpace
)