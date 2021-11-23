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
import com.devstree.annibee.activity.SearchActivity.Companion.pastList
import com.devstree.annibee.adapter.PastAnniversaryDetailAdapter
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentPastAnniversaryBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent

class PastAnniversaryFragment : BaseFragment(), TextWatcher {

    lateinit var binding: FragmentPastAnniversaryBinding
    private var adapter: PastAnniversaryDetailAdapter? = null
//    private lateinit var actionBarBinding: LayoutToolbarBinding
//    private var pastList: ArrayList<AnniversaryEvent> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPastAnniversaryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        actionBarBinding = (activity as SearchActivity).binding.searchToolbar
        initUi()
    }

    private fun initUi() {

        adapter = PastAnniversaryDetailAdapter(this::onItemClick)
        binding.rvPastYear.layoutManager = LinearLayoutManager(context)
        binding.rvPastYear.adapter = adapter

//        actionBarBinding.edtSearch.addTextChangedListener(this)

        /*if (navigation?.isNetworkAvailable(context) == true) {
            getSearchData(actionBarBinding.edtSearch.text.toString())
        } else {
            navigation?.noNetWorkAvailable()
        }*/

        adapter?.setData(pastList)
        binding.txtNoData.visibility =
            View.VISIBLE.takeIf { pastList.isEmpty() } ?: View.GONE
    }

    /*private fun getSearchData(string: String) {
        val params = AppHelper.getDefaultParam()
        params["search_text"] = string

        NetworkCall.searchData(params, object : BaseResponseListener<ResponseBody<HomeData>>() {
            override fun result(response: ResponseBody<HomeData>?) {
                if (success) {
                    pastList.clear()
                    response?.data?.past?.let { pastList.addAll(it) }
                    adapter?.setData(pastList)
                    binding.txtNoData.visibility =
                        View.VISIBLE.takeIf { pastList.isEmpty() } ?: View.GONE

                } else {
                    DialogHelper.newInstance(message).show(activity!!)
                }

            }

        })
    }*/

    private fun onItemClick(item: AnniversaryEvent) {
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

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        /* if (navigation?.isNetworkAvailable(context) == true) {
             getSearchData(s.toString())
         } else {
             navigation?.noNetWorkAvailable()
         }*/
    }
}