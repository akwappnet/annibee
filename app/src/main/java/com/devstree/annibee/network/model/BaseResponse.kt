package com.devstree.annibee.network.model


open class BaseResponse {
    var code = 0
    var message = "Unable to parse response"

    val success: Boolean get() = code == 200 || code == 1
}