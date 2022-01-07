package com.example.parkingapp.feature_parking.presentation.system_create

sealed class SystemCreateResponse{
    data class Success(val floorCount: Int, val parkingSpaceCount: Int) : SystemCreateResponse()
    data class Error(val message:String): SystemCreateResponse()
}
