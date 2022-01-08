package com.example.parkingapp.feature_fee_collection.domain.repository

import com.example.parkingapp.feature_fee_collection.data.data_source.ParkingTicketDao
import com.example.parkingapp.feature_fee_collection.data.repository.ParkingTicketRepository
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket

class ParkingTicketRepositoryImpl(private val parkingTicketDao: ParkingTicketDao) : ParkingTicketRepository {
    override suspend fun insert(parkingTicket: ParkingTicket) {
        parkingTicketDao.insert(parkingTicket)
    }

    override suspend fun delete(parkingTicket: ParkingTicket) {
        parkingTicketDao.delete(parkingTicket)
    }

    override suspend fun getAll(): List<ParkingTicket> {
        return parkingTicketDao.getAll()
    }

    override suspend fun get(parkingSpaceName: Int): ParkingTicket {
        return parkingTicketDao.get(parkingSpaceName)
    }
}