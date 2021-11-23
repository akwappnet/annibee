package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Attribute() : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("ja_name")
    var jaName: String? = null
    @SerializedName("en_name", alternate = ["name"])
    var enName: String? = null

    @SerializedName("ch1_name")
    var ch1Name: String? = null
    @SerializedName("ch2_name")
    var ch2Name: String? = null

    @SerializedName("user_id")
    var userId: Long = 0

    @SerializedName("parent_name")
    var parentName: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        jaName = parcel.readString()
        enName = parcel.readString()
        ch1Name = parcel.readString()
        ch2Name = parcel.readString()
        userId = parcel.readLong()
        parentName = parcel.readString()
        createdAt = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(jaName)
        parcel.writeString(enName)
        parcel.writeString(ch1Name)
        parcel.writeString(ch2Name)
        parcel.writeLong(userId)
        parcel.writeString(parentName)
        parcel.writeString(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attribute> {
        override fun createFromParcel(parcel: Parcel): Attribute {
            return Attribute(parcel)
        }

        override fun newArray(size: Int): Array<Attribute?> {
            return arrayOfNulls(size)
        }
    }
}