package com.example.parkingapp.feature_parking.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.parkingapp.feature_fee_collection.data.data_source.ParkingTicketDao
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_fee_collection.domain.util.Converter
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace
import com.example.parkingapp.feature_reservation.data.data_source.ReservationTicketDao
import com.example.parkingapp.feature_reservation.domain.model.ReservationTicket

@Database(entities = [ParkingSpace::class, ParkingTicket::class, ReservationTicket::class], version = 2)
abstract class ParkingLotDatabase: RoomDatabase() {

     abstract val parkingSpaceDao: ParkingSpaceDao

     abstract val parkingTicketDao: ParkingTicketDao

     abstract val reservationTicketDao: ReservationTicketDao

     companion object{
          const val DATABASE_NAME = "parking_space_db"
     }
}