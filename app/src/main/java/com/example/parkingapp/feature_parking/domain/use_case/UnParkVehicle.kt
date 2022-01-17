package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.LoadingInfo
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.DateUtil
import com.example.parkingapp.feature_parking.domain.util.ParkingSpaceUtil
import java.lang.Exception

class UnParkVehicle(
    val parkingSpaceRepository: ParkingSpaceRepository,
    private val parkingTicketRepository: ParkingTicketRepository
) {

    suspend operator fun invoke(vehicle: Vehicle): ParkingTicket {

        val parkingTickets = parkingTicketRepository.getAllTickets()

        //TODO - parkingTicketNum validation
        val parkingTicketList =
            parkingTickets.filter { parkingTicket -> parkingTicket.ticketId == vehicle.parkingTicketNum?.toLong() && parkingTicket.amountPaid == null }

        if(parkingTicketList.isNotEmpty()){
            val parkingTicket = parkingTicketList[0]
            val currentDateTime = DateUtil.getCurrentDateTime()

            return parkingTicket.copy(
                pickUpTime = currentDateTime,
                duration = DateUtil.getDuration(parkingTicket.parkedTime, currentDateTime)
                    .toString()
            )
        }else{
            throw Exception() //TODO(Handle error)
        }
    }
}