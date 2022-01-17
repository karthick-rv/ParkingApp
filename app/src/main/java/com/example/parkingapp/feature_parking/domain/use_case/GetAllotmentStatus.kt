package com.example.parkingapp.feature_parking.domain.use_case

import android.text.format.DateUtils
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.LoadingInfo
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.util.DateUtil
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket
import java.util.*


class GetAllotmentStatus(
    private val repository: ParkingSpaceRepository,
    private val reservationTicketRepository: ReservationTicketRepository
) {

    suspend operator fun invoke(
        parkingLotManager: ParkingLotManager,
        loadingInfo: LoadingInfo
    ): List<ParkingSpace> {

        val parkingSpaces = parkingLotManager.getParkingSpaces(
            loadingInfo.floorIndex,
            loadingInfo.loadIndex,
            loadingInfo.loadRem
        )

        val parkedSpaces = repository.getAllSpaces()
        val reservationTickets = reservationTicketRepository.getAllTickets()

            parkingSpaces.map { parkingSpace ->
                val parkedSpace = parkedSpaces.find { space -> space.name == parkingSpace.name }

                val reservedSpace = reservationTickets.find {
                        ticket -> ticket.parkingSpaceName == parkingSpace.name
                        &&  DateUtils.isToday(DateUtil.dateToMillis(ticket.date))
                }

                reservedSpace?.let {
                    parkingSpace.apply {
                        reservationTicketNum = it.ticketId.toFloat()
                        vehicleNum = it.vehicleNum
                        isReserved = true
                    }
                }

                parkedSpace?.let {
                    parkingSpace.apply {
                        free = it.free
                        parkingTicketNum = it.parkingTicketNum
                        vehicleNum = it.vehicleNum
                    }
                }
            }
        return parkingSpaces
    }


    private fun updateFloorStatus(parkingLot: ParkingLot) {
        parkingLot.floors.forEach {
            val freeSpaces = it.parkingSpaces.filter { parkingSpace -> parkingSpace.free }
            if (freeSpaces.isEmpty()) it.isFull = true
        }
    }

    private fun updateParkingLotStatus(parkingLot: ParkingLot) {
        val floorWithSpaces = parkingLot.floors.filter { floor -> !floor.isFull }
        if (floorWithSpaces.isEmpty()) parkingLot.isFull = true
    }
}