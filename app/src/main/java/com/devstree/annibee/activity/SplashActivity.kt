package com.devstree.annibee.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.devstree.annibee.Controller
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivitySplashBinding
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class SplashActivity : NavigationActivity(), Runnable {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenActivity()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        Handler(Looper.getMainLooper()).postDelayed(this, 2000)
    }

    override fun run() {

        getFcmToken()

        val language = AppHelper.getAppLanguage()
        when (language) {
            Constants.ENGLISH -> {
                Controller.instance.updateLocale(this, Locale.US)
            }
            Constants.JAPANESE -> {
                Controller.instance.updateLocale(this, Locale.JAPANESE)
            }
            Constants.TRADITIONAL_CHINESE -> {
                Controller.instance.updateLocale(this, Locale.TRADITIONAL_CHINESE)
            }
            Constants.SIMPLIFIED_CHINESE -> {
                Controller.instance.updateLocale(this, Locale.SIMPLIFIED_CHINESE)
            }
        }
        if (AppHelper.isLoggedIn()) {
            openHomeActivity()
        } else {
            openIntroActivity()
        }
        finish()
    }
}