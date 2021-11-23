package com.devstree.annibee.utility

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.devstree.annibee.model.response_model.User
import com.google.gson.Gson

object AppHelper {
    var user: User? = null
    val KEY_APP_LANGUAGE = "appLanguage"
    val FCM_TOKEN = "fcmToken"

    fun setLogin(isLogin: Boolean) {
        PreferenceManager.putBoolean(SharedPrefConstant.IS_LOGIN, isLogin)
    }

    fun isLoggedIn(): Boolean {
        return PreferenceManager.getBoolean(SharedPrefConstant.IS_LOGIN)
    }

    fun saveAppLanguage(text: String) {
        PreferenceManager.putString(KEY_APP_LANGUAGE, text)
    }

    fun getAppLanguage(): String {
        return PreferenceManager.getString(KEY_APP_LANGUAGE, Constants.ENGLISH).toString()
    }

    fun getAccessToken(): String? {
        return PreferenceManager.getString("token")
    }

    fun getFcmToken(): String {
        return PreferenceManager.getString(FCM_TOKEN, "").toString()
    }

    fun saveFcmToken(token: String) {
        PreferenceManager.putString(FCM_TOKEN, token)
    }

    fun setUserDetail(user: User?) {
        if (user == null) return
        AppHelper.user = user
        PreferenceManager.putString("user", Gson().toJson(user))
        if (!isLoggedIn()) PreferenceManager.putString("token", user.token)
    }

    fun getUserDetail(): User? {
        if (user == null) {
            user = Gson().fromJson(
                PreferenceManager.getString(SharedPrefConstant.USER_DETAILS),
                User::class.java
            )
        }
        return user
    }

    fun getDefaultParam(): HashMap<String, Any?> {
        val params = hashMapOf<String, Any?>()
        when (getAppLanguage()) {
            Constants.ENGLISH -> {
//                Controller.instance.updateLocale(context, Locale.US)
                params["lang_code"] = "en"
            }
            Constants.JAPANESE -> {
//                Controller.instance.updateLocale(context, Locale.JAPANESE)
                params["lang_code"] = "ja"
            }
            Constants.TRADITIONAL_CHINESE -> {
                params["lang_code"] = "zh-Hant"
            }
            Constants.SIMPLIFIED_CHINESE -> {
                params["lang_code"] = "zh-Hans"
            }
        }

        return params
    }

    private fun isIntentAvailable(ctx: Context, intent: Intent): Boolean {
        val packageManager: PackageManager = ctx.packageManager
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }

    /* @Suppress("DEPRECATION")
     @SuppressLint("PackageManagerGetSignatures")
     fun printKeyHash() {
         try {
             val info = Controller.instance.packageManager.getPackageInfo(
                 BuildConfig.APPLICATION_ID,
                 PackageManager.GET_SIGNATURES
             )
             for (signature in info.signatures) {
                 val md: MessageDigest = MessageDigest.getInstance("SHA")
                 md.update(signature.toByteArray())
                 d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
             }
         } catch (e: PackageManager.NameNotFoundException) {
             e.printStackTrace()
         } catch (e: NoSuchAlgorithmException) {
             e.printStackTrace()
         }
     }*/
}