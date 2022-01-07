package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.Vehicle

class ParkVehicle(private val repository: ParkingSpaceRepository) {

    suspend operator fun invoke(parkingLot: ParkingLot, vehicle: Vehicle) : ParkingLot {

        val floor =
            parkingLot.floors.filter { floor -> !floor.isFull }.minByOrNull { floor -> floor.name }

        val parkingSpace = floor?.let {
            it.parkingSpaces.firstOrNull { parkingSpace -> parkingSpace.free }
        }

        parkingSpace?.let {
//            val parkingSpaceWithVehicle = parkingSpace.copy(vehicleNum = vehicle.vehicleNum, free = false)
            it.free = false
            it.vehicleNum = vehicle.vehicleNum
            repository.insertSpace(it)
        }

        return parkingLot
    }

}


fun <T> List<T>.update(index: Int, item: T): List<T> = toMutableList().apply { this[index] = item }