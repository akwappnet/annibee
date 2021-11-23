package com.devstree.annibee.model.response_model

import com.google.gson.annotations.SerializedName

class HomeData {

    @SerializedName("today")
    var today: ArrayList<AnniversaryEvent>? = null

    @SerializedName("future")
    var upcoming: ArrayList<AnniversaryEvent>? = null

    @SerializedName("past")
    var past: ArrayList<AnniversaryEvent>? = null

    @SerializedName("all")
    var all: ArrayList<AnniversaryEvent>? = null

    @SerializedName("notification_counter")
    var notificationCount: Int? = null

}