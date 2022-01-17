package com.example.parkingapp.di

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_reservation.data.repository.ReservationTicketRepository
import com.example.parkingapp.feature_transactions.domain.use_case.GetTransactionData
import com.example.parkingapp.feature_transactions.domain.use_case.TransactionUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Provides
    @Singleton
    fun provideTransactionData(parkingTicketRepository: ParkingTicketRepository,reservationTicketRepository: ReservationTicketRepository ): TransactionUseCases {
        return TransactionUseCases(
            getTransactionData = GetTransactionData(parkingTicketRepository, reservationTicketRepository)
        )
    }

}