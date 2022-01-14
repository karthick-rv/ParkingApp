package com.example.parkingapp.feature_reservation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingapp.feature_parking.domain.model.Vehicle
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

    private val _reservationFeesFlow: MutableSharedFlow<Float> = MutableSharedFlow()
    val reservationFeesFlow: SharedFlow<Float> = _reservationFeesFlow

    private val _reservationTicket: MutableSharedFlow<ReservationTicket> = MutableSharedFlow()
    val reservationTicket: SharedFlow<ReservationTicket> = _reservationTicket

    private val _unReserveResult: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val unReserveResult: SharedFlow<Boolean> = _unReserveResult

    fun onEvent(reservationEvent: ReservationEvent){
        when(reservationEvent){
            is ReservationEvent.ReserveParkingSpace -> {
                viewModelScope.launch {
                    _reservationTicket.emit(reservationUseCases.reserveParkingSpace(vehicle, date))
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