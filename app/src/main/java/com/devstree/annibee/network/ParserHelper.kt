package com.devstree.annibee.network

import com.devstree.annibee.model.BaseModel
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

object ParserHelper {
    fun baseError(error: ResponseBody?): BaseModel {
        return try {
            Gson().fromJson(error!!.charStream(), BaseModel::class.java)
        } catch (e: Exception) {
            BaseModel(false, ResponseHandler.handleErrorResponse(e))
        }
    }

    fun <T> baseError(response: Response<T>): BaseModel {
        return try {
            return Gson().fromJson(response.body().toString().takeIf { response.code() == NetworkURL.RESPONSE_OK || response.code() == NetworkURL.RESPONSE_CREATED }
                ?: response.errorBody()?.string(), BaseModel::class.java)
        } catch (e: Exception) {
            BaseModel(false, ResponseHandler.handleErrorResponse(e))
        }
    }
}