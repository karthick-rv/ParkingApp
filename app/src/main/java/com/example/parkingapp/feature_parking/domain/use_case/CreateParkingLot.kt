package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.domain.model.Floor
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.util.VehicleType

class CreateParkingLot {

    operator fun invoke(parkingLotConfig: ParkingLotConfig): ParkingLot {
        val floorCount = parkingLotConfig.floorCount
        val parkingSpaceCount = parkingLotConfig.parkingSpaceCount
        val floorList = mutableListOf<Floor>()

        for (i in 1..floorCount) {
            val floorName = getCharForNumber(i)
            val parkingSpaces = mutableListOf<ParkingSpace>()
            for (j in 1..parkingSpaceCount) {
                floorName?.let {
                    parkingSpaces.add(
                        index = j - 1,
                        element = ParkingSpace(
                            type = VehicleType.CAR,
                            name = it.toString() + j,
                            floorName = it,
                            free = true,
                            vehicleNum = null
                        )
                    )
                }
            }

            floorName?.let {
                floorList.add(i-1, Floor(
                    name = floorName,
                    parkingSpaces = parkingSpaces,
                    isFull = false
                ))
            }
        }
        return ParkingLot(parkingLotConfig.name!!, floorList)
    }


    private fun getCharForNumber(num: Int): Char? {
        return if (num in 1..26) ((num + 64).toChar()) else null
    }

}