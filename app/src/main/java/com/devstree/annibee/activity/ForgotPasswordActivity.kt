package com.devstree.annibee.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityForgotPasswordBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Validator

class ForgotPasswordActivity : NavigationActivity(), View.OnClickListener {

    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolBar(getString(R.string.forgot_password), true)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSubmit -> {
                if (Validator.isEmptyFieldValidate(binding.edtEmail.text.toString())) {
                    Validator.setError(binding.edtEmail, getString(R.string.please_enter_email))
                    return
                }
                if (!Validator.isValidEmail(binding.edtEmail.text.toString())) {
                    Validator.setError(
                        binding.edtEmail,
                        getString(R.string.please_enter_valid_email)
                    )
                    return
                }

                if (isNetworkAvailable(this)) {
                    forgotPassword()
                } else {
                    noNetWorkAvailable()
                }
            }
        }
    }

    private fun forgotPassword() {
        showProgressDialog()

        val params = AppHelper.getDefaultParam()
        params["email"] = binding.edtEmail.text.toString()

        NetworkCall.forgotPassword(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    DialogHelper.newInstance(message, object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            val intent = Intent(
                                this@ForgotPasswordActivity,
                                VerificationCodeActivity::class.java
                            )
                            intent.putExtra("email", binding.edtEmail.text.toString())
                            intent.putExtra("verifyCode", "forgotCode")
                            startActivity(intent)
                            finish()
                        }

                    }).show(this@ForgotPasswordActivity)
                } else {
                    DialogHelper.newInstance(message).show(this@ForgotPasswordActivity)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1001 -> {

                }
            }
        }
    }

    companion object {
        const val REQUEST_CHANGE_PASSWORD = 1001
    }
}