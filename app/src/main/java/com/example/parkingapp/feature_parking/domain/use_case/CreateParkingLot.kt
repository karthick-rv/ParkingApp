package com.example.parkingapp.feature_parking.domain.use_case

import com.example.parkingapp.feature_parking.domain.model.Floor
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import kotlin.math.round

class CreateParkingLot {

    operator fun invoke(parkingLotConfig: ParkingLotConfig): ParkingLot {
        val floorCount = parkingLotConfig.floorCount
        val parkingSpaceCount = parkingLotConfig.parkingSpaceCount
        val floorList = createFloors(floorCount, parkingSpaceCount)

        return ParkingLot(parkingLotConfig.name!!, floorList, false)
    }

    private fun createFloors(floorCount: Int, parkingSpaceCount: Int): MutableList<Floor> {
        val floorList = mutableListOf<Floor>()

        for (i in 1..floorCount) {
            val floorName = getCharForNumber(i)
            val parkingSpaces = floorName?.let { createParkingSpaces(parkingSpaceCount, it) }

            floorName?.let { name ->
                parkingSpaces?.let {
                    floorList.add(
                        i - 1, Floor(
                            name = name,
                            parkingSpaces = it,
                            isFull = false
                        )
                    )
                }
            }
        }
        return floorList
    }

    private fun createParkingSpaces(parkingSpaceCount: Int, floorName: Char): List<ParkingSpace> {
        val vehicleTypes = VehicleType.values()
        val parkingSpaces = mutableListOf<ParkingSpace>()
        var i = 0;
        var totalCount = 0

        vehicleTypes.forEach {
            var count = getAllocationCount(it.allocationPercentage, parkingSpaceCount)
            totalCount += count
            while (count > 0 && totalCount <= parkingSpaceCount) {
                parkingSpaces.add(
                    i, ParkingSpace(
                        it,
                        floorName.toString() + (i + 1), floorName, true, null
                    )
                )
                i++
                count--
            }
        }
        return parkingSpaces
    }

    private fun getAllocationCount(percentage: Float, parkingSpaceCount: Int): Int {
        return round(((parkingSpaceCount * percentage) / 100)).toInt()
    }

    private fun getCharForNumber(num: Int): Char? {
        return if (num in 1..26) ((num + 64).toChar()) else null
    }
}