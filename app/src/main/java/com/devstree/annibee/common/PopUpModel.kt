package com.devstree.annibee.common

import com.google.gson.annotations.SerializedName

/**
 * Created by Dhaval Baldha on 7/12/20
 */
data class PopUpModel(
    val id: String,
    @SerializedName("name")
    val title: String
)