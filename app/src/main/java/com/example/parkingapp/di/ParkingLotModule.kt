package com.example.parkingapp.di

import android.app.Application
import androidx.room.Room
import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_parking.data.data_source.ParkingLotDatabase
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.repository.ParkingSpaceRepositoryImpl
import com.example.parkingapp.feature_parking.domain.use_case.*
import com.example.parkingapp.feature_parking.presentation.system_create.SystemConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParkingLotModule {

    @Provides
    @Singleton
    fun provideParkingSlotDatabase(app: Application): ParkingLotDatabase {
        return Room.databaseBuilder(
            app,
            ParkingLotDatabase::class.java,
            ParkingLotDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideParkingLotRepository(parkingLotDatabase: ParkingLotDatabase): ParkingSpaceRepository {
        return ParkingSpaceRepositoryImpl(parkingLotDatabase.parkingSpaceDao)
    }

    @Provides
    @Singleton
    fun provideParkingUseCases(repository: ParkingSpaceRepository, parkingTicketRepository: ParkingTicketRepository): ParkingUseCases {
        return ParkingUseCases(
            createParkingLot = CreateParkingLot(),
            getAllotmentStatus = GetAllotmentStatus(repository),
            parkVehicle = ParkVehicle(repository, parkingTicketRepository),
            unParkVehicle = UnParkVehicle(repository, parkingTicketRepository),
            getParkedSpaces = GetParkedSpaces(repository)
        )
    }


    @Singleton
    @Provides
    fun provideSystemConfigManager(app: Application): SystemConfigManager {
        return SystemConfigManager(app)
    }

}