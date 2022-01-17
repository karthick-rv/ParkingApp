package com.example.parkingapp.feature_parking.domain.util

object ParkingSpaceUtil {

    fun getCharForNumber(num: Int): Char {
        return if (num in 1..26) ((num + 64).toChar()) else '-'
    }

    fun getNumberForAlphabet(alphabet: Char): Int{
        val alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return alphabets.indexOf(alphabet) + 1
    }

}