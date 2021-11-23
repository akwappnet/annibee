package com.devstree.annibee.model.response_model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(): Parcelable {

    @SerializedName("id")
    var userId: Long? = 0

    @SerializedName("aws_id")
    var awsId: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("user_name")
    var userName: String? = null
    @SerializedName("usertype")
    var userType: String? = null

    @SerializedName("device_type")
    var deviceType: String? = null
    @SerializedName("device_id")
    var deviceId: String? = null

    @SerializedName("otp_verified")
    var otpVerified: String? = null
    @SerializedName("otps", alternate = ["otp"])
    var otp: String? = null

    @SerializedName("profile_photo")
    var image: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null
    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("token")
    var token: String? = null

    @SerializedName("social_auth_id")
    var socialAuthId: String? = null
    @SerializedName("social_type")
    var socialType: String? = null

    @SerializedName("birthdate")
    var birthday: String? = null

    @SerializedName("temp_email")
    var tempEmail: String? = null
    @SerializedName("email_otp")
    var emailOtp: String? = null
    @SerializedName("email_otp_verified")
    var emailOtpVerified: String? = null

    @SerializedName("anniversary_count")
    var totalAnniversary: Long? = 0
    @SerializedName("event_count")
    var totalEvent: Long? = 0

    constructor(parcel: Parcel) : this() {
        userId = parcel.readLong()
        name = parcel.readString()
        email = parcel.readString()
        userName = parcel.readString()
        userType = parcel.readString()
        deviceType = parcel.readString()
        deviceId = parcel.readString()
        otpVerified = parcel.readString()
        otp = parcel.readString()
        image = parcel.readString()
        updatedAt = parcel.readString()
        createdAt = parcel.readString()
        token = parcel.readString()
        socialAuthId = parcel.readString()
        socialType = parcel.readString()
        birthday = parcel.readString()
        tempEmail = parcel.readString()
        emailOtp = parcel.readString()
        emailOtpVerified = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(userName)
        parcel.writeString(userType)
        parcel.writeString(deviceType)
        parcel.writeString(deviceId)
        parcel.writeString(otpVerified)
        parcel.writeString(otp)
        parcel.writeString(image)
        parcel.writeString(updatedAt)
        parcel.writeString(createdAt)
        parcel.writeString(token)
        parcel.writeString(socialAuthId)
        parcel.writeString(socialType)
        parcel.writeString(birthday)
        parcel.writeString(tempEmail)
        parcel.writeString(emailOtp)
        parcel.writeString(emailOtpVerified)
        parcel.writeValue(totalAnniversary)
        parcel.writeValue(totalEvent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}