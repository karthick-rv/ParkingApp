package com.example.parkingapp.feature_parking.presentation.parking_lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_parking.domain.model.ParkingLot
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.domain.use_case.ParkingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingLotViewModel @Inject constructor(private val parkingUseCases: ParkingUseCases): ViewModel() {

    lateinit var parkingLot: ParkingLot

    private val _parkingLotFlow: MutableSharedFlow<ParkingLot> = MutableSharedFlow()

    val parkingLotFlow: SharedFlow<ParkingLot> = _parkingLotFlow

    fun configure(parkingLotConfig: ParkingLotConfig){
           parkingLot = parkingUseCases.createParkingLot(parkingLotConfig)
    }

    fun onEvent(parkingLotEvent: ParkingLotEvent){
        when(parkingLotEvent){
            is ParkingLotEvent.Park -> {
                viewModelScope.launch {
                    val parkingLot = parkingUseCases.getAllotmentStatus(parkingLot)
                    _parkingLotFlow.emit(parkingUseCases.parkVehicle(parkingLot, vehicle = parkingLotEvent.vehicle))
                }
            }
            is ParkingLotEvent.UnPark -> {

            }
            is ParkingLotEvent.ShowLotStatus -> {
                viewModelScope.launch {
                    _parkingLotFlow.emit(parkingUseCases.getAllotmentStatus(parkingLot))
                }
            }
        }
    }



}