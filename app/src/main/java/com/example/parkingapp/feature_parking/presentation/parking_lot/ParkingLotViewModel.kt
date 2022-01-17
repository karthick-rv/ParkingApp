package com.example.parkingapp.feature_parking.presentation.parking_lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.*
import com.example.parkingapp.feature_parking.domain.use_case.GetAllotmentStatus
import com.example.parkingapp.feature_parking.domain.use_case.ParkingLotManager
import com.example.parkingapp.feature_parking.domain.use_case.ParkingUseCases
import com.example.parkingapp.feature_parking.domain.util.ParkingSpaceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkingLotViewModel @Inject constructor(private val parkingUseCases: ParkingUseCases) :
    ViewModel() {

    private lateinit var parkingLotManager: ParkingLotManager

    lateinit var currentLoadingInfo: LoadingInfo

    private val _parkingTicketFlow: MutableSharedFlow<Resource<ParkingTicket>> = MutableSharedFlow()
    val parkingTicketFlow: SharedFlow<Resource<ParkingTicket>> = _parkingTicketFlow

    private val _parkedSpacesFlow: MutableSharedFlow<Resource<List<ParkingSpace>>> = MutableSharedFlow()
    val parkedSpacesFlow: SharedFlow<Resource<List<ParkingSpace>>> = _parkedSpacesFlow

    private val _isParkingLotFullFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isParkingLotFullFlow: SharedFlow<Boolean> = _isParkingLotFullFlow

    private val _parkingLotManagerFlow: MutableStateFlow<ParkingLotManager> = MutableStateFlow(
        ParkingLotManager(ParkingLotConfig("", 0, 0))
    )
    val parkingLotManagerFlow: StateFlow<ParkingLotManager> = _parkingLotManagerFlow

    private val _parkOnReservedResultFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val parkOnReservedResultFlow: SharedFlow<Boolean> = _parkOnReservedResultFlow

    private val _unParkFromReservedResultFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val unParkFromReservedResultFlow: SharedFlow<Boolean> = _unParkFromReservedResultFlow



    fun configure(parkingLotConfig: ParkingLotConfig) {
        parkingLotManager = ParkingLotManager(parkingLotConfig)
        viewModelScope.launch {
            _parkingLotManagerFlow.emit(parkingLotManager)
        }
    }

    fun onEvent(parkingLotEvent: ParkingLotEvent) {
        when (parkingLotEvent) {
            is ParkingLotEvent.Park -> {
                if (parkingLotEvent.isReserved)
                    parkOnReservedSpace(parkingLotEvent.vehicle)
                else
                    parkVehicle(parkingLotEvent.vehicle)
            }
            is ParkingLotEvent.UnPark -> {
                if (parkingLotEvent.isReserved)
                    unParkFromReservedSpace(parkingLotEvent.vehicle)
                else
                    unParkVehicle(parkingLotEvent.vehicle)
            }
            is ParkingLotEvent.ShowParkingLot -> {
                currentLoadingInfo = parkingLotEvent.loadingInfo
                viewModelScope.launch {
                    _parkedSpacesFlow.emit(
                        Resource.Success(
                            parkingUseCases.getAllotmentStatus(parkingLotManager, parkingLotEvent.loadingInfo)
                        )
                    )
                }
            }
        }
    }

    fun getParkingSpaceCount(floorIndex: Int): Int {
        return parkingLotManager.getParkingSpaceCount(floorIndex)
    }

    fun getFloorCount(): Int {
        return parkingLotManager.getFloorCount()
    }

    fun getParkedSpaces() {
        viewModelScope.launch {
            _parkedSpacesFlow.emit(Resource.Success(parkingUseCases.getParkedSpaces()))
        }
    }

    fun getParkingLotOccupancyStatus() {
        viewModelScope.launch {
//            _isParkingLotFullFlow.emit(parkingUseCases.getAllotmentStatus(parkingLot = emptyParkingLot).isFull)
        }
    }

    private fun parkVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
//            if (parkingLot.isFull) {
//                _parkingTicketFlow.emit(Resource.Error("Parking Lot is Full. Try after some time"))
//            }
                try {
                    _parkingTicketFlow.emit(
                        Resource.Success(
                            parkingUseCases.parkVehicle(
                                vehicle = vehicle,
                                parkingLotManager = parkingLotManager
                            )
                        )
                    )
                } catch (exception: ParkingSpaceUnavailableException) {
                    exception.message?.let { _parkingTicketFlow.emit(Resource.Error(exception.message)) }
                }
        }
    }

    private fun unParkVehicle(vehicle: Vehicle) {
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
        // reservation Ticket Num Validation
        viewModelScope.launch {
           _parkOnReservedResultFlow.emit(parkingUseCases.parkOnReservedSpace(vehicle, parkingLotManager))
        }
    }

    private fun unParkFromReservedSpace(vehicle: Vehicle) {
        viewModelScope.launch {
            _unParkFromReservedResultFlow.emit(parkingUseCases.unParkFromReservedSpace(vehicle, parkingLotManager))
        }
    }
}