package com.example.parkingapp.feature_fee_collection.domain.util

import androidx.room.TypeConverter
import java.util.*

object Converter {

    @TypeConverter
    fun toDate(timeStamp: Long): Date{
        return Date(timeStamp)
    }

    @TypeConverter
    fun toTimeStamp(date: Date): Long{
        return date.time
    }
}