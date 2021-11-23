package com.devstree.annibee.model

import com.google.gson.annotations.SerializedName

open class BaseModel(var success: Boolean, var message: String) {

    @SerializedName("statusCode")
    var code: Int = 0

    @SerializedName("messageCode")
    var messageCode: String = ""
}