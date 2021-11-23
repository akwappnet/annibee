package com.devstree.annibee

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.widget.ListPopupWindow
import com.devstree.googlelogin.GoogleLogin
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class Controller : Application() {

    lateinit var currentDate: CalendarDay
    lateinit var loginManager: LoginManager
    lateinit var googleLogin: GoogleLogin
    var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var listPopupWindow: ListPopupWindow

    override fun onCreate() {
        super.onCreate()
        instance = this
        currentDate = CalendarDay.from(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH)+1,
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        loginManager = LoginManager.getInstance()

        googleLogin = GoogleLogin()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        listPopupWindow = ListPopupWindow(this)
    }

    companion object {
        lateinit var instance: Controller
        private val TAG = Controller::class.java.name
    }

    fun updateLocale(c: Context, localeToSwitchTo: Locale)/*: ContextWrapper */ {
        var context = c
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            }

        } else {
            Locale.setDefault(localeToSwitchTo)
            configuration.locale = localeToSwitchTo
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
//        return ContextWrapper(context)
    }
}