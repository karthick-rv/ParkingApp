package com.example.parkingapp.feature_parking.presentation.parking_lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.*
import com.example.parkingapp.feature_parking.domain.use_case.ParkingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingLotViewModel @Inject constructor(private val parkingUseCases: ParkingUseCases) :
    ViewModel() {

    private lateinit var emptyParkingLot: ParkingLot

    private val _parkingLotFlow: MutableSharedFlow<Resource<ParkingLot>> = MutableSharedFlow()
    val parkingLotFlow: SharedFlow<Resource<ParkingLot>> = _parkingLotFlow

    private val _parkingTicketFlow: MutableSharedFlow<Resource<ParkingTicket>> = MutableSharedFlow()
    val parkingTicketFlow: SharedFlow<Resource<ParkingTicket>> = _parkingTicketFlow

    private val _parkedSpacesFlow: MutableSharedFlow<List<ParkingSpace>> = MutableSharedFlow()
    val parkedSpacesFlow: SharedFlow<List<ParkingSpace>> = _parkedSpacesFlow

    private val _isParkingLotFullFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isParkingLotFullFlow: SharedFlow<Boolean> = _isParkingLotFullFlow


    fun configure(parkingLotConfig: ParkingLotConfig) {
        emptyParkingLot = parkingUseCases.createParkingLot(parkingLotConfig)
    }

    fun onEvent(parkingLotEvent: ParkingLotEvent) {
        when (parkingLotEvent) {
            is ParkingLotEvent.Park -> {
                if(parkingLotEvent.isReserved)
                    parkOnReservedSpace(parkingLotEvent.vehicle)
                else
                    parkVehicle(parkingLotEvent.vehicle)
            }
            is ParkingLotEvent.UnPark -> {
                if(parkingLotEvent.isReserved)
                    unParkFromReservedSpace(parkingLotEvent.vehicle)
                else
                    unParkVehicle(parkingLotEvent.vehicle)
            }
            is ParkingLotEvent.ShowParkingLot -> {
                viewModelScope.launch {
                    _parkingLotFlow.emit(
                        Resource.Success(
                            parkingUseCases.getAllotmentStatus(
                                emptyParkingLot
                            )
                        )
                    )
                }
            }
        }
    }

    fun getParkedSpaces() {
        viewModelScope.launch {
            _parkedSpacesFlow.emit(parkingUseCases.getParkedSpaces())
        }
    }

    fun getParkingLotOccupancyStatus() {
        viewModelScope.launch {
            _isParkingLotFullFlow.emit(parkingUseCases.getAllotmentStatus(parkingLot = emptyParkingLot).isFull)
        }
    }

    private fun parkVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            val parkingLot = parkingUseCases.getAllotmentStatus(emptyParkingLot)
            if (parkingLot.isFull) {
                _parkingTicketFlow.emit(Resource.Error("Parking Lot is Full. Try after some time"))
            } else {
                try {
                    _parkingTicketFlow.emit(
                        Resource.Success(
                            parkingUseCases.parkVehicle(
                                parkingLot,
                                vehicle = vehicle
                            )
                        )
                    )
                } catch (exception: ParkingSpaceUnavailableException) {
                    exception.message?.let { _parkingTicketFlow.emit(Resource.Error(exception.message)) }
                }
            }
        }
    }

    private fun unParkVehicle(vehicle: Vehicle){
        viewModelScope.launch {
            _parkingTicketFlow.emit(
                Resource.Success(
                    parkingUseCases.unParkVehicle(
                        vehicle = vehicle
                    )
                )
            )
        }
    }

    private fun parkOnReservedSpace(vehicle: Vehicle) {
        // TODO(Park and show dialog with Parking Space Number)
    }

    private fun unParkFromReservedSpace(vehicle: Vehicle) {
        // TODO(UnPark and show dialog with Parking Space Number)
    }

}