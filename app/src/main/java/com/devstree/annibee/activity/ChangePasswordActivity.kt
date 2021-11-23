package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityChangePasswordBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Log
import com.devstree.annibee.utility.Validator
import okhttp3.RequestBody.Companion.toRequestBody

class ChangePasswordActivity : NavigationActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    var navigation: NavigationActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(getString(R.string.change_password), true)
        binding.container.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun onClick(v: View) {
        when (v) {
            binding.btnLogin -> {
                if (validate()) {
                    Log.e("validate::: ${validate()}")
                    if (isNetworkAvailable(this)) {
                       changePassword()
                    } else {
                        noNetWorkAvailable()
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {

        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtOldPassword.text.toString())) {
            Validator.setError(binding.edtOldPassword, getString(R.string.please_enter_old_password))
            isValid = false
        }
        else if (Validator.isEmptyFieldValidate(binding.edtNewPassword.text.toString())) {
            Validator.setError(binding.edtNewPassword, getString(R.string.please_enter_new_password))
            isValid = false
        }

        else if (!Validator.isPasswordValidate(binding.edtOldPassword.text.toString())) {
            Validator.setError(
                binding.edtOldPassword,
                getString(R.string.please_enter_8_char_old_password)
            )
            isValid = false
        }
        else if (!Validator.isPasswordValidate(binding.edtNewPassword.text.toString())) {
            Validator.setError(
                binding.edtNewPassword,
                getString(R.string.please_enter_8_char_new_password)
            )
            isValid = false
        }

        else if(binding.edtNewPassword.text.toString() != binding.edtConfirmPassword.text.toString()){
            Validator.setError(
                binding.edtConfirmPassword,
                getString(R.string.password_not_match)
            )
            isValid = false
        }
        else if(binding.edtOldPassword.text.toString() == binding.edtNewPassword.text.toString()){
            Validator.setError(
                binding.edtNewPassword,
                getString(R.string.please_enter_different_password)
            )
            isValid = false
        }

        return isValid
    }

    private fun changePassword() {
        hideKeyBoard()
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["old_password"] = binding.edtOldPassword.text.toString()
        params["new_password"] = binding.edtNewPassword.text.toString()
        params["lang_code"] = when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                "en".toRequestBody()
            }
            Constants.JAPANESE -> {
                "ja".toRequestBody()
            }
            Constants.TRADITIONAL_CHINESE -> {
                "zh-Hant".toRequestBody()
            }
            Constants.SIMPLIFIED_CHINESE -> {
                "zh-Hans".toRequestBody()
            }
            else -> {
                "en".toRequestBody()
            }
        }

        NetworkCall.changePassword(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    DialogHelper.newInstance(getString(R.string.msg_password_changed), object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            onBackPressed()
                        }
                    }).show(this@ChangePasswordActivity)
                } else {
                    DialogHelper.newInstance(message).show(this@ChangePasswordActivity)
                }
            }
        })
    }
}