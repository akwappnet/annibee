package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CalendarData() : Parcelable {

    @SerializedName("date")
    var date: Long? = 0

    @SerializedName("items")
    var items: List<AnniversaryEvent>? = null

    constructor(parcel: Parcel) : this() {
        date = parcel.readLong()
        items = parcel.createTypedArrayList(AnniversaryEvent)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(date)
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarData> {
        override fun createFromParcel(parcel: Parcel): CalendarData {
            return CalendarData(parcel)
        }

        override fun newArray(size: Int): Array<CalendarData?> {
            return arrayOfNulls(size)
        }
    }

}