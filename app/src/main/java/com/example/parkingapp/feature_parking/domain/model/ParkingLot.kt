package com.example.parkingapp.feature_parking.domain.model

import java.io.Serializable

data class ParkingLot(
    val name: String,
    val floors: List<Floor>,
    var isFull: Boolean
):Serializable