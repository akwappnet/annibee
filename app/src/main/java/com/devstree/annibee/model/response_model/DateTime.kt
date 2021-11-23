package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DateTime() : Parcelable {

    @SerializedName("event_date")
    var eventDate: String? = null

    @SerializedName("event_time")
    var eventTime: String? = null

    constructor(parcel: Parcel) : this() {
        eventDate = parcel.readString()
        eventTime = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventDate)
        parcel.writeString(eventTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateTime> {
        override fun createFromParcel(parcel: Parcel): DateTime {
            return DateTime(parcel)
        }

        override fun newArray(size: Int): Array<DateTime?> {
            return arrayOfNulls(size)
        }
    }


}