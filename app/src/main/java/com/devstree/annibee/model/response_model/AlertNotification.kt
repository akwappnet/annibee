package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AlertNotification() : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("days")
    var days: Long = 0

    @SerializedName("created_at")
    var createdAt: String? = null

    var isChecked: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        days = parcel.readLong()
        createdAt = parcel.readString()
        isChecked = parcel.readByte() != 0.toByte()
    }

    constructor(id: Long, name:String, days:Long, isChecked:Boolean = false) : this() {
        this.id = id
        this.name = name
        this.days = days
        this.isChecked = isChecked
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeLong(days)
        parcel.writeString(createdAt)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlertNotification> {
        override fun createFromParcel(parcel: Parcel): AlertNotification {
            return AlertNotification(parcel)
        }

        override fun newArray(size: Int): Array<AlertNotification?> {
            return arrayOfNulls(size)
        }
    }
}