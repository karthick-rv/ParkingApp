package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.Vehicle

class UnParkVehicle(val repository: ParkingSpaceRepository) {

    suspend operator fun invoke(parkingLot: ParkingLot, vehicle: Vehicle): ParkingLot {
        val parkedSpaces = repository.getAllSpaces()

        val parkedSpaceList =
            parkedSpaces.filter { parkingSpace -> parkingSpace.name == vehicle.parkingSpaceNum
                    && parkingSpace.vehicleNum == vehicle.vehicleNum }

        parkedSpaceList.isNotEmpty().let {
            val parkedSpace = parkedSpaceList[0]
            repository.deleteSpace(parkingSpace = parkedSpace)
            val floor = parkingLot.floors.filter { floor -> floor.name == parkedSpace.floorName }
            val space =
                floor[0].parkingSpaces.filter { parkingSpace -> parkingSpace.name == parkedSpace.name }
            space[0].apply {
                free = true
                vehicleNum = null
            }
        }
        return parkingLot
    }
}