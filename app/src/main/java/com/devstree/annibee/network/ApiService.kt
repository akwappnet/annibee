package com.devstree.annibee.network

import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.model.response_model.*
import com.devstree.annibee.network.model.ResponseBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("user/register")
    fun register(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @POST("user/login")
    fun login(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @Multipart
    @POST("user/profile/setup")
    fun updateProfile(
        @PartMap params: HashMap<String, RequestBody?>,
        @Part image: MultipartBody.Part?
    ): Call<ResponseBody<User>>?

    @POST("user/get/profile")
    fun getUserProfile(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @POST("user/notification-list")
    fun alertNotification(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<AlertNotification>>>?

    @POST("user/attribute-list")
    fun attribute(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<Attribute>>>?


    @POST("user/home-data")
    fun getHomeData(@Body params: HashMap<String, Any?>): Call<ResponseBody<HomeData>>


    @Multipart
    @POST("user/create-anniversary")
    fun createAnniversary(
        @PartMap params: HashMap<String, RequestBody?>,
        @Part images: Array<MultipartBody.Part?>,
        @Part("notification_id[]") list: Array<Long?>
    ): Call<ResponseBody<AnniversaryEvent>>?

    @POST("user/anniversary-datail")
    fun anniversaryDetail(@Body params: HashMap<String, Any?>): Call<ResponseBody<AnniversaryEvent>>?

    @POST("user/anniversary-list")
    fun anniversaryList(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<AnniversaryEvent>>>?

    @POST("user/anniversary-delete")
    fun deleteAnniversary(@Body params: HashMap<String, Any?>): Call<ResponseBody<AnniversaryEvent>>?

    @Multipart
    @POST("user/update-anniversary")
    fun updateAnniversary(
        @PartMap params: HashMap<String, RequestBody?>,
        @Part images: Array<MultipartBody.Part?>,
        @Part("notification_id[]") notificationList: Array<Long?>,
        @Part("remove_photo_id[]") removePhotoId: Array<Long?>?
    ): Call<ResponseBody<AnniversaryEvent>>?


    @POST("user/anniversary-dropdown")
    fun getAnniversaryDropDownList(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<PopUpModel>>>

    @Multipart
    @POST("user/create-event")
    fun createEvent(
        @PartMap params: HashMap<String, RequestBody?>,
        @Part images: Array<MultipartBody.Part?>,
        @Part("event_date[]") dateList: Array<RequestBody?>,
        @Part("event_time[]") timeList: Array<RequestBody?>
    ): Call<ResponseBody<AnniversaryEvent>>?

    @POST("user/event-datail")
    fun eventDetail(@Body params: HashMap<String, Any?>): Call<ResponseBody<AnniversaryEvent>>?

    @POST("user/event-list")
    fun eventList(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<AnniversaryEvent>>>?

    @POST("user/event-delete")
    fun deleteEvent(@Body params: HashMap<String, Any?>): Call<ResponseBody<AnniversaryEvent>>?

    @Multipart
    @POST("user/update-event")
    fun updateEvent(
        @PartMap params: HashMap<String, RequestBody?>,
        @Part images: Array<MultipartBody.Part?>,
        @Part("event_date[]") dateList: Array<RequestBody?>,
        @Part("event_time[]") timeList: Array<RequestBody?>,
        @Part("remove_photo_id[]") removePhotoId: Array<Long?>?
    ): Call<ResponseBody<AnniversaryEvent>>?


    @POST("user/search-anniversary-home")
    fun searchData(@Body params: HashMap<String, Any?>) : Call<ResponseBody<HomeData>>


    @POST("user/calendar-data")
    fun calendarData(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<CalendarData>>>?

    @POST("user/fcmtokenupdate")
    fun updateToken(@Body params: HashMap<String, Any?>): Call<ResponseBody<FCMToken>>?

    @POST("user/fcm-notification-list")
    fun fcmNotificationList(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<AnniversaryEvent>>>?


    //param title and description
    @POST("user/contact-us")
    fun contactUs(@Body params: HashMap<String, Any?>): Call<ResponseBody<ContactUs>>?

    @POST("user/faqs")
    fun FAQ(@Body params: HashMap<String, Any?>): Call<ResponseBody<List<FAQ>>>?

    //params is_notification in string 1 or 0
    @POST("user/notification-setting")
    fun notificationSetting(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?



    @POST("user/forgotpassword")
    fun forgotPassword(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @POST("user/verifyotp")
    fun verifyCode(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @POST("user/resetpassword")
    fun resetPassword(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @POST("user/change/password")
    fun changePassword(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?

    @POST("user/logout")
    fun logout(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>

    @POST("user/deleteaccount")
    fun deleteAccount(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>

    @POST("user/email-verify")
    fun emailVerifyOtp(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>

    @POST("user/read-notification")
    fun readNotification(): Call<ResponseBody<List<AnniversaryEvent>>>


//    @POST("user/resendotp")
//    fun resendCode(@Body params: HashMap<String, Any?>): Call<ResponseBody<User>>?
//    @POST("user/change/password")
//    fun changePassword(@Body params: HashMap<String, Any>?): Call<ResponseBody<User>>?

}
