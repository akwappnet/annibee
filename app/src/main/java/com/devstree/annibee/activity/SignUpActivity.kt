package com.devstree.annibee.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivitySignUpBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Validator
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import java.util.*

class SignUpActivity : NavigationActivity() {

    lateinit var binding: ActivitySignUpBinding
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initUi() {
        setUpToolBar(getString(R.string.sign_up), true)

        binding.txtTerms.movementMethod = LinkMovementMethod.getInstance()
//        binding.txtTerms.text = Html.fromHtml(getString(R.string.terms_of_use), 1001)
    }

    fun onClick(v: View) {
        when (v) {
            binding.btnSignUp -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        registerUser()
                    } else {
                        noNetWorkAvailable()
                    }
                }
            }
            binding.txtLogin -> {
                openLoginActivity()
            }
            /*binding.txtTerms -> {
                val intent = Intent(this, WebActivity::class.java)
                if (AppHelper.getAppLanguage() == Constants.ENGLISH) {
                    intent.putExtra("url", Constants.TERMS)
                } else {
                    intent.putExtra("url", Constants.TERMS_JAPANESE)
                }
                startActivityForResult(intent, 1001)
            }
            binding.txtPrivacy -> {
                val intent = Intent(this, WebActivity::class.java)
                if (AppHelper.getAppLanguage() == Constants.ENGLISH) {
                    intent.putExtra("url", Constants.PRIVACY_POLICY)
                } else {
                    intent.putExtra("url", Constants.PRIVACY_POLICY_JAPANESE)
                }
                startActivityForResult(intent, 1002)
            }*/
            binding.llGoogleLogin, binding.btnGoogle -> {
                if (isNetworkAvailable(this)) {
                    googleLogin()
                } else {
                    noNetWorkAvailable()
                }
            }
            binding.llFbLogin, binding.btnFB -> {
                if (isNetworkAvailable(this)) {
                    fbLogin()
                } else {
                    noNetWorkAvailable()
                }
            }
        }
    }

    private fun fbLogin() {
//        FacebookSdk.sdkInitialize(this)
        callbackManager = CallbackManager.Factory.create()


        Controller.instance.loginManager.logInWithReadPermissions(
            this,
            listOf("public_profile", "email")
        )

        Controller.instance.loginManager.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    val request =
                        GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                            if (`object` != null) {
                                try {
                                    val name = `object`.getString("name")
                                    val email = `object`.getString("email")
                                    val fbUserID = `object`.getString("id")
//                            val birthday = `object`.getString("birthday")

                                    com.devstree.annibee.utility.Log.e("$name, $email, $fbUserID")

                                    login(email, "3", fbUserID)

                                    // do action after Facebook login success
                                    // or call your API
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id, name, email, gender, birthday"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Log.e("LoginScreen", "---onCancel")
                }

                override fun onError(error: FacebookException) {
                    // here write code when get error
                    Log.e("LoginScreen", "----onError: " + error.message)
                }
            })

    }

    private fun googleLogin() {

        val accountData: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (accountData != null) {
            showProgressDialog()

            Log.e("handleSignInResult: ", accountData.id!!)
            Log.e("handleSignInResult: ", accountData.displayName!!)
            Log.e("handleSignInResult: ", accountData.email!!)
            Log.e("handleSignInResult: ", accountData.photoUrl.toString())
            login(accountData.email!!, "1", accountData.id)

        } else {
            val signInIntent = Controller.instance.mGoogleSignInClient?.signInIntent
            if (signInIntent != null) {
                startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
            }
        }

        /*Controller.instance.googleLogin.logIn(this, object : GoogleLoginResponse {

            override fun onSuccess(responseData: GoogleLoginResponseData) {
                 get the user account detail
                showProgressDialog()
                Log.e("handleSignInResult: ", responseData.loginId)
                Log.e("handleSignInResult: ", responseData.name)
                Log.e("handleSignInResult: ", responseData.email)
                Log.e("handleSignInResult: ", responseData.profilePic)
                login(responseData)
            }

            override fun onFailure(message: String) {
                 handle sign in error
            }
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == RC_SIGN_IN_GOOGLE && resultCode == RESULT_OK && data != null) {
                val account =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException::class.java)
                if (account != null) {
                    login(account.email!!, "1", account.id)
                }
            }
        } catch (e: ApiException) {
            toast(e.message)
        }

        /*if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CHANGE_PASSWORD -> {
                    AppHelper.setLogin(true)
                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }*/
    }

    private fun login(email: String, socialType: String?, socialAuthId: String?) {
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["email"] = email
        params["password"] = binding.edtPassword.text.toString()
        params["device_type"] = "2"
        params["device_id"] = /*Utils.getDeviceID(this)*/ AppHelper.getFcmToken()
        params["social_auth_id"] = socialAuthId
        params["social_type"] = socialType

        NetworkCall.login(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    AppHelper.setUserDetail(response?.data)
                    AppHelper.setLogin(true)
                    val intent = Intent(this@SignUpActivity, EditProfileActivity::class.java)
                    intent.putExtra("user", socialAuthId)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    DialogHelper.newInstance(message).show(this@SignUpActivity)
                    Controller.instance.mGoogleSignInClient?.signOut()
                }
            }

        })
    }

    private fun registerUser() {
        showProgressDialog()

        val params = AppHelper.getDefaultParam()
        params["name"] = binding.edtUserId.text.toString()
        params["email"] = binding.edtEmail.text.toString()
        params["password"] = binding.edtPassword.text.toString()
        params["user_name"] = binding.edtUserName.text.toString()
        params["device_type"] = "2"
        params["device_id"] = /*Utils.getDeviceID(this)*/ AppHelper.getFcmToken()


        NetworkCall.register(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    hideProgressDialog()
                    AppHelper.setUserDetail(response?.data)
                    DialogHelper.newInstance(message, object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            val intent =
                                Intent(this@SignUpActivity, VerificationCodeActivity::class.java)
                            intent.putExtra("email", binding.edtEmail.text.toString())
                            intent.putExtra("verifyCode", "newUser")
                            startActivity(intent)
                        }
                    }).show(this@SignUpActivity)

                } else {
                    DialogHelper.newInstance(message).show(this@SignUpActivity)
                }
            }

        })
    }

    private fun validate(): Boolean {

        var isValid = true

        if (!Validator.isEmptyFieldValidate(binding.edtUserId.text.toString())) {
            if (!Validator.isUserIdValidate(binding.edtUserId.text.toString())) {
                Validator.setError(
                    binding.edtUserId,
                    getString(R.string.please_enter_valid_user_id)
                )
                isValid = false
            }
        }

        if (Validator.isEmptyFieldValidate(binding.edtUserName.text.toString())) {
            Validator.setError(binding.edtUserName, getString(R.string.please_enter_user_name))
            isValid = false
        }

        if (Validator.isEmptyFieldValidate(binding.edtEmail.text.toString())) {
            Validator.setError(binding.edtEmail, getString(R.string.please_enter_email))
            isValid = false
        }
        if (!Validator.isValidEmail(binding.edtEmail.text.toString())) {
            Validator.setError(binding.edtEmail, getString(R.string.please_enter_valid_email))
            isValid = false
        }
        if (Validator.isEmptyFieldValidate(binding.edtPassword.text.toString())) {
            Validator.setError(binding.edtPassword, getString(R.string.please_enter_password))
            isValid = false
        }
        if (!Validator.isPasswordValidate(binding.edtPassword.text.toString())) {
            Validator.setError(
                binding.edtPassword,
                getString(R.string.please_enter_8_char_password)
            )
            isValid = false
        }
        if (binding.edtReenterPassword.text.toString() != binding.edtPassword.text.toString()) {
            Validator.setError(binding.edtReenterPassword, getString(R.string.password_not_match))
            isValid = false
        }

        if (!isValid) return isValid

        if (!binding.checkbox.isChecked) {
            DialogHelper.newInstance(getString(R.string.please_check_the_user_policy_and_privacy_policy))
                .show(this)
            isValid = false
        }

        return isValid
    }

    companion object {
        const val REQUEST_CHANGE_PASSWORD = 1003
        const val RC_SIGN_IN_GOOGLE = 1001
        const val RC_SIGN_IN_FB = 1002
    }
}