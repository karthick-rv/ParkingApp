package com.example.parkingapp.feature_parking.domain.util

import android.annotation.SuppressLint
import com.example.parkingapp.feature_fee_collection.domain.model.ParkedDuration
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateUtil {

    private const val datePattern = "dd/M/yyyy hh:mm:ss a"

    fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat(datePattern)
        return formatter.format(Date())
    }

    fun dateToMillis(date: String): Long {
        val sdf = SimpleDateFormat(datePattern.split(" ")[0])
        return sdf.parse(date).time
    }

    fun getDuration(parkedTime: String, pickUpTime: String): ParkedDuration{
        val formatter: DateFormat = SimpleDateFormat(datePattern)
        val parked = formatter.parse(parkedTime)!!
        val pickup = formatter.parse(pickUpTime)!!

        var diff = pickup.time.minus(parked.time)

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60

        val elapsedHours: Long = diff / hoursInMilli
        diff %= hoursInMilli

        val elapsedMinutes: Long = diff / minutesInMilli
        diff %= minutesInMilli

        val elapsedSeconds = diff/ secondsInMilli

        return ParkedDuration(elapsedHours, elapsedMinutes, elapsedSeconds)
    }

}