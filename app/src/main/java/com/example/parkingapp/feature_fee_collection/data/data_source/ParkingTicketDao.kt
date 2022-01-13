package com.example.parkingapp.feature_fee_collection.data.data_source

import androidx.room.*
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.domain.util.VehicleType


@Dao
interface ParkingTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parkingTicket: ParkingTicket): Long

    @Delete
    suspend fun delete(parkingTicket: ParkingTicket)

    @Query("SELECT * FROM parkingticket")
    suspend fun getAll(): List<ParkingTicket>

    @Query("SELECT * FROM parkingticket WHERE ticketId =:parkingTicketId")
    suspend fun get(parkingTicketId: Long): ParkingTicket

    @Query("SELECT * FROM parkingticket WHERE vehicleType =:vehicleType")
    suspend fun getByVehicleType(vehicleType: String): List<ParkingTicket>
}