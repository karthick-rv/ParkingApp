package com.example.parkingapp.di

import com.example.parkingapp.feature_parking.data.data_source.ParkingLotDatabase
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_reservation.domain.repository.ReservationTicketRepositoryImpl
import com.example.parkingapp.feature_reservation.domain.use_case.CalculateReservationFees
import com.example.parkingapp.feature_reservation.domain.use_case.ReservationUseCases
import com.example.parkingapp.feature_reservation.domain.use_case.ReserveParkingSpace
import com.example.parkingapp.feature_reservation.domain.use_case.UnReserveParkingSpace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReservationModule {

    @Provides
    @Singleton
    fun provideReservationTicketRepository(parkingLotDatabase: ParkingLotDatabase): ReservationTicketRepository{
        return ReservationTicketRepositoryImpl(parkingLotDatabase.reservationTicketDao)
    }

    @Provides
    @Singleton
    fun provideReservationUseCases(reservationTicketRepository: ReservationTicketRepository): ReservationUseCases{
        return ReservationUseCases(
            reserveParkingSpace = ReserveParkingSpace(reservationTicketRepository),
            calculateReservationFees = CalculateReservationFees(),
            unReserveParkingSpace = UnReserveParkingSpace(reservationTicketRepository)
        )
    }

}