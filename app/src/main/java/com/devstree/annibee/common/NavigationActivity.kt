package com.devstree.annibee.common

import android.content.Intent
import android.net.Uri
import com.devstree.annibee.activity.*
import com.devstree.annibee.dialog.EventBasedOnDateDialog
import com.devstree.annibee.dialog.SelectAnniversaryDialog
import com.devstree.annibee.model.response_model.AnniversaryEvent

abstract class NavigationActivity : BaseActivity() {

    open fun openWebLink(urlLink: String) {
        var url = urlLink
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://$url"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun openIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun openForgotPasswordActivity() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun openSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun openWebActivity(str: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", str)
        startActivityForResult(intent, 1001)
    }

    fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun openSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    fun openCreateAnniversaryActivity() {
        val intent = Intent(this, CreateAnniversaryActivity::class.java)
        startActivity(intent)
    }

    fun openCreateEventActivity() {
        val intent = Intent(this, CreateEventActivity::class.java)
        startActivity(intent)
    }

    fun openAnniversaryDetailActivity() {
        val intent = Intent(this, AnniversaryDetailActivity::class.java)
        startActivity(intent)
    }

    fun openEventDetailActivity() {
        val intent = Intent(this, EventDetailActivity::class.java)
        startActivity(intent)
    }

    fun openSelectAnniversaryDialog() {
        val dialog = SelectAnniversaryDialog.newInstance()
        dialog.show(supportFragmentManager, dialog::class.java.simpleName)
    }

    fun openEventBasedOnDateDialog(
        date: Int,
        month: Int,
        year: Int,
        eventList: ArrayList<AnniversaryEvent>
    ) {
        val dialog = EventBasedOnDateDialog.newInstance(date, month, year, eventList)
        dialog.show(supportFragmentManager, dialog::class.java.simpleName)
    }

    fun openNavigationActivity() {
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    fun openListActivity() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }
}