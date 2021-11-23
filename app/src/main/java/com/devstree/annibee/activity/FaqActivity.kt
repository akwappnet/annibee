package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.adapter.FaqAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityFaqBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.FAQ
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.PaginationScrollListener

class FaqActivity : NavigationActivity() {

    lateinit var binding: ActivityFaqBinding

    private var adapter: FaqAdapter? = null
    var faqList: ArrayList<FAQ> = ArrayList()

    private var pageCount = 1
    private var totalPage = 1
    private var isLastPage = false
    private var isLoading = false
    private var isFirstTimeLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(R.string.faq, true)

        adapter = FaqAdapter()
        binding.container.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })
        binding.recyclerView.adapter = adapter

        if (isNetworkAvailable(this)) {
            callFAQ()
        }else {
            noNetWorkAvailable()
        }

        binding.recyclerView.addOnScrollListener(object :
            PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                if (isNetworkAvailable(this@FaqActivity)) {
                    callFAQ()
                } else {
                    noNetWorkAvailable()
                }
            }

            override val totalPageCount: Int get() = totalPage
            override val isLastPage: Boolean get() = this@FaqActivity.isLastPage
            override val isLoading: Boolean get() = this@FaqActivity.isLoading
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun callFAQ() {
        if (!isFirstTimeLoaded) showProgressDialog()

        isFirstTimeLoaded = true
        isLoading = true

        val params = AppHelper.getDefaultParam()
        params["page_number"] = pageCount
        params["language_id"] =
            when (AppHelper.getAppLanguage()) { /* 1: Japanese, 2: English, 3: Chinese1, 4 Chinese 2*/
                Constants.JAPANESE -> "1"
                Constants.ENGLISH -> "2"
                Constants.SIMPLIFIED_CHINESE -> "3"
                Constants.TRADITIONAL_CHINESE -> "4"
                else -> "2"
            }

        NetworkCall.faq(
            params,
            object : BaseResponseListener<ResponseBody<List<FAQ>>>() {
                override fun result(response: ResponseBody<List<FAQ>>?) {
                    isLoading = false
                    hideProgressDialog()
                    if (success) {

                        totalPage = response?.page_no ?: 0

                        if (pageCount == 1) {
                            faqList.clear()
                        }

                        response?.data?.let { faqList.addAll(it) }
                        adapter?.setItem(faqList)
                        pageCount += 1

                        if (faqList.isEmpty()) {
                            binding.recyclerView.visibility = View.GONE
                            binding.txtNoData.visibility = View.VISIBLE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.txtNoData.visibility = View.GONE
                        }
                    } else {
                        DialogHelper.newInstance(message, object : IDialogButtonClick {
                            override fun onButtonClick(isPositive: Boolean) {
                                finish()
                            }

                        }).show(this@FaqActivity)
                    }
                }
            }
        )
    }

    fun onClick(view: View?) {
        when (view) {
            binding.btnContactUs -> {
                startActivity(Intent(this, ContactUsActivity::class.java))
            }
        }
    }
}