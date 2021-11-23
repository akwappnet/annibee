package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Image() : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("anniversary_photo")
    var image: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        image = parcel.readString()
    }

    constructor(string: String) : this() {
        image = string
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }

}