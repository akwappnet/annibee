package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.devstree.annibee.R
import com.devstree.annibee.adapter.AnniversaryViewPagerAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityAnniversaryDetailBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.fragment.AnniversaryDetailFragment
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.collections.ArrayList


class AnniversaryDetailActivity : NavigationActivity(), PopupMenu.OnMenuItemClickListener,
    View.OnClickListener {

    lateinit var binding: ActivityAnniversaryDetailBinding
    private var anniversaryId: Long? = 0
    private var id: Long? = 0
//    var adapter: AnniversaryEventPhotoAdapter? = null
//    val list = ArrayList<Event>()

//    private var peopleAdapter: PeopleAdapter? = null

    var anniversary: AnniversaryEvent? = null
//    private val peopleList = ArrayList<String>()

    //    private var recyclerViewPosition: Int = 0
    var currentPosition = 0

    val fragmentList = ArrayList<Fragment>()

    private var viewPagerAdapter: AnniversaryViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnniversaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adView()
    }

    override fun initUi() {
        setUpToolBar(getString(R.string.anniversary_detail), true)
        binding.detailToolbar.imgBack.setImageResource(R.drawable.close)
        binding.detailToolbar.imgMenu.visibility = View.VISIBLE

        id = intent.getLongExtra("anniversary_id", 0)
        anniversaryId = intent.getLongExtra("anniversary", 0)

        if (id != 0.toLong()) anniversaryId = id

//        binding.viewPager.isUserInputEnabled = false
        /*viewPagerAdapter = AnniversaryViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter*/

        /*adapter = AnniversaryEventPhotoAdapter(object : EventPhotoAdapter.ClickListener {
            override fun onItemClick(item: Event, position: Int) {
                val intent = Intent(this@AnniversaryDetailActivity, EventDetailActivity::class.java)
                intent.putExtra("event", item.id)
                startActivity(intent)
            }
        })
        binding.rvEvent.adapter = adapter

        binding.rvPeople.layoutManager = LinearLayoutManager(this)
        peopleAdapter = PeopleAdapter()
        binding.rvPeople.adapter = peopleAdapter

        binding.btnShowAll.setOnClickListener {
            showAllPeople()
        }*/

        binding.detailToolbar.imgMenu.setOnClickListener {
            showPopUpMenu(it)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
                manageClick()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(this)) getAnniversaryDetail(anniversaryId)
        else noNetWorkAvailable()
    }

    private fun adView() {
        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun getAnniversaryDetail(id: Long?) {
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["id"] = id

        NetworkCall.anniversaryDetail(
            params,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    if (success) {
                        anniversary = response?.data
                        setData(response?.data)

                        if (anniversary?.anniversaryList?.get(currentPosition)?.type == "3") {
                            binding.detailToolbar.imgMenu.visibility = View.GONE
                        } else {
                            binding.detailToolbar.imgMenu.visibility = View.VISIBLE
                        }
                    } else {
                        hideProgressDialog()

                        DialogHelper.newInstance(
                            message,
                            getString(R.string.ok),
                            object : IDialogButtonClick {
                                override fun onButtonClick(isPositive: Boolean) {
                                    finish()
                                }
                            }).show(this@AnniversaryDetailActivity)
                    }
                }

            })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: AnniversaryEvent?) {

        fragmentList.clear()

        if (data == null) return
        if (data.anniversaryList?.isEmpty() == true) return

        for (index in data.anniversaryList!!.indices) {
            if (data.id == data.anniversaryList!![index].id) {
                currentPosition = index
            }
            fragmentList.add(AnniversaryDetailFragment(data.anniversaryList!![index]))
        }

        viewPagerAdapter = AnniversaryViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter

        binding.viewPager.offscreenPageLimit = data.anniversaryList!!.size
        binding.viewPager.currentItem = currentPosition

        if (data.anniversaryList?.size == 1 || data.anniversaryList?.isEmpty() == true) {
            binding.btnNext.visibility = View.GONE
            binding.btnPrev.visibility = View.GONE
        } else {
            if (currentPosition >= 1) {
                binding.btnPrev.visibility = View.VISIBLE
            }
            binding.btnNext.visibility = View.VISIBLE
        }

        if (currentPosition == (anniversary?.anniversaryList?.size?.minus(1))) {
            binding.btnNext.visibility = View.GONE
        }

        if (currentPosition <= 0) {
            binding.btnPrev.visibility = View.GONE
        } else binding.btnPrev.visibility = View.VISIBLE

        hideProgressDialog()

    }

    private fun showPopUpMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.option_menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menuEdit -> {
                val intent1 = Intent(this, CreateAnniversaryActivity::class.java)
                intent1.putExtra("anniversary", anniversary?.anniversaryList?.get(currentPosition))
                startActivityForResult(intent1, REQUEST_UPDATE_ANNIVERSARY)
                true
            }
            R.id.menuDelete -> {
                DialogHelper.newInstance(
                    getString(R.string.are_you_sure_n_you_want_to_delete_this_anniversary),
                    (getString(R.string.delete)),
                    (getString(R.string.cancel)), object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            if (isPositive) {
                                deleteAnniversary(anniversary?.anniversaryList?.get(currentPosition)?.id)
                            }
                        }

                    }).show(this)
                true
            }
            /*  R.id.menuShare -> {
                  shareApplication()
                  true
              }*/
            else -> false
        }
    }

    private fun deleteAnniversary(id: Long?) {
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["id"] = id

        NetworkCall.deleteAnniversary(
            params,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {
                        fragmentList.removeAt(currentPosition)
                        viewPagerAdapter?.notifyItemRemoved(currentPosition)
                        currentPosition = (currentPosition - 1).takeIf { currentPosition != 0 }
                            ?: (currentPosition)
                        binding.viewPager.adapter = viewPagerAdapter
                        binding.viewPager.currentItem = currentPosition
                        manageClick()
//                        finish()
                    } else DialogHelper.newInstance(message).show(this@AnniversaryDetailActivity)
                }

            }
        )
    }

    override fun onClick(view: View?) {

        when (view) {
            /*binding.btnCreateNew -> {
                val intent = Intent(this, CreateEventActivity::class.java)
                intent.putExtra("anniversary", anniversary)
                startActivityForResult(intent, REQUEST_NEW_EVENT)
            }*/
            binding.btnNext -> {
                currentPosition += 1
                binding.viewPager.currentItem = currentPosition
                manageClick()
            }
            binding.btnPrev -> {
                currentPosition -= 1
                binding.viewPager.currentItem = currentPosition
                manageClick()
            }
        }
    }

    private fun manageClick() {

        anniversaryId =
            if (anniversary?.anniversaryList?.size!! > 0) anniversary?.anniversaryList?.get(currentPosition)?.id
            else anniversary?.id

        if (anniversary?.anniversaryList?.get(currentPosition)?.type == "3") {
            binding.detailToolbar.imgMenu.visibility = View.GONE
        } else {
            binding.detailToolbar.imgMenu.visibility = View.VISIBLE
        }

        if (currentPosition == 0 || currentPosition <= 0) {
            binding.btnPrev.visibility = View.GONE
        } else binding.btnPrev.visibility = View.VISIBLE

        if (currentPosition == fragmentList.size.minus(1)) {
            binding.btnNext.visibility = View.GONE

        } else binding.btnNext.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        for (fragment in supportFragmentManager.fragments) {
//            fragment.onActivityResult(requestCode, resultCode, data)
//        }
        val fragmentList = ArrayList<Fragment>()
        viewPagerAdapter = AnniversaryViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter
    }

    override fun onBackPressed() {
        if (id != 0.toLong()) {
            openHomeActivity()
        } else super.onBackPressed()
    }

    companion object {
        const val REQUEST_UPDATE_ANNIVERSARY = 1001
        const val REQUEST_NEW_EVENT = 1002
    }
}