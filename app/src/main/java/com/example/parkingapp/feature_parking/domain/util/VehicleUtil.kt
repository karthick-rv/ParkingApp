package com.example.parkingapp.feature_parking.domain.util

import java.util.regex.Pattern

object VehicleUtil {

    fun isValidVehicleNumberFormat(vehicleNum: String): Boolean{
        val pattern = Pattern.compile("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}\$")
        return pattern.matcher(vehicleNum).matches()
    }

}