package com.example.parkingapp.feature_parking.domain.repository

import com.example.parkingapp.feature_parking.data.data_source.ParkingSpaceDao
import com.example.parkingapp.feature_parking.data.repository.ParkingSpaceRepository
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ParkingSpaceRepositoryImpl @Inject constructor(private val parkingSpaceDao: ParkingSpaceDao):
    ParkingSpaceRepository {

    override suspend fun insertSpace(parkingSpace: ParkingSpace) {
        parkingSpaceDao.insert(parkingSpace)
    }

    override suspend fun deleteSpace(parkingSpace: ParkingSpace) {
        parkingSpaceDao.delete(parkingSpace)
    }

    override suspend fun getAllSpaces(): List<ParkingSpace> {
        return parkingSpaceDao.getAll()
    }

    override suspend fun getSpaceBy(parkingSpaceName: String): ParkingSpace {
        return parkingSpaceDao.get(parkingSpaceName)
    }

}