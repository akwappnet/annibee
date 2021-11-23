package com.devstree.annibee.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.devstree.annibee.R
import com.devstree.annibee.adapter.SearchViewPagerAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivitySearchBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.HomeData
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchActivity : NavigationActivity(), TextWatcher {

    lateinit var binding: ActivitySearchBinding
    var adapter : SearchViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {

        setUpToolBar("", true)

        binding.searchToolbar.txtToolbarTitle.visibility = View.GONE
        binding.searchToolbar.edtSearch.visibility = View.VISIBLE

        if (isNetworkAvailable(this)) {
            getSearchData(binding.searchToolbar.edtSearch.text.toString())
        } else {
            noNetWorkAvailable()
        }

        binding.tabLayout.addTab(binding.tabLayout.newTab())
        binding.tabLayout.addTab(binding.tabLayout.newTab())
        binding.tabLayout.addTab(binding.tabLayout.newTab())

        adapter = SearchViewPagerAdapter(this, binding.tabLayout.tabCount)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

        binding.searchToolbar.edtSearch.addTextChangedListener(this)


    }

    private fun getSearchData(string: String) {
        val params = AppHelper.getDefaultParam()
        params["search_text"] = string

        NetworkCall.searchData(params, object : BaseResponseListener<ResponseBody<HomeData>>() {
            override fun result(response: ResponseBody<HomeData>?) {
                if (success) {

                    upcomingList.clear()
                    todayList.clear()
                    pastList.clear()

                    response?.data?.upcoming?.let { upcomingList.addAll(it) }
                    response?.data?.today?.let { todayList.addAll(it) }
                    response?.data?.past?.let { pastList.addAll(it) }

                    binding.viewPager.adapter = adapter
                    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                        when (position) {
                            0 -> {
                                tab.text = getString(R.string.all)
                            }
                            1 -> {
                                tab.text = getString(R.string.upcoming_anniversary_and_events)
                            }
                            2 -> {
                                tab.text = getString(R.string.past_anniversaries_and_events)
                            }
                        }
                    }.attach()

                } else {
                    DialogHelper.newInstance(message).show(this@SearchActivity)
                }

            }

        })
    }

    fun onClick(v: View) {
        when (v) {
            binding.searchToolbar.imgFilter -> {

            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        if (isNetworkAvailable(this)) {
            getSearchData(s.toString())
        } else {
            noNetWorkAvailable()
        }
    }

    companion object {
        var upcomingList: ArrayList<AnniversaryEvent> = ArrayList()
        var todayList: ArrayList<AnniversaryEvent> = ArrayList()
        var pastList: ArrayList<AnniversaryEvent> = ArrayList()
    }
}