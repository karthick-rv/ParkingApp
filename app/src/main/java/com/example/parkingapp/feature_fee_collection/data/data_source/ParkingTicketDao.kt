package com.example.parkingapp.feature_fee_collection.data.data_source

import androidx.room.*
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket


@Dao
interface ParkingTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parkingTicket: ParkingTicket)

    @Delete
    suspend fun delete(parkingTicket: ParkingTicket)

    @Query("SELECT * FROM parkingticket")
    suspend fun getAll(): List<ParkingTicket>

    @Query("SELECT * FROM parkingticket WHERE parkingSpaceName =:parkingSpaceName")
    suspend fun get(parkingSpaceName: Int): ParkingTicket

}