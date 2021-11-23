package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class AnniversaryEvent() : Parcelable {

    @SerializedName("id")
    var id: Long = 0
    @SerializedName("user_id")
    var userId: Long = 0

    @SerializedName("name",  alternate = ["ja_name", "en_name"])
    var name: String? = null

    @SerializedName("date")
    var date: String? = null
    @SerializedName("event_time")
    var eventTime: String? = null
    @SerializedName("event_format_time")
    var eventFormatTime: String? = null

    @SerializedName("event_end_date")
    var eventEndDate: String? = null
    @SerializedName("event_end_time")
    var eventEndTime: String? = null
    @SerializedName("event_format_end_time")
    var eventFormatEndTime: String? = null

    @SerializedName("attribute_id")
    var attributeId: String? = null
    @SerializedName("attribute_name")
    var attributeName: String? = null
    @SerializedName("default_attribute_name")
    var defaultAttributeName: String? = null

    @SerializedName("note")
    var note: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("notifications")
    var notifications: List<AlertNotification>? = null

    @SerializedName("photos")
    var photos: List<Image>? = null

    @SerializedName("event_photos")
    var eventPhotos: List<Event>? = null

    @SerializedName("is_all_day")
    var isAllDay: String? = null
    @SerializedName("people")
    var people: String? = null

    @SerializedName("anniversary_name")
    var anniversaryName: String? = null
    @SerializedName("anniversary_id")
    var anniversaryId: Long = 0
    @SerializedName("event_name")
    var eventName: String? = null
    @SerializedName("event_id")
    var eventId: Long = 0

    @SerializedName("dates")
    var eventDates: List<DateTime>? = null

    @SerializedName("anniversary_data")
    var anniversaryData: AnniversaryEvent? = null

    @SerializedName("remain_days")
    var remainDays: Long = 0

    @SerializedName("total_years")
    var totalYears: Long = 0

    @SerializedName("is_advertise")
    var isAdvertise: Long = 0

    @SerializedName("past_days")
    var pastDays: Long = 0
    @SerializedName("past_months")
    var pastMonths: Long = 0
    @SerializedName("past_years")
    var pastYears: Long = 0

    @SerializedName("current_year_anniversary_data")
    var anniversaryList: List<AnniversaryEvent>? = null
    @SerializedName("event_data")
    var eventList: List<AnniversaryEvent>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        userId = parcel.readLong()
        name = parcel.readString()
        date = parcel.readString()
        eventTime = parcel.readString()
        eventFormatTime = parcel.readString()
        eventEndDate = parcel.readString()
        eventEndTime = parcel.readString()
        eventFormatEndTime = parcel.readString()
        attributeId = parcel.readString()
        attributeName = parcel.readString()
        defaultAttributeName = parcel.readString()
        note = parcel.readString()
        type = parcel.readString()
        updatedAt = parcel.readString()
        createdAt = parcel.readString()
        notifications = parcel.createTypedArrayList(AlertNotification)
        photos = parcel.createTypedArrayList(Image)
        eventPhotos = parcel.createTypedArrayList(Event)
        isAllDay = parcel.readString()
        people = parcel.readString()
        anniversaryName = parcel.readString()
        anniversaryId = parcel.readLong()
        eventName = parcel.readString()
        eventId = parcel.readLong()
        eventDates = parcel.createTypedArrayList(DateTime)
        anniversaryData = parcel.readParcelable(AnniversaryEvent::class.java.classLoader)
        remainDays = parcel.readLong()
        totalYears = parcel.readLong()
        isAdvertise = parcel.readLong()
        pastDays = parcel.readLong()
        pastMonths = parcel.readLong()
        pastYears = parcel.readLong()
        anniversaryList = parcel.createTypedArrayList(CREATOR)
        eventList = parcel.createTypedArrayList(CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(userId)
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(eventTime)
        parcel.writeString(eventFormatTime)
        parcel.writeString(eventEndDate)
        parcel.writeString(eventEndTime)
        parcel.writeString(eventFormatEndTime)
        parcel.writeString(attributeId)
        parcel.writeString(attributeName)
        parcel.writeString(defaultAttributeName)
        parcel.writeString(note)
        parcel.writeString(type)
        parcel.writeString(updatedAt)
        parcel.writeString(createdAt)
        parcel.writeTypedList(notifications)
        parcel.writeTypedList(photos)
        parcel.writeTypedList(eventPhotos)
        parcel.writeString(isAllDay)
        parcel.writeString(people)
        parcel.writeString(anniversaryName)
        parcel.writeLong(anniversaryId)
        parcel.writeString(eventName)
        parcel.writeLong(eventId)
        parcel.writeTypedList(eventDates)
        parcel.writeParcelable(anniversaryData, flags)
        parcel.writeLong(remainDays)
        parcel.writeLong(totalYears)
        parcel.writeLong(isAdvertise)
        parcel.writeLong(pastDays)
        parcel.writeLong(pastMonths)
        parcel.writeLong(pastYears)
        parcel.writeTypedList(anniversaryList)
        parcel.writeTypedList(eventList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnniversaryEvent> {
        override fun createFromParcel(parcel: Parcel): AnniversaryEvent {
            return AnniversaryEvent(parcel)
        }

        override fun newArray(size: Int): Array<AnniversaryEvent?> {
            return arrayOfNulls(size)
        }
    }

}