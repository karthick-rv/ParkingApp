package com.example.parkingapp.feature_parking.domain.use_case

import android.util.Log
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_parking.domain.util.ParkingSpaceUtil.getCharForNumber
import com.example.parkingapp.feature_parking.domain.util.VehicleType

class ParkingLotManager(private val parkingLotConfig: ParkingLotConfig) {

    private val parkingSpacesCount = parkingLotConfig.parkingSpaceCount
    private val floorCount = parkingLotConfig.floorCount

    private var loadCount = COUNT_PER_LOAD

    companion object{
        const val COUNT_PER_LOAD = 200
    }

    fun getParkingSpaces(floorIndex: Int = 1, index: Int = 0, rem:Int? = null): List<ParkingSpace> {

        val resultList = mutableListOf<ParkingSpace>()

        val floorName = getCharForNumber(floorIndex)

        if(parkingSpacesCount < loadCount) loadCount = parkingSpacesCount

        val start = index * loadCount
        val end = rem?.let { start + rem  } ?: start + loadCount - 1

        val map = vehicleTypeAllotmentMap()

        var countFilled = 0
        if(countFilled < loadCount) {
            map.forEach {

                if(countFilled >= loadCount) return@forEach

                val vehicleType = it.key
                val value = it.value

                val startIndexOfVehicleType = value[0]
                val endIndexOfVehicleType = value[1]


                if (start in startIndexOfVehicleType..endIndexOfVehicleType && end in startIndexOfVehicleType..endIndexOfVehicleType) {
                    Log.d(
                        "If $vehicleType",
                        " [ $startIndexOfVehicleType - $endIndexOfVehicleType  ] "
                    )

                    for (i in start..end) {
                        val parkingSpace = ParkingSpace(
                                    VehicleType.valueOf(vehicleType),
                                    floorName.toString() + (i+1), floorName, true, null
                                )
                        resultList.add(parkingSpace)
                        countFilled++
                    }

                    Log.d("Count Filled - ", countFilled.toString())
                } else if (start in startIndexOfVehicleType..endIndexOfVehicleType) {
                    Log.d(
                        "Else If $vehicleType",
                        " [ $startIndexOfVehicleType - $endIndexOfVehicleType  ] "
                    )

                    for (i in start..endIndexOfVehicleType){
                        val parkingSpace = ParkingSpace(
                            VehicleType.valueOf(vehicleType),
                            floorName.toString() + (i+1), floorName, true, null
                        )
                        resultList.add(parkingSpace)
                        countFilled++
                    }

                    Log.d("Count Filled - ", countFilled.toString())
                } else if (end in startIndexOfVehicleType..endIndexOfVehicleType) {
                    Log.d(
                        "Else $vehicleType",
                        " [ $startIndexOfVehicleType - $endIndexOfVehicleType  ] "
                    )

                    for (i in startIndexOfVehicleType..end) {
                        val parkingSpace = ParkingSpace(
                            VehicleType.valueOf(vehicleType),
                            floorName.toString() + (i+1), floorName, true, null
                        )
                        resultList.add(parkingSpace)
                        countFilled++
                    }

                    Log.d("Count Filled - ", countFilled.toString())
                }
                else if(startIndexOfVehicleType > start && endIndexOfVehicleType < end){
                    Log.d(
                        "Nothing Else $vehicleType",
                        " [ $startIndexOfVehicleType - $endIndexOfVehicleType  ] "
                    )

                    for (i in startIndexOfVehicleType..endIndexOfVehicleType) {
                        val parkingSpace = ParkingSpace(
                            VehicleType.valueOf(vehicleType),
                            floorName.toString() + (i+1), floorName, true, null
                        )
                        resultList.add(parkingSpace)
                        countFilled++
                    }

                    Log.d("Count Filled - ", countFilled.toString())
                }
            }
        }
        return resultList.toList()
    }



    fun getParkingSpaceCount(floorIndex: Int): Int {
        return parkingSpacesCount
    }

    fun getFloorCount(): Int {
        return floorCount
    }

    private fun vehicleTypeAllotmentMap(): Map<String, Array<Int>> {
        val vehicleTypes = VehicleType.values()
        val map = mutableMapOf<String, Array<Int>>()

        var startNum = 0
        vehicleTypes.forEach {
            val name = it.name
            val percentage = it.allocationPercentage
            val allocationCount = (parkingSpacesCount * percentage.toInt()) / 100

            map[name] = arrayOf(startNum, startNum + allocationCount - 1)
            startNum += allocationCount

            Log.d(
                "$it",
                " [ ${map[name]?.get(0)} - ${map[name]?.get(1)}] "
            )
        }
        return map.toMap()
    }

}