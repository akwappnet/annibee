package com.devstree.annibee.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.activity.AnniversaryDetailActivity
import com.devstree.annibee.activity.EventDetailActivity
import com.devstree.annibee.activity.HomeActivity
import com.devstree.annibee.adapter.*
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentHomeBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.HomeData
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(), View.OnClickListener, HomePastImageAdapter.ItemClickListener,
    HomeUpcomingAdapter.ClickListener, HomeTodayAdapter.ClickListener {

    lateinit var binding: FragmentHomeBinding
    var upcomingAdapter: HomeUpcomingAdapter? = null
    var todayAdapter: HomeTodayAdapter? = null
    var pastAdapter: HomePastAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {

        adView()

/*
        binding.imgClose.setOnClickListener {
            binding.rlAdView.visibility = View.GONE
        }
*/

        (activity as HomeActivity).binding.homeToolbar.imgSearch.setOnClickListener(this)


        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)
        upcomingAdapter = HomeUpcomingAdapter()
        upcomingAdapter!!.setOnItemClickListener(this)
        binding.rvUpcoming.adapter = upcomingAdapter

        binding.rvToday.layoutManager = LinearLayoutManager(context)
        todayAdapter = HomeTodayAdapter()
        todayAdapter!!.setOnItemClickListener(this)
        binding.rvToday.adapter = todayAdapter

        binding.rvPast.layoutManager = LinearLayoutManager(context)
        pastAdapter = HomePastAdapter(this)
        pastAdapter!!.setOnItemClickListener(object : HomePastAdapter.ClickListener{
            override fun onItemClick(position: Int, item: AnniversaryEvent) {
                this@HomeFragment.onItemClick(item, position)
            }

        })
        binding.rvPast.adapter = pastAdapter
    }

    override fun onItemClick(item: AnniversaryEvent, position: Int) {
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


    override fun onResume() {
        super.onResume()
        if (home?.isNetworkAvailable(context) == true) {
            getAllHomeData()
        }
        else {
            home?.noNetWorkAvailable()
        }
    }

    private fun getAllHomeData() {
        navigation?.showProgressDialog()

        NetworkCall.getHomeData(
            AppHelper.getDefaultParam(),
            object : BaseResponseListener<ResponseBody<HomeData>>() {
                override fun result(response: ResponseBody<HomeData>?) {
                    navigation?.hideProgressDialog()
                    if (success) {
                        PreferenceManager.putString(SharedPrefConstant.NOTIFICATION_COUNTER,response?.data?.notificationCount.toString())
                      //  (activity as HomeActivity).showAlertMessage(response?.data?.notificationCount.toString())
                        if (response?.data?.upcoming?.isEmpty() == true) {
                            binding.rvUpcoming.visibility = View.GONE
                            binding.layoutNoDataUpcoming.visibility = View.VISIBLE
                        }else {
                            binding.rvUpcoming.visibility = View.VISIBLE
                            binding.layoutNoDataUpcoming.visibility = View.GONE
                        }
                        if (response?.data?.today?.isEmpty() == true) {
                            binding.rvToday.visibility = View.GONE
                            binding.layoutNoDataToday.visibility = View.VISIBLE
                        }
                        else {
                            binding.rvToday.visibility = View.VISIBLE
                            binding.layoutNoDataToday.visibility = View.GONE
                        }
                        if (response?.data?.past?.isEmpty() == true) {
                            binding.rvPast.visibility = View.GONE
                            binding.layoutNoDataPast.visibility = View.VISIBLE
//                            binding.largeAdView3.visibility = View.VISIBLE
//                            adView()
                        }
                        else {
                            binding.rvPast.visibility = View.VISIBLE
                            binding.layoutNoDataPast.visibility = View.GONE

//                            binding.largeAdView3.visibility = View.GONE
                        }


                        response?.data?.today?.let { todayAdapter?.setData(it) }
                        response?.data?.upcoming?.let { sortAnniversaryData(it) }
                        //response?.data?.upcoming?.let { upcomingAdapter?.setData(it) }
                        response?.data?.past?.let { pastAdapter?.setData(it) }

                    }
                    else base?.unAuthorized(code, message.toString())
                }
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun sortAnniversaryData(list : List<AnniversaryEvent>)  {

        /*val list = ArrayList<AnniversaryEvent>()

        val er1= AnniversaryEvent()
        er1.date ="2021-07-19" // 06-21  621 - 3
        val er2= AnniversaryEvent()
        er2.date ="2021-06-24" // 05-21  521 - 1
        val er3= AnniversaryEvent()
        er3.date ="2021-07-23" // 08-20  820 - 4
        val er4= AnniversaryEvent()
        er4.date ="2021-06-27" // 04-15  1115 - 5
        val er5= AnniversaryEvent()
        er5.date ="2021-06-28" // 06-20  620 - 2
        val er6= AnniversaryEvent()
        er5.date ="2021-06-29" // 06-20  620 - 2


        list.add(er1)
        list.add(er2)
        list.add(er3)
        list.add(er4)
        list.add(er5)
        list.add(er6)*/


        Collections.sort(list, Comparator<AnniversaryEvent?> { o1, o2 ->
            try {
                if(o1?.date != null && o2?.date != null) {
                    val date1 = o1.date
                    val date2 = o2.date
                    val date1Length = date1!!.split("-")
                    val date2Length = date2!!.split("-")

                    val date1Data : Int = Integer.parseInt( date1Length[1]+date1Length[2])
                    val date2Data : Int = Integer.parseInt( date2Length[1]+date2Length[2])

                    return@Comparator date2Data - date1Data
                }else{
                    return@Comparator 0
                }
            } catch (e: Exception) {
                return@Comparator 0
            }
        })

        for (item in list){
            Log.e("list@@@@  ${item.date}")
        }


        list.let { upcomingAdapter?.setData(it) }

    }

    private fun adView() {
        MobileAds.initialize(requireContext())

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
       /* binding.largeAdView1.loadAd(adRequest)
        binding.largeAdView2.loadAd(adRequest)
        binding.largeAdView3.loadAd(adRequest)*/

    }

    override fun onClick(v: View) {
        when (v) {
            (activity as HomeActivity).binding.homeToolbar.imgSearch -> {
                (activity as HomeActivity).openSearchActivity()
            }
        }
    }

}