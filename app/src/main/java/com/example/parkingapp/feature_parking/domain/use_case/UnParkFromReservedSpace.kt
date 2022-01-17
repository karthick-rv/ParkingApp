package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.LoadingInfo
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.ParkingSpaceUtil
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository

class UnParkFromReservedSpace(val parkingSpaceRepository: ParkingSpaceRepository,
                              val reservationTicketRepository: ReservationTicketRepository) {

    suspend operator fun invoke(vehicle: Vehicle, parkingLotManager: ParkingLotManager): Boolean {
        val reservationTicket = vehicle.reservationTicketNum?.toLong()?.let {
            reservationTicketRepository.get(
                it
            )
        }

        reservationTicket?.let {
            val floorChar = it.parkingSpaceName[0]
            val parkingNum = it.parkingSpaceName.substring(1)
            val floorIndex = ParkingSpaceUtil.getNumberForAlphabet(floorChar)

            val loadIndex = parkingNum.toInt() / ParkingLotManager.COUNT_PER_LOAD

            val loadingInfo = LoadingInfo(floorIndex, loadIndex)
            val parkingSpaces = GetAllotmentStatus(parkingSpaceRepository, reservationTicketRepository)(
                parkingLotManager,
                loadingInfo
            )

            val parkingSpaceList = parkingSpaces.filter { parkingSpace -> parkingSpace.isReserved &&
                    parkingSpace.vehicleNum == vehicle.vehicleNum
            }

            if(parkingSpaceList.isNotEmpty()){
                val parkingSpace = parkingSpaceList[0]
                if(!parkingSpace.free) {
                    parkingSpace.free = true
                    parkingSpaceRepository.insertSpace(parkingSpace)
                    return true
                }else{
                    return false
                }
            }
        }
        return false
    }
}