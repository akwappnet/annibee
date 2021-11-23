package com.devstree.annibee.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.activity.AnniversaryDetailActivity
import com.devstree.annibee.activity.EventDetailActivity
import com.devstree.annibee.activity.SearchActivity.Companion.todayList
import com.devstree.annibee.activity.SearchActivity.Companion.upcomingList
import com.devstree.annibee.adapter.TodayAnniversaryDetailAdapter
import com.devstree.annibee.adapter.UpcomingAnniversaryDetailAdapter
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentUpcomingAnniversaryBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent

class UpcomingAnniversaryFragment : BaseFragment(), TextWatcher {

    lateinit var binding: FragmentUpcomingAnniversaryBinding
    private var adapter: UpcomingAnniversaryDetailAdapter? = null
    private var todayAdapter: TodayAnniversaryDetailAdapter? = null
//    private lateinit var actionBarBinding: LayoutToolbarBinding
//    private var upcomingList: ArrayList<AnniversaryEvent> = ArrayList()
//    private var todayList: ArrayList<AnniversaryEvent> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingAnniversaryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        actionBarBinding = (activity as SearchActivity).binding.searchToolbar
        initUi()
    }

    private fun initUi() {
        adapter = UpcomingAnniversaryDetailAdapter(this::onItemClick)
        binding.rvUpcomingYear.layoutManager = LinearLayoutManager(context)
        binding.rvUpcomingYear.adapter = adapter

        todayAdapter = TodayAnniversaryDetailAdapter(this::onItemClick)
        binding.rvTodayYear.layoutManager = LinearLayoutManager(context)
        binding.rvTodayYear.adapter = todayAdapter

//        actionBarBinding.edtSearch.addTextChangedListener(this)

        /* if (navigation?.isNetworkAvailable(context) == true) {
             getSearchData(actionBarBinding.edtSearch.text.toString())
         } else {
             navigation?.noNetWorkAvailable()
         }*/

        adapter?.setData(upcomingList)
        todayAdapter?.setData(todayList)

        binding.txtNoData.visibility =
            View.VISIBLE.takeIf { upcomingList.isEmpty() } ?: View.GONE

        binding.txtNoTodayData.visibility =
            View.VISIBLE.takeIf { todayList.isEmpty() } ?: View.GONE
    }

    /*private fun getSearchData(string: String) {
        val params = AppHelper.getDefaultParam()
        params["search_text"] = string

        NetworkCall.searchData(params, object : BaseResponseListener<ResponseBody<HomeData>>() {
            override fun result(response: ResponseBody<HomeData>?) {
                if (success) {
                    upcomingList.clear()
                    todayList.clear()

                    response?.data?.upcoming?.let { upcomingList.addAll(it) }
                    response?.data?.today?.let { todayList.addAll(it) }

                    adapter?.setData(upcomingList)
                    todayAdapter?.setData(todayList)

                    binding.txtNoData.visibility =
                        View.VISIBLE.takeIf { upcomingList.isEmpty() } ?: View.GONE

                    binding.txtNoTodayData.visibility =
                        View.VISIBLE.takeIf { todayList.isEmpty() } ?: View.GONE

                } else {
                    DialogHelper.newInstance(message).show(activity!!)
                }

            }

        })
    }*/

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        /*  if (navigation?.isNetworkAvailable(context) == true) {
              getSearchData(s.toString())
          } else {
              navigation?.noNetWorkAvailable()
          }*/
    }

    fun onItemClick(item: AnniversaryEvent) {
        when (item.type) {
            "1" -> {
                val intent = Intent(context, AnniversaryDetailActivity::class.java)
                intent.putExtra("anniversary", item.id)
                startActivity(intent)
            }
            "2" -> {
                val intent = Intent(context, EventDetailActivity::class.java)
                intent.putExtra("event", item.id)
                startActivity(intent)
            }
            "3" -> {
                val intent = Intent(context, AnniversaryDetailActivity::class.java)
                intent.putExtra("anniversary", item.id)
                startActivity(intent)
            }
        }
    }
}