package com.devstree.annibee.network.model


open class ResponseBody<T> : BaseResponse() {

    var total_records: Int = 0
    var page_no: Int = 0

    var data: T? = null
}