package com.example.parkingapp.di

import android.app.Application
import androidx.room.Room
import com.example.parkingapp.feature_parking.data.data_source.ParkingSpaceDatabase
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.domain.repository.ParkingSpaceRepositoryImpl
import com.example.parkingapp.feature_parking.domain.use_case.CreateParkingLot
import com.example.parkingapp.feature_parking.domain.use_case.GetAllotmentStatus
import com.example.parkingapp.feature_parking.domain.use_case.ParkVehicle
import com.example.parkingapp.feature_parking.domain.use_case.ParkingUseCases
import com.example.parkingapp.feature_parking.presentation.system_create.SystemConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParkingLotModule {

    @Provides
    @Singleton
    fun provideParkingSlotDatabase(app: Application): ParkingSpaceDatabase {
        return Room.databaseBuilder(
            app,
            ParkingSpaceDatabase::class.java,
            ParkingSpaceDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideParkingLotRepository(parkingSpaceDatabase: ParkingSpaceDatabase): ParkingSpaceRepository {
        return ParkingSpaceRepositoryImpl(parkingSpaceDatabase.parkingSpaceDao)
    }

    @Provides
    @Singleton
    fun provideParkingUseCases(repository: ParkingSpaceRepository): ParkingUseCases {
        return ParkingUseCases(
            createParkingLot = CreateParkingLot(),
            getAllotmentStatus = GetAllotmentStatus(repository),
            parkVehicle = ParkVehicle(repository)
        )
    }


    @Singleton
    @Provides
    fun provideSystemConfigManager(app: Application): SystemConfigManager {
        return SystemConfigManager(app)
    }

}