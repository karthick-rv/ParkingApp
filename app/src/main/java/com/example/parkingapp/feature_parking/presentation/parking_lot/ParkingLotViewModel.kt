package com.example.parkingapp.feature_parking.presentation.parking_lot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.*
import com.example.parkingapp.feature_parking.domain.use_case.ParkingLotManager
import com.example.parkingapp.feature_parking.domain.use_case.ParkingUseCases
import com.example.parkingapp.feature_parking.domain.util.VehicleUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ParkingLotViewModel @Inject constructor(private val parkingUseCases: ParkingUseCases) :
    ViewModel() {

    private lateinit var parkingLotManager: ParkingLotManager

    lateinit var currentLoadingInfo: LoadingInfo

    private val _parkingTicketFlow: MutableSharedFlow<Resource<ParkingTicket>> = MutableSharedFlow()
    val parkingTicketFlow: SharedFlow<Resource<ParkingTicket>> = _parkingTicketFlow

    private val _parkedSpacesFlow: MutableSharedFlow<Resource<List<ParkingSpace>>> =
        MutableSharedFlow()
    val parkedSpacesFlow: SharedFlow<Resource<List<ParkingSpace>>> = _parkedSpacesFlow

    private val _parkingLotManagerFlow: MutableStateFlow<ParkingLotManager> = MutableStateFlow(
        ParkingLotManager(ParkingLotConfig("", 0, 0))
    )
    val parkingLotManagerFlow: StateFlow<ParkingLotManager> = _parkingLotManagerFlow

    private val _parkOnReservedResultFlow: MutableSharedFlow<Resource<Boolean>> =
        MutableSharedFlow()
    val parkOnReservedResultFlow: SharedFlow<Resource<Boolean>> = _parkOnReservedResultFlow

    private val _unParkFromReservedResultFlow: MutableSharedFlow<Resource<Boolean>> = MutableSharedFlow()
    val unParkFromReservedResultFlow: SharedFlow<Resource<Boolean>> = _unParkFromReservedResultFlow


    fun configure(parkingLotConfig: ParkingLotConfig) {
        parkingLotManager = ParkingLotManager(parkingLotConfig)
        viewModelScope.launch {
            _parkingLotManagerFlow.emit(parkingLotManager)
        }
    }

    fun onEvent(parkingLotEvent: ParkingLotEvent) {
        when (parkingLotEvent) {
            is ParkingLotEvent.Park -> {
                if (parkingLotEvent.isReserved) {
                    parkOnReservedSpace(parkingLotEvent.vehicle)
                } else {
                    parkVehicle(parkingLotEvent.vehicle)
                }
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
                            parkingUseCases.getAllotmentStatus(
                                parkingLotManager,
                                parkingLotEvent.loadingInfo
                            )
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

    private fun parkVehicle(vehicle: Vehicle) {
        if (vehicle.vehicleNum.isBlank() || vehicle.model.isBlank()) {
            viewModelScope.launch {
                _parkingTicketFlow.emit(Resource.Error("Fields can't be empty"))
            }
        } else {
            if (VehicleUtil.isValidVehicleNumberFormat(vehicle.vehicleNum)) {
                viewModelScope.launch {
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
                    } catch (exception: VehicleAlreadyExistException) {
                        exception.message?.let { _parkingTicketFlow.emit(Resource.Error(exception.message)) }
                    }
                }
            } else {
                viewModelScope.launch {
                    _parkingTicketFlow.emit(Resource.Error("Vehicle Number doesn't match the format"))
                }
            }
        }
    }

    private fun unParkVehicle(vehicle: Vehicle) {

        if (vehicle.parkingTicketNum.isNullOrBlank() || vehicle.vehicleNum.isNullOrBlank()) {
            viewModelScope.launch {
                _parkingTicketFlow.emit(Resource.Error("Fields can't be empty"))
            }
        } else {
            try {
                val parkingTicketNum = vehicle.parkingTicketNum.toInt()

                if (parkingTicketNum <= 0) {
                    viewModelScope.launch {
                        _parkingTicketFlow.emit(Resource.Error("Parking Ticket Number is not valid"))
                    }
                }
                else if (VehicleUtil.isValidVehicleNumberFormat(vehicle.vehicleNum)) {
                    try {
                        viewModelScope.launch {
                            val result = parkingUseCases.unParkVehicle(
                                vehicle = vehicle
                            )
                            result?.let {
                                _parkingTicketFlow.emit(
                                    Resource.Success(it)
                                )
                            }?: _parkingTicketFlow.emit(
                                Resource.Error(
                                    "Input is not valid"
                                )
                            )

                        }
                    }catch (exception: VehicleNotAvailableException){
                        viewModelScope.launch {
                            exception.message?.let { _parkingTicketFlow.emit(Resource.Error(it)) }
                        }
                    }catch (exception: Exception){
                        viewModelScope.launch {
                            exception.message?.let { _parkingTicketFlow.emit(Resource.Error(
                                it
                            )) }
                        }
                    }
                }else{
                    viewModelScope.launch {
                        _parkingTicketFlow.emit(Resource.Error("Vehicle Number doesn't match the format"))
                    }
                }
            } catch (ex: NumberFormatException) {
                viewModelScope.launch {
                    _parkingTicketFlow.emit(Resource.Error("Input should be valid"))
                }
            }
        }
    }

    private fun parkOnReservedSpace(vehicle: Vehicle) {

        if (vehicle.vehicleNum.isBlank() || vehicle.reservationTicketNum!!.isBlank()) {
            viewModelScope.launch {
                _parkOnReservedResultFlow.emit(Resource.Error("Fields can't be empty"))
            }
            return
        }

        if (VehicleUtil.isValidVehicleNumberFormat(vehicle.vehicleNum)) {
            try {
                val reservationTicketNum = vehicle.reservationTicketNum.toInt()

                if (reservationTicketNum < 0) {
                    viewModelScope.launch {
                        _parkOnReservedResultFlow.emit(Resource.Error("Reservation Ticket Number is not valid"))
                    }
                } else {
                    viewModelScope.launch {
                        _parkOnReservedResultFlow.emit(
                            Resource.Success(
                                parkingUseCases.parkOnReservedSpace(
                                    vehicle,
                                    parkingLotManager
                                )
                            )
                        )
                    }
                }
            } catch (ex: NumberFormatException) {
                viewModelScope.launch {
                    _parkOnReservedResultFlow.emit(Resource.Error("Input should be valid"))
                }
            }
        } else {
            viewModelScope.launch {
                _parkOnReservedResultFlow.emit(Resource.Error("Vehicle Number doesn't match the format"))
            }
        }
    }

    private fun unParkFromReservedSpace(vehicle: Vehicle) {

        if (vehicle.reservationTicketNum.isNullOrBlank() || vehicle.vehicleNum.isBlank()) {
            viewModelScope.launch {
                _unParkFromReservedResultFlow.emit(Resource.Error("Fields can't be empty"))
            }
        } else {
            try {
                val parkingTicketNum = vehicle.reservationTicketNum.toInt()

                if (parkingTicketNum < 0) {
                    viewModelScope.launch {
                        _unParkFromReservedResultFlow.emit(Resource.Error("Parking Ticket Number is not valid"))
                    }
                } else if (VehicleUtil.isValidVehicleNumberFormat(vehicle.vehicleNum)) {
                    viewModelScope.launch {
                        _unParkFromReservedResultFlow.emit(
                            Resource.Success(
                                parkingUseCases.unParkFromReservedSpace(
                                    vehicle,
                                    parkingLotManager
                                )
                            )
                        )
                    }
                }else{
                    viewModelScope.launch {
                        _unParkFromReservedResultFlow.emit(Resource.Error("Vehicle Number doesn't match the format"))
                    }
                }
            } catch (ex: NumberFormatException) {
                viewModelScope.launch {
                    _parkOnReservedResultFlow.emit(Resource.Error("Input should be valid"))
                }
            }
        }
    }
}