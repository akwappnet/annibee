package com.devstree.annibee.model.response_model

import com.google.gson.annotations.SerializedName

class FAQ {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("title")
    var question: String? = null

    @SerializedName("description")
    var answer: String? = null
}