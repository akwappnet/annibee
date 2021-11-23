package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class ContactUs() : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("user_id")
    var userId: Long = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        title = parcel.readString()
        description = parcel.readString()
        updatedAt = parcel.readString()
        createdAt = parcel.readString()
        userId = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(updatedAt)
        parcel.writeString(createdAt)
        parcel.writeLong(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactUs> {
        override fun createFromParcel(parcel: Parcel): ContactUs {
            return ContactUs(parcel)
        }

        override fun newArray(size: Int): Array<ContactUs?> {
            return arrayOfNulls(size)
        }
    }


}