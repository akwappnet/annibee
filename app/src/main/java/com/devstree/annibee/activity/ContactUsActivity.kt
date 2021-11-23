package com.devstree.annibee.activity

import android.os.Bundle
import android.view.View
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityContactUsBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.ContactUs
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Validator

class ContactUsActivity : NavigationActivity() {

    lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolBar(getString(R.string.contact_us), true)
    }

    fun onClick(view: View?) {
        when (view) {
            binding.btnSubmit -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        callContactUs()
                    } else {
                        noNetWorkAvailable()
                    }
                }
            }
        }
    }

    private fun callContactUs() {
        showProgressDialog()

        val params = AppHelper.getDefaultParam()
        params["title"] = binding.edtTitle.text.toString()
        params["description"] = binding.edtMessage.text.toString()
        params["type"] = "1"

        NetworkCall.contactUs(params, object : BaseResponseListener<ResponseBody<ContactUs>>() {
            override fun result(response: ResponseBody<ContactUs>?) {
                hideProgressDialog()
                if (success) {
                    DialogHelper.newInstance(message, object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            finish()
                        }
                    }).show(this@ContactUsActivity)
                } else {
                    DialogHelper.newInstance(message).show(this@ContactUsActivity)
                }
            }

        })
    }

    private fun validate(): Boolean {
        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtTitle.text.toString())) {
            Validator.setError(binding.edtTitle, getString(R.string.enter_title))
            isValid = false
        }

        if (Validator.isEmptyFieldValidate(binding.edtMessage.text.toString())) {
            Validator.setError(binding.edtMessage, getString(R.string.enter_message))
            isValid = false
        }

        return isValid
    }
}