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
import com.devstree.annibee.fragment.EventDetailFragment
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.util.*

class EventDetailActivity : NavigationActivity(), PopupMenu.OnMenuItemClickListener,
    View.OnClickListener {

    lateinit var binding: ActivityAnniversaryDetailBinding
    private var eventId: Long? = 0
    private var id: Long? = 0
//    val peopleList = ArrayList<String>()
//    var photoAdapter: PhotoAdapter? = null
//    var peopleAdapter: PeopleAdapter? = null

    val fragmentList = ArrayList<Fragment>()
    var event: AnniversaryEvent? = null
    var currentPosition = 0

    private var viewPagerAdapter: AnniversaryViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnniversaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adView()
    }

    override fun initUi() {
        setUpToolBar(getString(R.string.event_detail), true)
        binding.detailToolbar.imgBack.setImageResource(R.drawable.close)
        binding.detailToolbar.imgMenu.visibility = View.VISIBLE

        id = intent.getLongExtra("event_id", 0)
        eventId = intent.getLongExtra("event", 0)

        if (id != 0.toLong()) eventId = id

//        binding.viewPager.isUserInputEnabled = false
//        viewPagerAdapter = AnniversaryViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        binding.detailToolbar.imgMenu.setOnClickListener {
            showPopUpMenu(it)
        }

        if (isNetworkAvailable(this)) {
            getEventDetail(eventId)
        } else {
            noNetWorkAvailable()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
                manageClick().takeIf { event?.eventList?.size!! >= 1 }
            }
        })

    }

    private fun adView() {
        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun getEventDetail(id: Long?) {
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["id"] = id

        NetworkCall.eventDetail(
            params,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {
                        event = response?.data
                        setData(response?.data)
                    } else {
                        DialogHelper.newInstance(message, object : IDialogButtonClick {
                            override fun onButtonClick(isPositive: Boolean) {
                                finish()
                            }

                        }).show(this@EventDetailActivity)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: AnniversaryEvent?) {

        fragmentList.clear()

        if (data == null) return
        if (data.eventList?.isEmpty() == true) {
            fragmentList.add(EventDetailFragment(data))
        } else {
            for (index in data.eventList!!.indices) {
                if (data.id == data.eventList!![index].id) {
                    currentPosition = index
                }
                fragmentList.add(EventDetailFragment(data.eventList!![index]))
            }
        }

        viewPagerAdapter = AnniversaryViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter

        binding.viewPager.currentItem = currentPosition
        binding.viewPager.offscreenPageLimit =
            data.eventList!!.size.takeIf { data.eventList?.isNotEmpty() == true } ?: 1

        if (data.eventList?.size == 1 || data.eventList?.isEmpty() == true) {
            binding.btnNext.visibility = View.GONE
            binding.btnPrev.visibility = View.GONE
        } else {
            if (currentPosition >= 1) {
                binding.btnPrev.visibility = View.VISIBLE
            }
            binding.btnNext.visibility = View.VISIBLE
        }

        if (currentPosition == (event?.eventList?.size?.minus(1))) {
            binding.btnNext.visibility = View.GONE
        }

        if (currentPosition <= 0) {
            binding.btnPrev.visibility = View.GONE
        } else binding.btnPrev.visibility = View.VISIBLE


        /*if (event != null) {

            if (event.photos?.isNotEmpty() == true) binding.mainImage.setUrl(event.photos?.first()?.image)
            binding.txtEventName.text = event.name
            if (event.eventDates?.isNotEmpty() == true) {

                binding.txtDate.text =
                    "${event.eventDates?.first()?.eventDate} - ${event.eventDates?.last()?.eventDate}"
                if (event.isAllDay == "0") {
                    binding.txtTime.visibility = View.GONE
                    binding.txtTime.text =
                        "${event.eventDates?.first()?.eventTime} - ${event.eventDates?.last()?.eventTime}"
                }
            }

            if (event.notifications?.isNotEmpty() == true) {
                val notification = if (AppHelper.getAppLanguage() == Constants.ENGLISH) {
                    "Notify ${event.notifications?.first()?.days ?: 10} days in advance"
                } else if (AppHelper.getAppLanguage() == Constants.JAPANESE) {
                    "${event.notifications?.first()?.days ?: 10}日前までに通知"
                } else if (AppHelper.getAppLanguage() == Constants.TRADITIONAL_CHINESE) {
                    "提前${event.notifications?.first()?.days ?: 10}天通知"
                } else {

                    "提前${event.notifications?.first()?.days ?: 10}天通知"
                }

                binding.txtNotification.text = notification
            }

            getPeopleList(event.people)

            binding.txtNote.text = event.note


            val participants = if (AppHelper.getAppLanguage() == Constants.ENGLISH) {
                "${peopleList.size} participants"
            } else if (AppHelper.getAppLanguage() == Constants.JAPANESE) {
                "${peopleList.size} 人の参加者"
            } else if (AppHelper.getAppLanguage() == Constants.TRADITIONAL_CHINESE) {
                "${peopleList.size}名參加者"
            } else {

                "${peopleList.size}名参加者"
            }

            binding.txtParticipants.text = participants
            binding.txtAnniversary.text = event.anniversaryName

            event.photos?.let { photoAdapter?.setData(it) }
        }*/
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
                val intent1 = Intent(this, CreateEventActivity::class.java)
                if (event?.eventList?.isNotEmpty() == true) {
                    intent1.putExtra("event", event?.eventList?.get(currentPosition))
                } else {
                    intent1.putExtra("event", event)
                }
                startActivityForResult(intent1, REQUEST_UPDATE_EVENT)
                true
            }
            R.id.menuDelete -> {
                DialogHelper.newInstance(
                    getString(R.string.are_you_sure_n_you_want_to_delete_this_event),
                    (getString(R.string.delete)),
                    (getString(R.string.cancel)), object : IDialogButtonClick {
                        override fun onButtonClick(isPositive: Boolean) {
                            if (isPositive) {
                                if (isNetworkAvailable(this@EventDetailActivity)) {
                                    deleteEvent(eventId)
                                } else {
                                    noNetWorkAvailable()
                                }
                            }
                        }
                    }).show(this)
                true
            }
            /*R.id.menuShare -> {
                share()
                true
            }*/
            else -> false
        }
    }

    private fun deleteEvent(id: Long?) {
        showProgressDialog()
        val params = AppHelper.getDefaultParam()
        params["id"] = id

        NetworkCall.deleteEvent(
            params,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {
                        if (fragmentList.size != 1) {
                            fragmentList.removeAt(currentPosition)
                            viewPagerAdapter?.notifyItemRemoved(currentPosition)
                            currentPosition = (currentPosition - 1).takeIf { currentPosition != 0 }
                                ?: (currentPosition)
                            binding.viewPager.adapter = viewPagerAdapter
                            binding.viewPager.currentItem = currentPosition
                            manageClick()
                        } else {
                            finish()
                        }
                    } else DialogHelper.newInstance(message).show(this@EventDetailActivity)
                }

            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_UPDATE_EVENT -> {
//                    peopleList.clear()
                    val fragmentList = ArrayList<Fragment>()
                    viewPagerAdapter = AnniversaryViewPagerAdapter(this, fragmentList)
                    binding.viewPager.adapter = viewPagerAdapter

                    getEventDetail(eventId)
                }
            }
        }
    }

    /*private fun share() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        )
        intent.type = "text/plain"
        startActivity(intent)
    }*/

    override fun onClick(view: View?) {

        when (view) {
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

        eventId = if (event?.eventList?.size!! > 0) event?.eventList?.get(currentPosition)?.id
        else event?.id

        if (currentPosition == 0) {
            binding.btnPrev.visibility = View.GONE
        } else binding.btnPrev.visibility = View.VISIBLE

        if (currentPosition == (fragmentList.size.minus(1))) {
            binding.btnNext.visibility = View.GONE

        } else binding.btnNext.visibility = View.VISIBLE
    }

    /* private fun showAllPeople() {
         binding.btnShowAll.visibility = View.GONE
         peopleAdapter!!.setData(peopleList, true)
     }

     private fun getPeopleList(str: String?) {

         if (str == null) {
             binding.btnShowAll.visibility = View.GONE
             return
         }

         val people: List<List<String>> = listOf(str.split(","))

         for (i in people.first()) {
             if (i.isNotEmpty()) {
                 peopleList.add(i)
             }
         }
         peopleAdapter!!.setData(peopleList)

         if (peopleList.size <= 3) {
             binding.btnShowAll.visibility = View.GONE
         } else {
             binding.btnShowAll.visibility = View.VISIBLE
         }
     }*/

    override fun onBackPressed() {
        if (id != 0.toLong()) {
            openHomeActivity()
        } else super.onBackPressed()
    }

    companion object {
        const val REQUEST_UPDATE_EVENT = 1001
    }
}