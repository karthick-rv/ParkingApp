package com.example.parkingapp.feature_reservation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingSpaceUnavailableException
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.use_case.ParkingLotManager
import com.example.parkingapp.feature_parking.domain.util.VehicleUtil
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket
import com.example.parkingapp.feature_reservation.domain.use_case.ReservationUseCases
import com.example.parkingapp.feature_reservation.presentation.reservation.ReservationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(private val reservationUseCases: ReservationUseCases) : ViewModel() {

    private lateinit var vehicle: Vehicle
    private lateinit var date: String
    private lateinit var parkingLotManager: ParkingLotManager

    private val _reservationFeesFlow: MutableSharedFlow<Resource<Float>> = MutableSharedFlow()
    val reservationFeesFlow: SharedFlow<Resource<Float>> = _reservationFeesFlow

    private val _reservationTicket: MutableSharedFlow<Resource<ReservationTicket>> = MutableSharedFlow()
    val reservationTicket: SharedFlow<Resource<ReservationTicket>> = _reservationTicket

    private val _unReserveResult: MutableSharedFlow<Resource<Boolean>> = MutableSharedFlow()
    val unReserveResult: SharedFlow<Resource<Boolean>> = _unReserveResult

    fun configure(parkingLotManager: ParkingLotManager){
        this.parkingLotManager = parkingLotManager
    }

    fun onEvent(reservationEvent: ReservationEvent){
        when(reservationEvent){
            is ReservationEvent.ReserveParkingSpace -> {
                reserveParkingSpace()
            }
            is ReservationEvent.CalculateFees -> {
                vehicle = reservationEvent.vehicle
                date = reservationEvent.reservationDate
                calculateReservationFees(vehicle, date)
            }
            is ReservationEvent.UnReserveParkingSpace -> {
                unReserveParkingSpace(reservationEvent)
            }
        }
    }

    private fun calculateReservationFees(vehicle: Vehicle, date: String) {

        if(vehicle.vehicleNum.isBlank() || vehicle.model.isBlank() || date == "Pick date to reserve"){
            viewModelScope.launch {
                _reservationFeesFlow.emit(Resource.Error("Fields can't be empty"))
            }
        }else if(!VehicleUtil.isValidVehicleNumberFormat(vehicle.vehicleNum)){
            viewModelScope.launch {
                _reservationFeesFlow.emit(Resource.Error("Vehicle Number doesn't match the format"))
            }
        }
        else{
            viewModelScope.launch {
                val fees = reservationUseCases.calculateReservationFees(vehicle)
                _reservationFeesFlow.emit(Resource.Success(fees))
            }
        }
    }

    private fun unReserveParkingSpace(reservationEvent: ReservationEvent.UnReserveParkingSpace) {
        val vehicle = reservationEvent.vehicle
        if(vehicle.vehicleNum.isBlank() || vehicle.reservationTicketNum.isNullOrBlank()){
            viewModelScope.launch {
                _unReserveResult.emit(Resource.Error("Fields can't be empty"))
            }
        }else if(!VehicleUtil.isValidVehicleNumberFormat(vehicle.vehicleNum)){
            viewModelScope.launch {
                _unReserveResult.emit(Resource.Error("Vehicle Number doesn't match the format"))
            }
        }
        else{
            try {
                val reservationTicketNum = vehicle.reservationTicketNum.toInt()

                if(reservationTicketNum < 0){
                    viewModelScope.launch {
                        _unReserveResult.emit(Resource.Error("Reservation Ticket Number is not valid"))
                    }
                }else{
                    viewModelScope.launch {
                        _unReserveResult.emit(Resource.Success(reservationUseCases.unReserveParkingSpace(reservationEvent.vehicle)))
                    }
                }
            }catch (ex: NumberFormatException) {
                viewModelScope.launch {
                    _unReserveResult.emit(Resource.Error("Input should be valid"))
                }
            }
        }
    }

    private fun reserveParkingSpace() {
        viewModelScope.launch {
            try {
                _reservationTicket.emit(
                    Resource.Success(
                        reservationUseCases.reserveParkingSpace(
                            vehicle,
                            date,
                            parkingLotManager
                        )
                    )
                )
            } catch (ex: ParkingSpaceUnavailableException) {
                _reservationTicket.emit(Resource.Error(ex.message.toString()))
            }
        }
    }
}