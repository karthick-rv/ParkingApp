package com.example.parkingapp.feature_parking.data.repository

import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import kotlinx.coroutines.flow.Flow

interface ParkingSpaceRepository {

    suspend fun insertSpace(parkingSpace: ParkingSpace)

    suspend fun deleteSpace(parkingSpace: ParkingSpace)

    suspend fun getAllSpaces(): List<ParkingSpace>

    suspend fun getSpaceBy(vehicleNum: Int): ParkingSpace

}