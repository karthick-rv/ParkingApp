package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace

class GetParkedSpaces(val repository: ParkingSpaceRepository){

    suspend operator fun invoke(): List<ParkingSpace> {
        return repository.getAllSpaces()
    }
}