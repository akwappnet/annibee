package com.devstree.annibee.common

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.devstree.annibee.BuildConfig
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.activity.LoginActivity
import com.devstree.annibee.adapter.AttributeAdapter
import com.devstree.annibee.adapter.PopupWindowAdapter
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.Attribute
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.PreferenceManager
import com.devstree.annibee.utility.justTry
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList


//@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {
    private var snackbar: Snackbar? = null
    var mContext: Context = this
    private var toolbar: Toolbar? = null
    private var toolbarTitle: TextView? = null
    private val strUserEndPoints = ArrayList<String>()
    private var alertDialog: AlertDialog? = null
    private var initialLocale: String? = null
    lateinit var currentFragment: Fragment

    override fun attachBaseContext(newBase: Context?) {

        when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                Controller.instance.updateLocale(newBase!!, Locale.ENGLISH)
                super.attachBaseContext(newBase)
            }
            Constants.JAPANESE -> {
                Controller.instance.updateLocale(newBase!!, Locale.JAPANESE)
                super.attachBaseContext(newBase)

            }
            Constants.TRADITIONAL_CHINESE -> {
                Controller.instance.updateLocale(newBase!!, Locale.TRADITIONAL_CHINESE)
                super.attachBaseContext(newBase)

            }
            Constants.SIMPLIFIED_CHINESE -> {
                Controller.instance.updateLocale(newBase!!, Locale.SIMPLIFIED_CHINESE)
                super.attachBaseContext(newBase)

            }
        }
    }

    fun unAuthorized(code: Int, message: String) {
        if (code == 401) {
            DialogHelper.newInstance(
                message,
                getString(R.string.login),
                object : IDialogButtonClick {
                    override fun onButtonClick(isPositive: Boolean) {
                        AppHelper.setLogin(false)
                        Controller.instance.mGoogleSignInClient?.signOut()
                        val intent = Intent(this@BaseActivity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                })
                .show(this)
        } else DialogHelper.newInstance(message).show(this)
    }

    fun noNetWorkAvailable() {
        DialogHelper.newInstance(
            getString(R.string.no_internet_connection),
            object : IDialogButtonClick {
                override fun onButtonClick(isPositive: Boolean) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_SETTINGS
                    /*intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri: Uri = Uri.fromParts("package", activity.getPackageName(), null)
                    intent.data = uri*/
                    startActivity(intent)
                }

            }).show(this)
    }

    abstract fun initUi()

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initUi()
    }

    fun fullScreenActivity() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun shareApplication() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        )
        intent.type = "text/plain"
        startActivity(intent)
    }

    fun showSnackBar(view: View?, str: String?) {
        if (view == null) return
        if (str.isNullOrEmpty()) return
        if (snackbar != null && snackbar!!.isShownOrQueued) {
            snackbar?.dismiss()
        }
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun showSnackBar(str: String?) {
        val view: View = this.window.decorView
        if (str.isNullOrEmpty()) return
        if (snackbar != null) snackbar?.dismiss()
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun toast(str: String?) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    open fun recreateActivity() {
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        var isNetAvailable = false
        if (context != null) {
            val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = mConnectivityManager.activeNetworkInfo
            if (activeNetwork != null) isNetAvailable = true
        }
        return isNetAvailable
    }

    private var dialog: ProgressDialog? = null
    fun showProgressDialog() {
        showProgressDialog(getString(R.string.please_wait))
    }

    fun showProgressDialog(msg: String?) {
        if (dialog == null) {
            dialog = ProgressDialog.show(
                this,
                null,
                getString(R.string.please_wait).takeIf { msg.isNullOrEmpty() } ?: msg,
                true
            )
        }
        if (!dialog!!.isShowing)
            dialog!!.show()
    }

    fun hideProgressDialog() {
        justTry {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        }
    }

    fun setSecureActivity() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    fun keepScreenOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // set toolbar with center text and allow home button if set isHomeUpEnabled is true
    protected fun setUpToolBar(title: String, isHomeUpEnabled: Boolean) {
        justTry {
            toolbar = findViewById(R.id.toolbar)
            toolbarTitle = findViewById(R.id.txtToolbarTitle)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            toolbarTitle?.text = title
            val back: ImageView? = findViewById(R.id.imgBack)
            back?.visibility = View.VISIBLE.takeIf { isHomeUpEnabled } ?: View.GONE
            if (isHomeUpEnabled) {
                back?.setOnClickListener { onBackPressed() }
            }
        }
    }

    protected fun setUpToolBar(@StringRes title: Int, isHomeUpEnabled: Boolean) {
        setUpToolBar(getString(title), isHomeUpEnabled)
    }

    protected fun updateTitle(@StringRes title: Int) {
        toolbarTitle?.setText(title)
    }

    fun loadFragment(fragment: Fragment?,fragmentName: String): Boolean {
        //switching fragment
        if (fragment != null) {
            currentFragment = fragment
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment,fragmentName)
                .commit()
            return true
        }
        return false
    }

    open fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val fcmToken = task.result

            AppHelper.saveFcmToken(fcmToken.toString())

            com.devstree.annibee.utility.Log.e("FCM Token:", fcmToken.toString())
        })
    }

    open fun getFcmToken(callBack: (fcmToken: String, isSuccess: Boolean) -> Unit) {
//        val fcmToken = PreferenceManager.getString(SharedPrefConstant.FCM_TOKEN)
//        if (fcmToken.isNullOrEmpty()) {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    callBack.invoke(task.exception?.localizedMessage
//                        ?: "Fetching FCM registration token failed", false)
//                    return@OnCompleteListener
//                }
//                val token = task.result
//                if (token.isNullOrEmpty()) {
//                    callBack.invoke(task.exception?.localizedMessage
//                        ?: "Fetching FCM registration token failed", false)
//                    return@OnCompleteListener
//                }
//                PreferenceManager.putString(SharedPrefConstant.FCM_TOKEN, token)
//                callBack.invoke(token, true)
//            })
//        } else {
//            callBack.invoke(fcmToken, true)
//        }
    }

    open fun logout(callBack: (isSuccess: Boolean) -> Unit) {
        try {
            logoutActions()
            callBack.invoke(true)
        } catch (e: Exception) {
            e.printStackTrace()
            callBack.invoke(false)
        }
    }

    fun logoutActions() {
        PreferenceManager.clearPreference()
//        AppHelper.user = null
    }

    open fun showAlertMessage(str: String) {
        showAlertMessage(str = str, null)
    }

    open fun showAlertMessage(str: String, onClickListener: DialogInterface.OnClickListener?) {
        showAlertMessage(null, str, true, "", onClickListener)
    }

    open fun showAlertMessage(
        title: String?, str: String, isCancelable: Boolean, positiveText: String,
        onClickListener: DialogInterface.OnClickListener?,
    ): AlertDialog? {
        try {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog!!.dismiss()
            }
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialog))
                .setMessage(str).setCancelable(isCancelable)
                .setPositiveButton(positiveText.takeIf { positiveText.isNotBlank() }
                    ?: getString(R.string.ok), onClickListener)

            if (!title.isNullOrBlank()) builder.setTitle(title)

            alertDialog = builder.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return alertDialog
    }

    open fun showAlertMessage(
        title: String = "",
        str: String,
        isCancelable: Boolean,
        positiveText: String,
        nagetiveText: String,
        callback: (isPositive: Boolean) -> Unit,
    ): AlertDialog? {
        try {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog!!.dismiss()
            }
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialog))
                .setMessage(str)
                .setCancelable(isCancelable)
                .setPositiveButton(positiveText.takeIf { positiveText.isNotBlank() }
                    ?: getString(R.string.ok)) { _, _ -> callback.invoke(true) }
                .setNegativeButton(nagetiveText.takeIf { nagetiveText.isNotBlank() }
                    ?: getString(R.string.cancel)) { _, _ -> callback.invoke(false) }

            if (!title.isBlank()) builder.setTitle(title)

            alertDialog = builder.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return alertDialog
    }

    fun pickDateTime(onPickedDateTime: TimePickerDialog.OnTimeSetListener, is24HourView: Boolean) {

        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            mContext, R.style.DatePickerTheme,
            onPickedDateTime,
            startHour,
            startMinute,
            is24HourView
        )

        timePickerDialog.show()
    }

    fun pickPreviousDate(
        selectedDate: Calendar? = null,
        onPickedDate: (calender: Calendar) -> Unit
    ) {
        val currentDateTime = selectedDate.takeIf { selectedDate == null } ?: Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(mContext, { _, year, month, day ->
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year, month, day)
            onPickedDate.invoke(pickedDateTime)
        }, startYear, startMonth, startDay)

        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
    }

    fun openDatePickerDialog(
        onDateSet: DatePickerDialog.OnDateSetListener,
        day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
        month: Int = Calendar.getInstance().get(Calendar.MONTH),
        year: Int = Calendar.getInstance().get(Calendar.YEAR),
        isBirthDate: Boolean = false
    ) {
//        val calendar: Calendar = Calendar.getInstance()
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val month = calendar.get(Calendar.MONTH)
//        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(this, R.style.DatePickerTheme, onDateSet, year, month, day)
        if (isBirthDate) datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.datePicker.firstDayOfWeek = Calendar.MONDAY
        datePickerDialog.show()
    }

    open fun showPopupMenu(
        activity: Activity,
        anchorView: View?,
        list: List<PopUpModel>,
        fixWidth: Boolean,
        objectCallback: ObjectCallback<PopUpModel>

    ) {
        hideKeyBoard()
        if (list.isEmpty()) {
            return
        }
        val listPopupWindow = Controller.instance.listPopupWindow
        if (fixWidth) {
            listPopupWindow.width = 600
            listPopupWindow.horizontalOffset = 30
        }
        if (anchorView != null) listPopupWindow.anchorView = anchorView
        val listPopupWindowAdapter =
            PopupWindowAdapter(activity, list, object : ObjectCallback<PopUpModel> {
                override fun response(obj: PopUpModel?) {
                    objectCallback.response(obj)
                    listPopupWindow.dismiss()
                }
            })
        listPopupWindow.setAdapter(listPopupWindowAdapter)
        listPopupWindow.show()
    }

    open fun showAttribute(
        activity: Activity,
        anchorView: View?,
        list: List<Attribute>,
        fixWidth: Boolean,
        objectCallback: ObjectCallback<Attribute>

    ) {
        hideKeyBoard()
        if (list.isEmpty()) {
            return
        }
        val listPopupWindow = ListPopupWindow(activity)
        if (fixWidth) {
            listPopupWindow.width = 600
            listPopupWindow.horizontalOffset = 30
        }
        if (anchorView != null) listPopupWindow.anchorView = anchorView
        val listPopupWindowAdapter =
            AttributeAdapter(activity, list, object : ObjectCallback<Attribute> {
                override fun response(obj: Attribute?) {
                    objectCallback.response(obj)
                    listPopupWindow.dismiss()
                }
            })
        listPopupWindow.setAdapter(listPopupWindowAdapter)
        listPopupWindow.show()
    }

    fun addAttribute(): ArrayList<PopUpModel> {

        val list = ArrayList<PopUpModel>()
        list.add(PopUpModel("1", getString(R.string.birthday)))
        list.add(PopUpModel("2", getString(R.string.default_anniversary)))
        list.add(PopUpModel("3", getString(R.string.dating_anniversary)))
        list.add(PopUpModel("4", getString(R.string.wedding_anniversary)))
        list.add(PopUpModel("5", getString(R.string.anniversary_of_establishment)))
        list.add(PopUpModel("6", getString(R.string.date_of_death)))
        list.add(PopUpModel("7", getString(R.string.any)))

        return list
    }

    fun defaultAnniversaryList(): ArrayList<PopUpModel> {

        val list = ArrayList<PopUpModel>()
        list.add(PopUpModel("1", getString(R.string.mother_s_day)))
        list.add(PopUpModel("2", getString(R.string.father_s_day)))
        list.add(PopUpModel("3", getString(R.string.valentine)))
        list.add(PopUpModel("4", getString(R.string.white_day)))
        list.add(PopUpModel("5", getString(R.string.Respect_for_the_aged_day)))
        list.add(PopUpModel("6", getString(R.string.christmas_eve)))
        list.add(PopUpModel("7", getString(R.string.christmas)))
        list.add(PopUpModel("8", getString(R.string.new_year_s_day)))

        return list
    }

    fun yearList(): List<PopUpModel> {
        val list = ArrayList<PopUpModel>()
        list.add(PopUpModel("1", "2021"))
        list.add(PopUpModel("2", "2022"))
        list.add(PopUpModel("3", "2023"))
        list.add(PopUpModel("4", "2024"))
        list.add(PopUpModel("5", "2025"))
        list.add(PopUpModel("6", "2026"))
        list.add(PopUpModel("7", "2027"))
        list.add(PopUpModel("8", "2028"))
        list.add(PopUpModel("9", "2029"))
        list.add(PopUpModel("10", "2030"))

        return list
    }

    fun monthList(): List<PopUpModel> {
        val list = ArrayList<PopUpModel>()
        list.add(PopUpModel("1", getString(R.string.january)))
        list.add(PopUpModel("2", getString(R.string.february)))
        list.add(PopUpModel("3", getString(R.string.march)))
        list.add(PopUpModel("4", getString(R.string.april)))
        list.add(PopUpModel("5", getString(R.string.may)))
        list.add(PopUpModel("6", getString(R.string.june)))
        list.add(PopUpModel("7", getString(R.string.july)))
        list.add(PopUpModel("8", getString(R.string.august)))
        list.add(PopUpModel("9", getString(R.string.september)))
        list.add(PopUpModel("10", getString(R.string.october)))
        list.add(PopUpModel("11", getString(R.string.november)))
        list.add(PopUpModel("12", getString(R.string.december)))

        return list
    }

    fun photoNameList(): List<PopUpModel> {
        val list = ArrayList<PopUpModel>()
        list.add(PopUpModel("1", getString(R.string.gallery)))
        list.add(PopUpModel("2", getString(R.string.whats_app_images)))
        list.add(PopUpModel("3", getString(R.string.screenshot)))

        return list
    }
}
