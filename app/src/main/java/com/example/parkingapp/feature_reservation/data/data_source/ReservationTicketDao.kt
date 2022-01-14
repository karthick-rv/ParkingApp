package com.example.parkingapp.feature_reservation.data.data_source

import androidx.room.*
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket

@Dao
interface ReservationTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservationTicket: ReservationTicket): Long

    @Delete
    suspend fun delete(reservationTicket: ReservationTicket)

    @Query("SELECT * FROM reservationticket")
    suspend fun getAll(): List<ReservationTicket>

    @Query("SELECT * FROM reservationticket WHERE ticketId =:reservationTicketId")
    suspend fun get(reservationTicketId: Long): ReservationTicket

    @Query("SELECT * FROM reservationticket WHERE vehicleType =:vehicleType")
    suspend fun getByVehicleType(vehicleType: String): List<ReservationTicket>
}