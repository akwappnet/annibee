package com.devstree.annibee.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityResetPasswordBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Validator

class ResetPasswordActivity : NavigationActivity() {

    lateinit var binding: ActivityResetPasswordBinding
    var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolBar(R.string.reset_password, true)
        id = intent.getLongExtra("id", 0)
    }

    fun onClick(v: View?) {
        when (v) {
            binding.btnSubmit -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        resetPassword()
                    }else {
                        noNetWorkAvailable()
                    }
                }
            }
        }
    }

    private fun resetPassword() {
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["id"] = id
        params["password"] = binding.edtNewPassword.text.toString()

        NetworkCall.resetPassword(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    DialogHelper.newInstance(message, object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            startActivity(
                                Intent(
                                    this@ResetPasswordActivity,
                                    LoginActivity::class.java
                                )
                            )
                            finishAffinity()
                        }
                    }).show(this@ResetPasswordActivity)
                } else {
                    DialogHelper.newInstance(message).show(this@ResetPasswordActivity)
                }
            }
        })
    }

    private fun validate(): Boolean {
        if (Validator.isEmptyFieldValidate(binding.edtNewPassword.text.toString())) {
            Validator.setError(binding.edtNewPassword, getString(R.string.please_enter_password))
            return false
        }

        if (!Validator.isPasswordValidate(binding.edtNewPassword.text.toString())) {
            Validator.setError(
                binding.edtNewPassword,
                getString(R.string.please_enter_8_char_password)
            )
            return false
        }

        if (binding.edtConfirmPassword.text.toString() != binding.edtNewPassword.text.toString()) {
            Validator.setError(binding.edtConfirmPassword, getString(R.string.password_not_match))
            return false
        }

        return true
    }
}