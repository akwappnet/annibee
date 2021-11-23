package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.adapter.NotificationAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityNotificationBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.PaginationScrollListener
import com.devstree.annibee.utility.PreferenceManager
import com.devstree.annibee.utility.SharedPrefConstant

class NotificationActivity : NavigationActivity() {

    lateinit var binding: ActivityNotificationBinding
    var adapter: NotificationAdapter? = null
    private var notificationList: ArrayList<AnniversaryEvent> = ArrayList()

    private var pageCount = 1
    private var totalPage = 1
    private var isLastPage = false
    private var isLoading = false
    private var isFirstTimeLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(getString(R.string.notification), true)

        binding.container.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotificationAdapter(this::onItemClick)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object :
            PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                getNotificationList()
            }

            override val totalPageCount: Int get() = totalPage
            override val isLastPage: Boolean get() = this@NotificationActivity.isLastPage
            override val isLoading: Boolean get() = this@NotificationActivity.isLoading
        })

        getNotificationList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun onItemClick(item: AnniversaryEvent) {
        when (item.type) {
            "1" -> {
                val intent = Intent(this, AnniversaryDetailActivity::class.java)
                intent.putExtra("anniversary", item.anniversaryId)
                startActivity(intent)
            }
            "2" -> {
                val intent = Intent(this, EventDetailActivity::class.java)
                intent.putExtra("event", item.eventId)
                startActivity(intent)
            }
            "3" -> {
                val intent = Intent(this, AnniversaryDetailActivity::class.java)
                intent.putExtra("anniversary", item.anniversaryId)
                startActivity(intent)
            }
        }
    }


    private fun getNotificationList() {
        if (!isFirstTimeLoaded) showProgressDialog()

        isFirstTimeLoaded = true
        isLoading = true

        val params = AppHelper.getDefaultParam()
        params["page_number"] = pageCount

        NetworkCall.fcmNotificationList(params, object : BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>() {
            override fun result(response: ResponseBody<List<AnniversaryEvent>>?) {
                isLoading = false
                hideProgressDialog()
                if (success) {

                    totalPage = response?.page_no ?: 0

                    if (pageCount == 1) {
                        notificationList.clear()
                    }

                    notificationList.addAll(response?.data as ArrayList<AnniversaryEvent>)
                    adapter?.setItem(notificationList)
                    pageCount += 1
//                    response.data?.let { adapter?.setItem(it) }

                    if (notificationList.isEmpty()) {
                        binding.txtNoData.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }else {
                        binding.txtNoData.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                    readNotification()
                }
                else{
                    readNotification()
                    DialogHelper.newInstance(message).show(this@NotificationActivity)
                }
            }

        })

    }

    private fun readNotification() {
        showProgressDialog()

        NetworkCall.readNotification(object : BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>() {
            override fun result(response: ResponseBody<List<AnniversaryEvent>>?) {
                PreferenceManager.putString(SharedPrefConstant.NOTIFICATION_COUNTER,"0")
                isLoading = false
                hideProgressDialog()
            }
        })
    }
}