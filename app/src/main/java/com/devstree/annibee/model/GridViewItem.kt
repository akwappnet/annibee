package com.devstree.annibee.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class GridViewItem(
    val title: String? = null,
    val path: String? = null,
    var isBitMap: Boolean = false

): Parcelable {

    var isSelected: Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    ) {
        isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(path)
        parcel.writeByte(if (isBitMap) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GridViewItem> {
        override fun createFromParcel(parcel: Parcel): GridViewItem {
            return GridViewItem(parcel)
        }

        override fun newArray(size: Int): Array<GridViewItem?> {
            return arrayOfNulls(size)
        }
    }
}