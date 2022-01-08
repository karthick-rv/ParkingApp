package com.example.parkingapp.feature_parking.presentation.system_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class SystemCreateViewModel @Inject constructor(val systemConfigManager: SystemConfigManager) : ViewModel() {

    private val _parkingLotConfigFlow: MutableSharedFlow<Resource<ParkingLotConfig>> = MutableSharedFlow()
    val parkingLotConfigFlow: SharedFlow<Resource<ParkingLotConfig>> = _parkingLotConfigFlow

    fun createParkingSystem(floorCount: String, parkingSpaceCountPerFloor: String){

        if (floorCount.isBlank() || parkingSpaceCountPerFloor.isBlank()) {
            emitError("Fields Should not be Empty")
        }

        try{
            val floorCountInt = floorCount.toInt()
            val parkingSpaceCountInt = parkingSpaceCountPerFloor.toInt()

            if (floorCountInt > 0 && parkingSpaceCountInt > 0) {
                if (floorCountInt > 25) {
                    emitError("Floor Size is limited to 25 for the building")
                }
                viewModelScope.launch {
                    _parkingLotConfigFlow.emit(Resource.Loading())
                    val parkingLotConfig = ParkingLotConfig("",floorCountInt, parkingSpaceCountInt)
                    systemConfigManager.storeSystemConfig(parkingLotConfig)
                    _parkingLotConfigFlow.emit(Resource.Success(parkingLotConfig))
                }
            }else{
                emitError("Values should be greater than 0")
            }
        }catch (ex: NumberFormatException){
            emitError("Input should be valid")
        }
    }

    private fun emitError(errorMsg: String){
        viewModelScope.launch {
            _parkingLotConfigFlow.emit(Resource.Error(errorMsg))
        }
    }

}