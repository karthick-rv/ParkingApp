package com.example.parkingapp.feature_reservation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.ParkingSpaceUnavailableException
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.use_case.ParkingLotManager
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

    private val _reservationFeesFlow: MutableSharedFlow<Float> = MutableSharedFlow()
    val reservationFeesFlow: SharedFlow<Float> = _reservationFeesFlow

    private val _reservationTicket: MutableSharedFlow<Resource<ReservationTicket>> = MutableSharedFlow()
    val reservationTicket: SharedFlow<Resource<ReservationTicket>> = _reservationTicket

    private val _unReserveResult: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val unReserveResult: SharedFlow<Boolean> = _unReserveResult

    fun configure(parkingLotManager: ParkingLotManager){
        this.parkingLotManager = parkingLotManager
    }

    fun onEvent(reservationEvent: ReservationEvent){
        when(reservationEvent){
            is ReservationEvent.ReserveParkingSpace -> {
                viewModelScope.launch {
                    try {
                        _reservationTicket.emit(Resource.Success(reservationUseCases.reserveParkingSpace(vehicle, date, parkingLotManager)))
                    }catch (ex: ParkingSpaceUnavailableException){
                        _reservationTicket.emit(Resource.Error(ex.message.toString()))
                    }
                }
            }
            is ReservationEvent.CalculateFees -> {
                vehicle = reservationEvent.vehicle
                date = reservationEvent.reservationDate
                viewModelScope.launch {
                    val fees = reservationUseCases.calculateReservationFees(vehicle)
                    _reservationFeesFlow.emit(fees)
                }
            }
            is ReservationEvent.UnReserveParkingSpace -> {
                viewModelScope.launch {
                    _unReserveResult.emit(reservationUseCases.unReserveParkingSpace(reservationEvent.vehicle))
                }
            }
        }
    }
}