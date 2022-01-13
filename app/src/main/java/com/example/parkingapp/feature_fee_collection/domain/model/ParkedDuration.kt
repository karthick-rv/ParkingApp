package com.example.parkingapp.feature_fee_collection.domain.model

data class ParkedDuration(
    val hours: Long,
    val minutes: Long,
    val seconds: Long
){

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        if(hours > 0) stringBuilder.append(hours).append(" hrs ")
        if(minutes> 0) stringBuilder.append(minutes).append(" mins ")
        stringBuilder.append(seconds).append(" secs")
        return stringBuilder.toString()
    }
}


