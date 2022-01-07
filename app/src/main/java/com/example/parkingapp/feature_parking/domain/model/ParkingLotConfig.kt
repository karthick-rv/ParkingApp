package com.example.parkingapp.feature_parking.domain.model

import android.os.Parcel
import android.os.Parcelable

data class ParkingLotConfig(
    val name: String?,
    val floorCount: Int,
    val parkingSpaceCount: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(floorCount)
        parcel.writeInt(parkingSpaceCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParkingLotConfig> {
        override fun createFromParcel(parcel: Parcel): ParkingLotConfig {
            return ParkingLotConfig(parcel)
        }

        override fun newArray(size: Int): Array<ParkingLotConfig?> {
            return arrayOfNulls(size)
        }
    }
}
