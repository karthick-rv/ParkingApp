package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.Floor
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingSpaceUnavailableException
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.DateUtil
import com.example.parkingapp.feature_parking.domain.util.VehicleType

class ParkVehicle(
    private val repository: ParkingSpaceRepository,
    private val parkingTicketRepository: ParkingTicketRepository
) {

    @Throws(ParkingSpaceUnavailableException::class)
    suspend operator fun invoke(parkingLot: ParkingLot, vehicle: Vehicle): ParkingTicket {

        val floorList =
            parkingLot.floors.filter { floor ->
                !floor.isFull && hasSpaceForVehicleType(
                    floor,
                    vehicle.type
                )
            }.sortedBy { floor -> floor.name }

        val floor = if (floorList.isNotEmpty()) floorList[0] else null

        val parkingSpace = floor?.parkingSpaces?.firstOrNull { parkingSpace ->
            parkingSpace.free
                    && parkingSpace.type == vehicle.type
        }

        parkingSpace?.let {
            val parkingTicket = ParkingTicket(
                ticketId = 0,
                vehicleNum = vehicle.vehicleNum,
                parkingSpaceName = parkingSpace.name,
                vehicleType = vehicle.type,
                parkedTime = DateUtil.getCurrentDateTime(),
                pickUpTime = null
            )
            it.free = false
            it.vehicleNum = vehicle.vehicleNum
            val ticketId = parkingTicketRepository.insert(parkingTicket)
            it.parkingTicketNum = ticketId
            repository.insertSpace(it)
            return parkingTicketRepository.get(ticketId)
        }
            ?: throw ParkingSpaceUnavailableException("Parking space not available for ${vehicle.type}")
    }

    private fun hasSpaceForVehicleType(floor: Floor, vehicleType: VehicleType): Boolean {
        val spaceList =
            floor.parkingSpaces.filter { parkingSpace -> parkingSpace.type == vehicleType && parkingSpace.free }
        return spaceList.isNotEmpty()
    }
}