package com.devstree.annibee.network


import com.devstree.annibee.network.model.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseResponseListener<T : ResponseBody<*>> : Callback<T> {
    var code = 200
    var message: String? = ""
    var success: Boolean = false

    abstract fun result(response: T?)

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) success(body)
            else onError(response.message(), response.code())
        } else {
            val message = response.message().takeIf { response.code() == 200 }
                ?: ParserHelper.baseError(response.errorBody()).message
            onError(message, response.code())
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        t.printStackTrace()
        message = t.localizedMessage
        success = false
        result(null)
    }

    private fun success(data: T?) {
        if (data != null) {
            success = data.success
            message = data.message
        }
        result(data)
    }

    private fun onError(message: String, code: Int) {
        this.message = message
        this.code = code
        success = false
        result(null)
    }
}
/*override fun onFailure(call: Call<T>, t: Throwable) {
    t.printStackTrace()
    onError(call, t)
}

    private fun success(data: T?) {
        if (data != null) {
            message = data.message
            isSuccess = data.success
            messageCode = data.messageCode
        } else {
            isSuccess = false
        }
        result(data)
    }

    private fun onFailure(baseModel: BaseModel?) {
        if (baseModel != null) {
            message = baseModel.message
            isSuccess = baseModel.success
            messageCode = baseModel.messageCode
        } else {
            isSuccess = false
        }
        result(null)
    }

private fun onError(call: Call<T>, t: Throwable) {
    message = ResponseHandler.handleErrorResponse(t)
    isSuccess = false
    exception = t
    result(null)
}*/