package com.example.parkingapp.di

import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.repository.ParkingTicketRepositoryImpl
import com.example.parkingapp.feature_fee_collection.domain.use_case.CalculateFees
import com.example.parkingapp.feature_fee_collection.domain.use_case.FeeCollectionUseCases
import com.example.parkingapp.feature_fee_collection.domain.use_case.GetTicketDetails
import com.example.parkingapp.feature_fee_collection.domain.use_case.PayFees
import com.example.parkingapp.feature_parking.data.data_source.ParkingLotDatabase
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeeCollectionModule {

    @Provides
    @Singleton
    fun provideParkingTicketRepository(parkingLotDatabase: ParkingLotDatabase): ParkingTicketRepository {
        return ParkingTicketRepositoryImpl(parkingLotDatabase.parkingTicketDao)
    }

    @Provides
    @Singleton
    fun provideFeeCollectionCases(parkingTicketRepository: ParkingTicketRepository,
                                  parkingSpaceRepository: ParkingSpaceRepository): FeeCollectionUseCases {
        return FeeCollectionUseCases(
            getTicketDetails = GetTicketDetails(parkingTicketRepository),
            calculateFees = CalculateFees(),
            payFees = PayFees(parkingTicketRepository, parkingSpaceRepository)
        )
    }
}