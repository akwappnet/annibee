package com.devstree.annibee.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.activity.*
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentProfileBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.User
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.PreferenceManager
import com.devstree.annibee.utility.SharedPrefConstant
import java.util.*

class ProfileFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentProfileBinding

    lateinit var user: User
    private var notificationCount: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment



        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUi()

    }

    override fun onStart() {
        super.onStart()
        notificationCount = PreferenceManager.getString(SharedPrefConstant.NOTIFICATION_COUNTER)
        if(notificationCount != null && Integer.parseInt(notificationCount) > 0){
            (activity as HomeActivity).binding.homeToolbar.txtNotificationCount.visibility = View.VISIBLE
            (activity as HomeActivity).binding.homeToolbar.txtNotificationCount.text = notificationCount
        }else{
            (activity as HomeActivity).binding.homeToolbar.txtNotificationCount.visibility = View.GONE
        }
    }
    private fun initUi() {
        binding.listener = this

        (activity as HomeActivity).binding.homeToolbar.imgBell.setOnClickListener(this)
        (activity as HomeActivity).binding.homeToolbar.imgMenu.setOnClickListener(this)

        if (navigation?.isNetworkAvailable(context) == true) {
            getUserProfile()
        } else {
            navigation?.noNetWorkAvailable()
        }
    }

    private fun getUserProfile() {

        navigation?.showProgressDialog()
        NetworkCall.getUserProfile(
            AppHelper.getDefaultParam(),
            object : BaseResponseListener<ResponseBody<User>>() {
                override fun result(response: ResponseBody<User>?) {
                    navigation?.hideProgressDialog()
                    if (success) {
                        AppHelper.setUserDetail(response?.data)

                        binding.imgUser.setUrl(response?.data?.image)
                        binding.txtName.text = response?.data?.name
                        binding.txtUserName.text = response?.data?.userName
                        binding.txtUserBirthDate.text = response?.data?.birthday
                        binding.txtAnniversary.text = response?.data?.totalAnniversary.toString()
                        binding.txtEvent.text = response?.data?.totalEvent.toString()

                    } else {
                        base?.unAuthorized(code, message.toString())
                    }
                }
            })
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnEditProfile -> {
                val intent = Intent(context, EditProfileActivity::class.java)
                startActivityForResult(intent, EDIT_PROFILE)
            }
            (activity as HomeActivity).binding.homeToolbar.imgBell -> {
                (activity as HomeActivity).openNavigationActivity()
            }
            (activity as HomeActivity).binding.homeToolbar.imgMenu -> {

            }
            binding.llRegisterAnniversary -> {
                val intent = Intent(context, AnniversaryListActivity::class.java)
                startActivity(intent)
            }
            binding.llRegisterEvent -> {
                val intent = Intent(context, EventListActivity::class.java)
                startActivity(intent)
            }
            binding.btnSwitch -> {
//                binding.btnSwitch.isChecked =
//                    false.takeIf { binding.btnSwitch.isChecked } ?: true

                if (binding.btnSwitch.isChecked) {
                    setNotificationSetting("1")
                } else {
                    setNotificationSetting("0")
                }
            }
            binding.llPrivacyPolicy -> {
                val intent = Intent(context, WebActivity::class.java)
                when (AppHelper.getAppLanguage()) {
                    Constants.ENGLISH -> {
                        intent.putExtra("url", Constants.PRIVACY_POLICY)
                    }
                    Constants.JAPANESE -> {
                        intent.putExtra("url", Constants.PRIVACY_POLICY_JAPANESE)
                    }
                    Constants.TRADITIONAL_CHINESE -> {
                        intent.putExtra("url", Constants.PRIVACY_POLICY_JAPANESE)
                    }
                    Constants.SIMPLIFIED_CHINESE -> {
                        intent.putExtra("url", Constants.PRIVACY_POLICY_JAPANESE)
                    }
                }
                startActivity(intent)
            }
            binding.llTermsOfUse -> {
                val intent = Intent(context, WebActivity::class.java)
                when (AppHelper.getAppLanguage()) {
                    Constants.ENGLISH -> {
                        intent.putExtra("url", Constants.TERMS)
                    }
                    Constants.JAPANESE -> {
                        intent.putExtra("url", Constants.TERMS_JAPANESE)
                    }
                    Constants.TRADITIONAL_CHINESE -> {
                        intent.putExtra("url", Constants.TERMS_JAPANESE)
                    }
                    Constants.SIMPLIFIED_CHINESE -> {
                        intent.putExtra("url", Constants.TERMS_JAPANESE)
                    }
                }
                startActivity(intent)
            }
            binding.llFAQ -> {
                val intent = Intent(context, FaqActivity::class.java)
                startActivity(intent)
            }
            binding.llReportBugs -> {
                val intent = Intent(context, ReportBugActivity::class.java)
                startActivity(intent)
            }
            binding.llChangeLanguage -> {
                val intent = Intent(context, ChangeLanguageActivity::class.java)
                startActivityForResult(intent, CHANGE_LANGUAGE)
            }
            binding.llChangePassword -> {
                val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            binding.llLogout -> {
                logout()
            }
            binding.llDeleteAccount -> {
                deleteAccount()
            }
            binding.llShare -> {
                (activity as HomeActivity).shareApplication()
            }
        }
    }

    private fun setNotificationSetting(isNotification: String) {
        val params = AppHelper.getDefaultParam()
        params["is_notification"] = isNotification

        NetworkCall.notificationSetting(
            params,
            object : BaseResponseListener<ResponseBody<User>>() {
                override fun result(response: ResponseBody<User>?) {
                    if (success) {
                        DialogHelper.newInstance(message).show(activity!!)
                    } else {
                        DialogHelper.newInstance(message).show(activity!!)
                    }
                }

            })
    }

    private fun logout() {

        DialogHelper.newInstance(
            (getString(R.string.are_you_sure_n_you_want_to_logout)),
            (getString(R.string.logout)),
            (getString(R.string.cancel)), object : IDialogButtonClick {
                override fun onButtonClick(isPositive: Boolean) {
                    if (isPositive) {
                        if (navigation?.isNetworkAvailable(context) == true) {
                            navigation?.showProgressDialog()
                            NetworkCall.logout(
                                AppHelper.getDefaultParam(),
                                object : BaseResponseListener<ResponseBody<User>>() {
                                    override fun result(response: ResponseBody<User>?) {
                                        navigation?.hideProgressDialog()
                                        if (success) {
                                            AppHelper.setLogin(false)

                                            Controller.instance.mGoogleSignInClient?.signOut()
                                            Controller.instance.loginManager.logOut()

                                            val intent = Intent(context, LoginActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                        } else DialogHelper.newInstance(message).show(home!!)
                                    }

                                })
                        } else {
                            navigation?.noNetWorkAvailable()
                        }
                    }
                }

            }).show(requireActivity())
    }

    private fun deleteAccount() {

        DialogHelper.newInstance(
            (getString(R.string.are_you_sure_n_you_want_to_delete_your_account)),
            (getString(R.string.delete_account)),
            (getString(R.string.cancel)), object : IDialogButtonClick {
                override fun onButtonClick(isPositive: Boolean) {
                    if (isPositive) {
                        if (navigation?.isNetworkAvailable(context) == true) {
                            navigation?.showProgressDialog()
                            NetworkCall.deleteAccount(
                                AppHelper.getDefaultParam(),
                                object : BaseResponseListener<ResponseBody<User>>() {
                                    override fun result(response: ResponseBody<User>?) {
                                        navigation?.hideProgressDialog()
                                        if (success) {
                                            AppHelper.setLogin(false)

                                            Controller.instance.mGoogleSignInClient?.signOut()

                                            val intent = Intent(context, LoginActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                        } else DialogHelper.newInstance(message).show(home!!)
                                    }

                                })
                        } else {
                            navigation?.noNetWorkAvailable()
                        }
                    }
                }

            }).show(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            EDIT_PROFILE -> {
//                if (data != null) {

                user = AppHelper.getUserDetail()!!
                binding.txtName.text = user.name
                binding.txtUserName.text = user.userName
                binding.txtUserBirthDate.text = user.birthday
                binding.imgUser.setUrl(user.image)
//                }
            }

            CHANGE_LANGUAGE -> {
                if (data != null) {
                    val language = data.getStringExtra("language")

                    if (language != null) {
                        when (language) {
                            Constants.ENGLISH -> {
                                AppHelper.saveAppLanguage(Constants.ENGLISH)
                                Controller.instance.updateLocale(requireContext(), Locale.US)
                            }
                            Constants.JAPANESE -> {
                                AppHelper.saveAppLanguage(Constants.JAPANESE)
                                Controller.instance.updateLocale(requireContext(), Locale.JAPANESE)
                            }
                            Constants.TRADITIONAL_CHINESE -> {
                                AppHelper.saveAppLanguage(Constants.TRADITIONAL_CHINESE)
                                Controller.instance.updateLocale(
                                    requireContext(),
                                    Locale.TRADITIONAL_CHINESE
                                )
                            }
                            Constants.SIMPLIFIED_CHINESE -> {
                                AppHelper.saveAppLanguage(Constants.SIMPLIFIED_CHINESE)
                                Controller.instance.updateLocale(
                                    requireContext(),
                                    Locale.SIMPLIFIED_CHINESE
                                )
                            }
                        }
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.putExtra("language", "Changed")
                        startActivity(intent)
                        navigation?.finish()
//                        (activity as HomeActivity).loadFragment(ProfileFragment())
//                        (activity as HomeActivity).binding.homeToolbar.txtToolbarTitle.text = getString(R.string.profile)
                    }
                }
            }
        }
    }

    companion object {
        const val EDIT_PROFILE = 1001
        const val CHANGE_LANGUAGE = 1002
    }
}