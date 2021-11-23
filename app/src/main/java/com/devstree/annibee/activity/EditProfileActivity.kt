package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import com.bumptech.glide.Glide
import com.devstree.annibee.BuildConfig
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityEditProfileBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Validator
import com.devstree.mediafilepicker.bottomsheet.BottomSheetFilePicker
import com.devstree.mediafilepicker.listener.MediaPickerCallback
import com.devstree.mediafilepicker.model.Media
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : NavigationActivity(), DatePickerDialog.OnDateSetListener,
    View.OnClickListener {

    lateinit var binding: ActivityEditProfileBinding
    var profilePhoto: String? = null
    private var socialAuthId: String? = null

    lateinit var user: User
    private var media: Media? = null

    var date: Int? = 0
    var month: Int? = 0
    var year: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {

        socialAuthId = intent.getStringExtra("user")

        binding.lnlMain.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })

        if (socialAuthId.isNullOrEmpty()) {
            setUpToolBar(getString(R.string.edit_profile), true)
        } else {
            setUpToolBar(getString(R.string.setup_profile), true)
            binding.edtToolbar.imgBack.visibility = View.GONE
        }
        binding.edtToolbar.imgSave.visibility = View.VISIBLE
        binding.edtToolbar.imgSave.setOnClickListener(this)

        binding.edtBirthday.setOnClickListener {
            if (date == 0) {
                openDatePickerDialog(
                    this,
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.YEAR),
                    true
                )
            } else openDatePickerDialog(this, date!!, month!!, year!!, true)
        }

        user = AppHelper.getUserDetail()!!

        val month = SimpleDateFormat("MM", Locale.ENGLISH)
        val day = SimpleDateFormat("dd", Locale.ENGLISH)
        val year = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        if (user.birthday?.isNotEmpty() == true) {
            val date = sdf.parse(user.birthday!!)

            this.month = month.format(date!!).toInt() - 1
            this.date = day.format(date).toInt()
            this.year = year.format(date).toInt()

        }
        profilePhoto = user.image

        binding.edtName.setText(user.userName)
        binding.edtUserId.setText(user.name)
        binding.edtBirthday.setText(user.birthday)
        binding.edtEmail.setText(user.email)
        binding.imgUser.setUrl(profilePhoto)

    }



    override fun onClick(v: View) {
        when (v) {
            binding.edtToolbar.imgSave -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        updateProfile()
                    } else {
                        noNetWorkAvailable()
                    }
                }
            }
            binding.txtChangePhoto, binding.imgUser -> {
                uploadPhoto()
            }
        }
    }

    private fun updateProfile() {

        showProgressDialog()
        val params = HashMap<String, RequestBody?>()
        params["name"] = binding.edtUserId.text.toString().toRequestBody()
        params["user_name"] = binding.edtName.text.toString().toRequestBody()
        params["email"] = binding.edtEmail.text.toString().toRequestBody()
        params["birthdate"] = binding.edtBirthday.text.toString().toRequestBody()
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

        NetworkCall.updateProfile(
            params,
            media?.localThumbFile,
            object : BaseResponseListener<ResponseBody<User>>() {
                override fun result(response: ResponseBody<User>?) {
                    hideProgressDialog()
                    if (success) {

//                        if (user.birthday.toString() != binding.edtBirthday.text.toString()) anniversary()

                        val user = response?.data
                        user?.token = AppHelper.getAccessToken()
                        AppHelper.setUserDetail(user)
                        if (socialAuthId != null) {
                            AppHelper.setLogin(true)
                            val intent = Intent(this@EditProfileActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        } else {
                            if (user?.emailOtpVerified == "0") {
                                val intent = Intent(
                                    this@EditProfileActivity,
                                    VerificationCodeActivity::class.java
                                )
                                intent.putExtra("user", user)
                                startActivityForResult(intent, CHANGE_EMAIL)
                            } else {
                                val intent = Intent()
                                intent.putExtra("user", user)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }
                    } else {
                        unAuthorized(code, message.toString())
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHANGE_EMAIL) {
            setResult(RESULT_OK)
            finish()
        }
    }

    /* private fun anniversary() {
         showProgressDialog()
         val params = HashMap<String, RequestBody?>()
         params["name"] = getString(R.string.birthday).toRequestBody()
         params["date"] = binding.edtBirthday.text.toString().toRequestBody()
         params["lang_code"] = when (AppHelper.getAppLanguage()) {
             Constants.ENGLISH -> {
                 "en".toRequestBody()
             }
             Constants.JAPANESE -> {
                 "ja".toRequestBody()
             }
             Constants.TRADITIONAL_CHINESE -> {
                 "zh".toRequestBody()
             }
             Constants.SIMPLIFIED_CHINESE -> {
                 "zh".toRequestBody()
             }
             else -> {
                 "en".toRequestBody()
             }
         }

         val photoList = arrayOfNulls<MultipartBody.Part>(imageList.size)

         for (index in 0 until imageList.size) {

             val file = File(
                 imageList[index]
             )
             val surveyBody = file
                 .asRequestBody("*".toMediaTypeOrNull())
             photoList[index] = MultipartBody.Part.createFormData(
                 "anniversary_photo[]",
                 file.name,
                 surveyBody
             )

         }

         createAnniversary(params, photoList, list = arrayOfNulls(0))

     }

     private fun createAnniversary(
         params: HashMap<String, RequestBody?>,
         photoList: Array<MultipartBody.Part?>,
         list: Array<Long?>
     ) {
         NetworkCall.createAnniversary(
             params,
             photoList,
             list,
             object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                 override fun result(response: ResponseBody<AnniversaryEvent>?) {
                     hideProgressDialog()
                     if (!success) DialogHelper.newInstance(message).show(this@EditProfileActivity)
                 }
             })
     }*/

    private fun validate(): Boolean {

        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtName.text.toString())) {
            Validator.setError(binding.edtName, getString(R.string.please_enter_name))
            isValid = false
        }
        if (!Validator.isEmptyFieldValidate(binding.edtUserId.text.toString())) {
            if (!Validator.isUserIdValidate(binding.edtUserId.text.toString())) {
                Validator.setError(
                    binding.edtUserId,
                    getString(R.string.please_enter_valid_user_id)
                )
                isValid = false
            }
        }
        if (Validator.isEmptyFieldValidate(binding.edtEmail.text.toString())) {
            Validator.setError(binding.edtEmail, getString(R.string.please_enter_email))
            isValid = false
        }
        if (!Validator.isValidEmail(binding.edtEmail.text.toString())) {
            Validator.setError(binding.edtEmail, getString(R.string.please_enter_valid_email))
            isValid = false
        }
        /*if (Validator.isEmptyFieldValidate(binding.edtPassword.text.toString())) {
            Validator.setError(binding.edtPassword, getString(R.string.please_enter_password))
            isValid = false
        }*/

        if (!isValid) return isValid

        if (Validator.isEmptyFieldValidate(binding.edtBirthday.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_enter_birthday)).show(this)
            isValid = false
        }
        /* if (profilePhoto == null) {
             DialogHelper.newInstance(getString(R.string.select_profile_img)).show(this)
             isValid = false
         }*/
        return isValid
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        calendar.set(year, month, dayOfMonth)
        val dateString: String = sdf.format(calendar.time)
        binding.edtBirthday.setText(dateString)

        this.date = dayOfMonth
        this.month = month
        this.year = year
    }

    private fun uploadPhoto() {
        val bottomSheetFilePicker = BottomSheetFilePicker(BuildConfig.APPLICATION_ID)

        bottomSheetFilePicker.actionButtonBg = R.drawable.button_bg_rounded
        bottomSheetFilePicker.cancelButtonBg = R.drawable.button_red_bg_rounded
        bottomSheetFilePicker.actionButtonTextColor = R.color.colorPrimary
        bottomSheetFilePicker.cancelButtonTextColor = R.color.white

        bottomSheetFilePicker.setMediaListenerCallback(
            BottomSheetFilePicker.IMAGE,
            object : MediaPickerCallback {
                override fun onPickedSuccess(media: Media?) {
                    this@EditProfileActivity.media = media
                    if (media != null) {
                        profilePhoto = media.url
                        Glide.with(this@EditProfileActivity).asBitmap().load(media.url)
                            .into(binding.imgUser)
                    }
                }

                override fun onPickedError(error: String?) {

                }

                override fun showProgressBar(enable: Boolean) {

                }
            })

        bottomSheetFilePicker.show(supportFragmentManager, "image")
    }

    override fun onBackPressed() {
        if (socialAuthId != null) {
            AppHelper.setLogin(false)
        }
        super.onBackPressed()
    }

    companion object {
        const val CHANGE_EMAIL = 1001
    }
}