package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.LoadingInfo
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository

class GetAvailableParkingSpace constructor(
    val parkingSpaceRepository: ParkingSpaceRepository,
    val reservationTicketRepository: ReservationTicketRepository
) {

    suspend operator fun invoke(
        parkingLotManager: ParkingLotManager,
        vehicle: Vehicle
    ): ParkingSpace? {

        for (floorNum in 1..parkingLotManager.getFloorCount()) {
            val parkingSpaceCount = parkingLotManager.getParkingSpaceCount(floorNum)

            val loadCount = parkingSpaceCount / ParkingLotManager.COUNT_PER_LOAD
            val loadCountRem = parkingSpaceCount % ParkingLotManager.COUNT_PER_LOAD
            var parkingSpaces: List<ParkingSpace>
            var index = 0

            while (index < loadCount) {
                parkingSpaces =
                    GetAllotmentStatus(parkingSpaceRepository, reservationTicketRepository).invoke(
                        parkingLotManager,
                        LoadingInfo(floorNum, index)
                    )
                val parkingSpace =
                    parkingSpaces.firstOrNull { parkingSpace -> parkingSpace.free && parkingSpace.type == vehicle.type && !parkingSpace.isReserved}
                parkingSpace?.let {
                    return it
                }
                index++
            }

            if (loadCountRem > 0) {
                parkingSpaces =
                    GetAllotmentStatus(parkingSpaceRepository, reservationTicketRepository).invoke(
                        parkingLotManager,
                        LoadingInfo(floorNum, index, loadCountRem)
                    )

                val parkingSpace =
                    parkingSpaces.firstOrNull { parkingSpace -> parkingSpace.free && parkingSpace.type == vehicle.type && !parkingSpace.isReserved }
                parkingSpace?.let {
                    return it
                }
            }
        }
        return null
    }

}