package com.devstree.annibee.network

import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.model.response_model.*
import com.devstree.annibee.network.RetroClient.apiService
import com.devstree.annibee.network.model.ResponseBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object NetworkCall {
    fun register(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.register(param)?.enqueue(callback)
    }

    fun login(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.login(param)?.enqueue(callback)
    }


    fun getHomeData(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<HomeData>>) {
        apiService.getHomeData(param).enqueue(callback)
    }

    fun searchData(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<HomeData>>) {
        apiService.searchData(param).enqueue(callback)
    }


    fun updateProfile(param: HashMap<String, RequestBody?>, file: File?, callback: BaseResponseListener<ResponseBody<User>>) {
        var multipartBody : MultipartBody.Part? = null
        if (file!=null){
            multipartBody = MultipartBody.Part.createFormData("profile_photo", file.name, file.asRequestBody("image/*".toMediaType()))
        }
        apiService.updateProfile(param, multipartBody)?.enqueue(callback)
    }

    fun getUserProfile(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.getUserProfile(param)?.enqueue(callback)
    }

    fun alertNotification(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<AlertNotification>>>) {
        apiService.alertNotification(param)?.enqueue(callback)
    }

    fun attribute(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<Attribute>>>) {
        apiService.attribute(param)?.enqueue(callback)
    }

    fun createAnniversary(param: HashMap<String, RequestBody?>, images: Array<MultipartBody.Part?>, list: Array<Long?>, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.createAnniversary(param, images, list)?.enqueue(callback)
    }

    fun updateAnniversary(param: HashMap<String, RequestBody?>, images: Array<MultipartBody.Part?>, notificationList: Array<Long?>, removePhotoList: Array<Long?>?, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.updateAnniversary(param, images, notificationList, removePhotoList)?.enqueue(callback)
    }

    fun anniversaryDetail(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.anniversaryDetail(param)?.enqueue(callback)
    }

    fun anniversaryList(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>) {
        apiService.anniversaryList(param)?.enqueue(callback)
    }

    fun deleteAnniversary(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.deleteAnniversary(param)?.enqueue(callback)
    }

    fun getAnniversaryDropDown(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<PopUpModel>>>) {
        apiService.getAnniversaryDropDownList(param).enqueue(callback)
    }

    fun createEvent(param: HashMap<String, RequestBody?>, images: Array<MultipartBody.Part?>, dateList: Array<RequestBody?>, timeList: Array<RequestBody?>, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.createEvent(param, images, dateList, timeList)?.enqueue(callback)
    }

    fun updateEvent(param: HashMap<String, RequestBody?>, images: Array<MultipartBody.Part?>, dateList: Array<RequestBody?>, timeList: Array<RequestBody?>, removePhotoList: Array<Long?>?, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.updateEvent(param, images, dateList, timeList, removePhotoList)?.enqueue(callback)
    }

    fun eventDetail(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.eventDetail(param)?.enqueue(callback)
    }

    fun eventList(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>) {
        apiService.eventList(param)?.enqueue(callback)
    }

    fun deleteEvent(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<AnniversaryEvent>>) {
        apiService.deleteEvent(param)?.enqueue(callback)
    }



    fun calendarData(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<CalendarData>>>) {
        apiService.calendarData(param)?.enqueue(callback)
    }

    fun updateToken(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<FCMToken>>) {
        apiService.updateToken(param)?.enqueue(callback)
    }

    fun fcmNotificationList(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>) {
        apiService.fcmNotificationList(param)?.enqueue(callback)
    }

    fun contactUs(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<ContactUs>>) {
        apiService.contactUs(param)?.enqueue(callback)
    }

    fun faq(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<List<FAQ>>>) {
        apiService.FAQ(param)?.enqueue(callback)
    }

    fun notificationSetting(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.notificationSetting(param)?.enqueue(callback)
    }

    fun forgotPassword(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.forgotPassword(param)?.enqueue(callback)
    }

    fun verifyCode(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.verifyCode(param)?.enqueue(callback)
    }

    fun resetPassword(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.resetPassword(param)?.enqueue(callback)
    }

    fun changePassword(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.changePassword(param)?.enqueue(callback)
    }

    fun logout(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.logout(param).enqueue(callback)
    }

    fun deleteAccount(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.deleteAccount(param).enqueue(callback)
    }

    fun emailVerifyOtp(param: HashMap<String, Any?>, callback: BaseResponseListener<ResponseBody<User>>) {
        apiService.emailVerifyOtp(param).enqueue(callback)
    }

    fun readNotification(callback: BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>) {
        apiService.readNotification().enqueue(callback)
    }
}
