package com.example.parkingapp.feature_parking.presentation.system_create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class SystemCreateViewModel @Inject constructor() : ViewModel() {

    fun createParkingSystem(floorCount: String, parkingSpaceCountPerFloor: String): SystemCreateResponse {

        if (floorCount.isBlank() || parkingSpaceCountPerFloor.isBlank()) {
            return SystemCreateResponse.Error("Fields Should not be Empty")
        }

        try{
            val floorCountInt = floorCount.toInt()
            val parkingSpaceCountInt = parkingSpaceCountPerFloor.toInt()

            if (floorCountInt > 0 && parkingSpaceCountInt > 0) {
                if (floorCountInt > 25) {
                    return SystemCreateResponse.Error("Floor Size is limited to 25 for the building")
                }
                return SystemCreateResponse.Success(floorCountInt, parkingSpaceCountInt)
            }else{
                return SystemCreateResponse.Error("Values should be greater than 0")
            }
        }catch (ex: NumberFormatException){
            return SystemCreateResponse.Error("Input should be valid")
        }
    }

}