package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityHomeBinding
import com.devstree.annibee.fragment.DateFragment
import com.devstree.annibee.fragment.HomeFragment
import com.devstree.annibee.fragment.ListFragment
import com.devstree.annibee.fragment.ProfileFragment
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Log
import com.google.android.material.bottomsheet.BottomSheetDialog


class HomeActivity : NavigationActivity(), View.OnClickListener {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initUi() {
        setUpToolBar(getString(R.string.annibee), true)
        binding.homeToolbar.imgBack.visibility = View.GONE
        binding.homeToolbar.imgLogo.visibility = View.VISIBLE
        binding.homeToolbar.imgSearch.visibility = View.VISIBLE
        binding.homeToolbar.imgBack.setOnClickListener { }

        if (!isNetworkAvailable(this)) {
            noNetWorkAvailable()
        }

        if (!intent.getStringExtra("language").isNullOrEmpty()) {
            setToolbar()
            binding.account.isChecked = true
            binding.homeToolbar.txtToolbarTitle.text = getString(R.string.profile)
            binding.homeToolbar.rlBell.visibility = View.VISIBLE
            //binding.homeToolbar.imgMenu.visibility = View.VISIBLE
            loadFragment(ProfileFragment(),Constants.PROFILE_FRAGMENT)
        } else {
            loadFragment(HomeFragment(),Constants.HOME_FRAGMENT)
            binding.home.isChecked = true
        }



//        binding.container.setOnTouchListener(object : OnSwipeTouchListener(this) {
//
//            override fun onSwipeLeft() {
//                super.onSwipeLeft()
//                showAlertMessage("onSwipeLeft")
//            }
//
//            override fun onSwipeBottom() {
//                super.onSwipeBottom()
//                showAlertMessage("onSwipeBottom")
//            }
//
//            override fun onSwipeRight() {
//                super.onSwipeRight()
//                showAlertMessage("onSwipeRight")
//            }
//
//            override fun onSwipeTop() {
//                super.onSwipeTop()
//                showAlertMessage("onSwipeTop")
//            }
//
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                showAlertMessage("onTouch")
//                return super.onTouch(v, event)
//
//            }
//        })


    }

//    override fun onBackPressed() {
//       // super.onBackPressed()
//        val manager: FragmentManager = supportFragmentManager
//        if(manager != null && manager.fragments !=null){
//            if(manager.fragments.size == 1){
//                if(manager.fragments[0].tag == Constants.HOME_FRAGMENT){
//                    super.onBackPressed()
//                }else{
//                    setHomeFragment()
//                }
//            }else{
//                var fragmentName =   manager.fragments[0].tag
//                if(fragmentName != null && fragmentName.contains(".")){
//                    fragmentName =   manager.fragments[1].tag
//                }
//                if(fragmentName == Constants.HOME_FRAGMENT){
//                    super.onBackPressed()
//                }else{
//                    setHomeFragment()
//                }
//            }
//        }else{
//            super.onBackPressed()
//        }
//    }


    private fun setHomeFragment(){
        setToolbar()
        binding.homeToolbar.imgBack.visibility = View.GONE
        binding.homeToolbar.imgLogo.visibility = View.VISIBLE
        binding.homeToolbar.txtToolbarTitle.text = getString(R.string.annibee)
        binding.homeToolbar.imgSearch.visibility = View.VISIBLE
        binding.homeToolbar.imgBack.setOnClickListener {

        }
        Controller.instance.listPopupWindow.dismiss()
        loadFragment(HomeFragment(),Constants.HOME_FRAGMENT)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.home -> {
                setHomeFragment()
            }
            binding.date -> {
                setToolbar()
                binding.date.isChecked = true
                binding.homeToolbar.txtToolbarTitle.visibility = View.GONE
                binding.homeToolbar.spinner.visibility = View.VISIBLE
                binding.homeToolbar.spinnerYear.visibility = View.VISIBLE
                Controller.instance.listPopupWindow.dismiss()
                loadFragment(DateFragment(),Constants.DATE_FRAGMENT)
            }
            binding.add -> {
                Controller.instance.listPopupWindow.dismiss()
                openBottomSheet()
            }
            binding.list -> {
                setToolbar()
                binding.list.isChecked = true
                binding.homeToolbar.toolbar.visibility = View.GONE
//                binding.homeToolbar.spinner.visibility = View.VISIBLE
                Controller.instance.listPopupWindow.dismiss()
                loadFragment(ListFragment(),Constants.LIST_FRAGMENT)
            }
            binding.account -> {
                setToolbar()
                binding.account.isChecked = true
                binding.homeToolbar.txtToolbarTitle.text = getString(R.string.profile)
                binding.homeToolbar.rlBell.visibility = View.VISIBLE
                //binding.homeToolbar.imgMenu.visibility = View.VISIBLE
                Controller.instance.listPopupWindow.dismiss()
                loadFragment(ProfileFragment(),Constants.PROFILE_FRAGMENT)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheet() {
        val mBottomSheetDialog = BottomSheetDialog(this, R.style.DialogStyle)
        val sheetView: View = layoutInflater.inflate(R.layout.bottom_sheet_create_anni_event, null)

        val createAnni = sheetView.findViewById<TextView>(R.id.txtCreateAnni)
        val createEvent = sheetView.findViewById<TextView>(R.id.txtCreateEvent)

        createAnni.setOnClickListener {
            openCreateAnniversaryActivity()
            mBottomSheetDialog.dismiss()
        }

        createEvent.setOnClickListener {
            openCreateEventActivity()
            mBottomSheetDialog.dismiss()
        }

        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    private fun setToolbar() {
        binding.homeToolbar.toolbar.visibility = View.VISIBLE
        binding.homeToolbar.imgBack.visibility = View.GONE
        binding.homeToolbar.imgLogo.visibility = View.GONE
        binding.homeToolbar.txtToolbarTitle.visibility = View.VISIBLE
        binding.homeToolbar.edtSearch.visibility = View.GONE
        binding.homeToolbar.imgSearch.visibility = View.GONE
        binding.homeToolbar.imgFilter.visibility = View.GONE
        binding.homeToolbar.rlBell.visibility = View.GONE
        binding.homeToolbar.imgMenu.visibility = View.GONE
        binding.homeToolbar.spinner.visibility = View.GONE
        binding.homeToolbar.spinnerYear.visibility = View.GONE
    }
}