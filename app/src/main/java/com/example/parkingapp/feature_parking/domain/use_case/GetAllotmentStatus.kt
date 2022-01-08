package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingLot


class GetAllotmentStatus(private val repository: ParkingSpaceRepository) {

    suspend operator fun invoke(parkingLot: ParkingLot): ParkingLot{

        val parkingSpacesWithVehicle = repository.getAllSpaces()

        parkingSpacesWithVehicle.forEach { parkingSpace ->
            val floorName = parkingSpace.floorName
            val parkingSpaceNumber = parkingSpace.name.substring(1).toInt()

            val floor = parkingLot.floors.first { floor -> floor.name == floorName }
            val space = floor.parkingSpaces[parkingSpaceNumber - 1]

            space.apply {
                free = parkingSpace.free
                vehicleNum = parkingSpace.vehicleNum
            }
        }
        updateFloorStatus(parkingLot)
        updateParkingLotStatus(parkingLot)
        return parkingLot
    }

    private fun updateFloorStatus(parkingLot: ParkingLot) {
        parkingLot.floors.forEach {
            val freeSpaces = it.parkingSpaces.filter { parkingSpace -> parkingSpace.free }
            if(freeSpaces.isEmpty()) it.isFull = true
        }
    }

    private fun updateParkingLotStatus(parkingLot: ParkingLot) {
        val floorWithSpaces = parkingLot.floors.filter { floor -> !floor.isFull }
        if(floorWithSpaces.isEmpty()) parkingLot.isFull = true
    }
}