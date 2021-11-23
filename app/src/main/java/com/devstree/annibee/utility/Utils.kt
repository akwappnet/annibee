package com.devstree.annibee.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.format.DateFormat
import androidx.annotation.StringRes
import com.devstree.annibee.Controller
import java.text.DecimalFormat
import java.util.*

object Utils {
    fun getUniquePsuedoID(): String {
        val m_szDevIDShort = "35" + Build.BOARD.length % 10
        +Build.BRAND.length % 10
        +Build.CPU_ABI.length % 10
        +Build.DEVICE.length % 10
        +Build.MANUFACTURER.length % 10
        +Build.MODEL.length % 10
        +Build.PRODUCT.length % 10

        var serial: String? = null
        try {
            serial = Build::class.java.getField("SERIAL")[null]!!.toString()
            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            serial = "serial"
        }
        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
    }


    fun CheckOnMainThread(): Boolean {
        return Looper.getMainLooper().thread == Thread.currentThread()
    }

    fun executeOnMain(runnable: Runnable) {
        Handler(Looper.getMainLooper()).post(runnable)
    }

    fun executeInBackground(runnable: Runnable?) {
        Thread(runnable).start()
    }

    fun executeDelay(runnable: Runnable, delay: Long) {
        Looper.myLooper()?.let { Handler(it).postDelayed(runnable, delay) }
    }


    @JvmStatic
    fun Toast(s: String) {
        android.widget.Toast.makeText(Controller.instance, s, android.widget.Toast.LENGTH_SHORT)
            .show()
    }

    fun Toast(@StringRes message: Int) {
        android.widget.Toast.makeText(
            Controller.instance,
            message,
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    fun getFormattedDate(time: Long): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = time
        val now = Calendar.getInstance()
        val timeFormatString = ", MMMM d"
        val dateTimeFormatString = "EEEE, MMMM d"
        val HOURS = 60 * 60 * 60.toLong()
        return if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
            "Today" + DateFormat.format(timeFormatString, smsTime)
        } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else if (now[Calendar.YEAR] == smsTime[Calendar.YEAR]) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString()
        }
    }

    fun getDecimalFormatter(): DecimalFormat {
        val formatter = DecimalFormat("##.00")
        return formatter
    }

    fun openInstagramLink(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.setPackage("com.instagram.android")
        try {
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        }
    }

}