package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class FCMToken() : Parcelable{

    @SerializedName("device_type")
    var deviceType: String? = null

    @SerializedName("device_id")
    var deviceId: String? = null

    @SerializedName("user_id")
    var userId: Long = 0

    constructor(parcel: Parcel) : this() {
        deviceType = parcel.readString()
        deviceId = parcel.readString()
        userId = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deviceType)
        parcel.writeString(deviceId)
        parcel.writeLong(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FCMToken> {
        override fun createFromParcel(parcel: Parcel): FCMToken {
            return FCMToken(parcel)
        }

        override fun newArray(size: Int): Array<FCMToken?> {
            return arrayOfNulls(size)
        }
    }
}