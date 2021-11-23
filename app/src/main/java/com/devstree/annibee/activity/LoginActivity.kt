package com.devstree.annibee.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityLoginBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Validator
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson

class LoginActivity : NavigationActivity() {

    lateinit var binding: ActivityLoginBinding
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolBar(getString(R.string.login), true)


    }

    fun onClick(v: View) {
        when (v) {
            binding.btnLogin -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        login(binding.edtEmail.text.toString(), null, null)
                    } else {
                        noNetWorkAvailable()
                    }
                }
            }
            binding.txtSignUp -> {
                openSignUpActivity()
            }
            binding.txtForgotPassword -> {
                openForgotPasswordActivity()
            }
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
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        /*Controller.instance.googleLogin.logIn(this, object : GoogleLoginResponse {

            override fun onSuccess(responseData: GoogleLoginResponseData) {
                 get the user account detail


            }

            override fun onFailure(message: String) {
                 handle sign in error
                showToast(message)
            }
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK && data != null) {
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
//        Controller.instance.googleLogin.onActivityResult(requestCode, resultCode, data)
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

        Log.e("login: ", Gson().toJson(params))

        NetworkCall.login(params, object : BaseResponseListener<ResponseBody<User>>() {
            override fun result(response: ResponseBody<User>?) {
                hideProgressDialog()
                if (success) {
                    AppHelper.setUserDetail(response?.data)
                    AppHelper.setLogin(true)
                    if (!socialAuthId.isNullOrEmpty()) {
                        val intent = Intent(this@LoginActivity, EditProfileActivity::class.java)
                        intent.putExtra("user", socialAuthId)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                } else {
                    DialogHelper.newInstance(message).show(this@LoginActivity)
                    Controller.instance.mGoogleSignInClient?.signOut()
                }
            }

        })
    }

    private fun validate(): Boolean {

        var isValid = true

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

        return isValid
    }

    companion object {
        const val RC_SIGN_IN = 10101
    }
}