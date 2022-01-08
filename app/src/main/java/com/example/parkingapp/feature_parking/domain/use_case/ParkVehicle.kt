package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.Floor
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingSpaceUnavailableException
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import kotlin.jvm.Throws

class ParkVehicle(private val repository: ParkingSpaceRepository) {

    @Throws(ParkingSpaceUnavailableException::class)
    suspend operator fun invoke(parkingLot: ParkingLot, vehicle: Vehicle): ParkingLot {

        val floorList =
            parkingLot.floors.filter { floor ->
                !floor.isFull && hasSpaceForVehicleType(
                    floor,
                    vehicle.type
                )
            }.sortedBy { floor -> floor.name }

        val floor = if (floorList.isNotEmpty()) floorList[0] else null

        val parkingSpace = floor?.parkingSpaces?.firstOrNull { parkingSpace -> parkingSpace.free
                && parkingSpace.type == vehicle.type}

        parkingSpace?.let {
            it.free = false
            it.vehicleNum = vehicle.vehicleNum
            repository.insertSpace(it)
        } ?: throw ParkingSpaceUnavailableException("Parking space not available for ${vehicle.type}")

        return parkingLot
    }

    private fun hasSpaceForVehicleType(floor: Floor, vehicleType: VehicleType): Boolean {
        val spaceList =
            floor.parkingSpaces.filter { parkingSpace -> parkingSpace.type == vehicleType && parkingSpace.free }
        return spaceList.isNotEmpty()
    }

}