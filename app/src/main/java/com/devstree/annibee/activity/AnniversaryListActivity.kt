package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.adapter.AnniversaryListAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityAnniversaryListBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.PaginationScrollListener

class AnniversaryListActivity : NavigationActivity() {

    lateinit var binding: ActivityAnniversaryListBinding
    var adapter: AnniversaryListAdapter? = null
    var anniversaryList = ArrayList<AnniversaryEvent>()

    private var pageCount = 1
    private var totalPage = 1
    private var isLastPage = false
    private var isLoading = false
    private var isFirstTimeLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnniversaryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(getString(R.string.anniversary), true)


        binding.recyclerView.setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()

            }
        })


        adapter = AnniversaryListAdapter()
        adapter!!.setOnItemClickListener(object : AnniversaryListAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                val intent =
                    Intent(this@AnniversaryListActivity, AnniversaryDetailActivity::class.java)
                intent.putExtra("anniversary", anniversaryList[position].id)
                startActivity(intent)
            }
        })
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object :
            PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                if (isNetworkAvailable(this@AnniversaryListActivity)) {
                    getAnniversaryList()
                } else {
                    noNetWorkAvailable()
                }
            }

            override val totalPageCount: Int get() = totalPage
            override val isLastPage: Boolean get() = this@AnniversaryListActivity.isLastPage
            override val isLoading: Boolean get() = this@AnniversaryListActivity.isLoading
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun getAnniversaryList() {

        if (!isFirstTimeLoaded) showProgressDialog()

        isFirstTimeLoaded = true
        isLoading = true

        val params = AppHelper.getDefaultParam()
        params["page_number"] = pageCount

        NetworkCall.anniversaryList(
            params,
            object : BaseResponseListener<ResponseBody<List<AnniversaryEvent>>>() {
                override fun result(response: ResponseBody<List<AnniversaryEvent>>?) {
                    isLoading = false
                    hideProgressDialog()
                    if (success) {

                        totalPage = response?.page_no ?: 0

                        if (pageCount == 1) {
                            anniversaryList.clear()
                        }

                        anniversaryList.addAll(response?.data as ArrayList<AnniversaryEvent>)
                        adapter?.setData(anniversaryList)
                        pageCount += 1

                        if (anniversaryList.isEmpty()) {
                            binding.txtNoData.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            binding.txtNoData.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }

                    } else DialogHelper.newInstance(message).show(this@AnniversaryListActivity)
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(this)) {
            getAnniversaryList()
        } else {
            noNetWorkAvailable()
        }
    }
}