package com.example.parkingapp.feature_parking.domain.model

data class Floor(
    val name: Char,
    val parkingSpaces: List<ParkingSpace>,
    val isFull: Boolean
)