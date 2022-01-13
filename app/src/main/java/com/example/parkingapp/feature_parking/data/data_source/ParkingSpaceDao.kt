package com.example.parkingapp.feature_parking.data.data_source

import androidx.room.*
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingSpaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parkingSpace: ParkingSpace)

    @Delete
    suspend fun delete(parkingSpace: ParkingSpace)

    @Query("SELECT * FROM parkingspace")
    suspend fun getAll(): List<ParkingSpace>

    @Query("SELECT * FROM parkingspace WHERE name =:parkingSpaceName")
    suspend fun get(parkingSpaceName: String): ParkingSpace

}