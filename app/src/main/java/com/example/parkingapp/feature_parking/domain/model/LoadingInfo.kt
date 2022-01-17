package com.example.parkingapp.feature_parking.domain.model

data class LoadingInfo(
    val floorIndex: Int,
    val loadIndex: Int,
    val loadRem: Int? = null
)
