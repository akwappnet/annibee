package com.devstree.annibee.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityVerificationCodeBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper

class VerificationCodeActivity : NavigationActivity(), View.OnClickListener {

    lateinit var binding: ActivityVerificationCodeBinding
    var email: String? = null
    var verifyCode: String? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolBar(getString(R.string.verification_code), true)
        email = intent.getStringExtra("email")
        verifyCode = intent.getStringExtra("verifyCode")
        user = intent.getParcelableExtra("user")
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSubmit -> {
                if (binding.edtCode.length() < 6) return
                if (isNetworkAvailable(this)) {
                    if (user == null) {
                        verifyOtp()
                    } else {
                        emailVerifyOtp()
                    }
                } else {
                    noNetWorkAvailable()
                }
            }
        }
    }

    private fun verifyOtp() {
        showProgressDialog()

        val params = AppHelper.getDefaultParam()
        params["email"] = email
        params["otp"] = binding.edtCode.text.toString()

        NetworkCall.verifyCode(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    if (verifyCode == "newUser") {
                        AppHelper.setLogin(true)
                        val intent = Intent(this@VerificationCodeActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        val intent =
                            Intent(this@VerificationCodeActivity, ResetPasswordActivity::class.java)
                        intent.putExtra("id", response?.data?.userId)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    DialogHelper.newInstance(message).show(this@VerificationCodeActivity)
                }
            }

        })
    }

    private fun emailVerifyOtp() {
        showProgressDialog()

        val params = AppHelper.getDefaultParam()
        params["email_otp"] = binding.edtCode.text.toString()

        NetworkCall.emailVerifyOtp(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    AppHelper.setUserDetail(response?.data)
                    DialogHelper.newInstance(message, object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }).show(this@VerificationCodeActivity)
                } else {
                    DialogHelper.newInstance(message).show(this@VerificationCodeActivity)
                }
            }

        })
    }
}