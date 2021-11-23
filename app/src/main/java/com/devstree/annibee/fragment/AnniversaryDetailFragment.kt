package com.devstree.annibee.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstree.annibee.R
import com.devstree.annibee.activity.AnniversaryDetailActivity
import com.devstree.annibee.activity.CreateEventActivity
import com.devstree.annibee.activity.EventDetailActivity
import com.devstree.annibee.adapter.AnniversaryEventPhotoAdapter
import com.devstree.annibee.adapter.EventPhotoAdapter
import com.devstree.annibee.adapter.PeopleAdapter
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentAnniversaryDetailBinding
import com.devstree.annibee.model.response_model.AlertNotification
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.Event
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants

class AnniversaryDetailFragment(val anniversary: AnniversaryEvent) : BaseFragment(),
    View.OnClickListener {

    lateinit var binding: FragmentAnniversaryDetailBinding
    var adapter: AnniversaryEventPhotoAdapter? = null
    val list = ArrayList<Event>()

    private var peopleAdapter: PeopleAdapter? = null

    private val peopleList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnniversaryDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {

        binding.listener = this

        adapter = AnniversaryEventPhotoAdapter(object : EventPhotoAdapter.ClickListener {
            override fun onItemClick(item: Event, position: Int) {
                val intent = Intent(context, EventDetailActivity::class.java)
                intent.putExtra("event", item.id)
                startActivity(intent)
            }
        })
        binding.rvEvent.adapter = adapter

        peopleAdapter = PeopleAdapter()
        binding.rvPeople.adapter = peopleAdapter

        binding.btnShowAll.setOnClickListener {
            showAllPeople()
        }

        setData(anniversary)
    }

    private fun getAlertNotification(): java.util.ArrayList<AlertNotification> {
        val alertNotification = java.util.ArrayList<AlertNotification>()
        alertNotification.add(
            AlertNotification(
                1.toLong(),
                getString(R.string._4_weeks_ago),
                28,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                2.toLong(),
                getString(R.string._3_weeks_ago),
                21,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                3.toLong(),
                getString(R.string._2_weeks_ago),
                14,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                4.toLong(),
                getString(R.string._1_weeks_ago),
                7,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                5.toLong(),
                getString(R.string.the_day_before),
                1,
                false
            )
        )
        alertNotification.add(AlertNotification(6.toLong(), getString(R.string.that_day), 0, false))

        return alertNotification
    }

    @SuppressLint("SetTextI18n")
    fun setData(anniversary: AnniversaryEvent?) {
        anniversaryDetail?.showProgressDialog()

        peopleList.clear()
        list.clear()
        binding.txtNotice.text = ""

        if (anniversary?.photos?.isNotEmpty() == true) binding.mainImage.setUrl(anniversary.photos?.last()?.image)
        binding.txtAnniName.text = anniversary?.name
        binding.txtDate.text = anniversary?.date
        binding.txtAttribute.text =
            anniversary?.attributeName.takeIf { anniversary?.defaultAttributeName.isNullOrEmpty() }
                ?: "${anniversary?.attributeName} (${anniversary?.defaultAttributeName})"


        /*if (anniversary?.pastYears == 0.toLong()) {
            if (anniversary.pastMonths == 0.toLong()) {
                if (anniversary.pastDays == 0.toLong()) {
                    binding.txtPastDays.text = ""
                } else {
                   c
                }
            } else {
                binding.txtPastDays.text = when (AppHelper.getAppLanguage()) {
                    Constants.ENGLISH -> {
                        "${anniversary.pastMonths} month"
                    }
                    Constants.JAPANESE -> {
                        "${anniversary.pastMonths}ヶ月"
                    }
                    Constants.TRADITIONAL_CHINESE -> {
                        "${anniversary.pastMonths}個月"
                    }
                    Constants.SIMPLIFIED_CHINESE -> {
                        "${anniversary.pastMonths}个月"
                    }
                    else -> {
                        "${anniversary.pastMonths} month"
                    }
                }
            }
        } else {
            binding.txtPastDays.text = when (AppHelper.getAppLanguage()) {
                Constants.ENGLISH -> {
                    "${anniversary?.pastYears} year"
                }
                Constants.JAPANESE -> {
                    "${anniversary?.pastYears}年"
                }
                Constants.TRADITIONAL_CHINESE -> {
                    "${anniversary?.pastYears}年"
                }
                Constants.SIMPLIFIED_CHINESE -> {
                    "${anniversary?.pastYears}年"
                }
                else -> {
                    "${anniversary?.pastYears} year"
                }
            }
        }*/
        //anniversary?.pastDays.toString().isNullOrEmpty() && anniversary?.pastDays!! >0
        if(!anniversary?.pastDays.toString().isNullOrEmpty() && anniversary?.pastDays!! >0) {

            binding.txtPastDays.text = when (AppHelper.getAppLanguage()) {
                Constants.ENGLISH -> {
                    "${anniversary?.pastDays} days passed"
                }
                Constants.JAPANESE -> {
                    "${anniversary?.pastDays}日経過"
                }
                Constants.TRADITIONAL_CHINESE -> {
                    "${anniversary?.pastDays}天过去了"
                }
                Constants.SIMPLIFIED_CHINESE -> {
                    "${anniversary?.pastDays}天过去了"
                }
                else -> {
                    "${anniversary?.pastDays} days passed"
                }
            }
        }else{
            binding.txtPastDays.visibility =View.GONE
        }

        if (anniversary?.notifications?.isNotEmpty() == true) {
            val notification = when (AppHelper.getAppLanguage()) {
                Constants.ENGLISH -> {
                    "Notify ${anniversary.notifications?.first()?.days ?: 10} days in advance"
                }
                Constants.JAPANESE -> {
                    "${anniversary.notifications?.first()?.days ?: 10}日前までに通知"
                }
                Constants.TRADITIONAL_CHINESE -> {
                    "提前${anniversary.notifications?.first()?.days ?: 10}天通知"
                }
                Constants.SIMPLIFIED_CHINESE -> {
                    "提前${anniversary.notifications?.first()?.days ?: 10}天通知"
                }
                else -> {
                    "Notify ${anniversary.notifications?.first()?.days ?: 10} days in advance"
                }
            }

            binding.txtNotification.text = notification
        } else {
            binding.img.visibility = View.GONE
        }

        anniversary?.notifications?.indices?.forEach { data ->

            for (index in getAlertNotification()) {
                if (index.id == anniversary.notifications?.get(data)?.id) {
                    if (data != anniversary.notifications!!.size - 1) {
                        binding.txtNotice.text =
                            binding.txtNotice.text.toString() + index.name + ", "
                    } else {
                        binding.txtNotice.text =
                            binding.txtNotice.text.toString() + index.name
                    }
                }
            }
        }

        getPeopleList(anniversary?.people)

        val participants = when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                "${peopleList.size} participants"
            }
            Constants.JAPANESE -> {
                "${peopleList.size} 人の参加者"
            }
            Constants.TRADITIONAL_CHINESE -> {
                "${peopleList.size}名參加者"
            }
            Constants.SIMPLIFIED_CHINESE -> {
                "${peopleList.size}名参加者"
            }
            else -> {
                "${peopleList.size} participants"
            }
        }

        binding.txtParticipants.text = participants


        binding.txtNote.text = anniversary?.note


        anniversary?.eventPhotos?.forEach { event ->
            if (event.photos?.isNotEmpty() == true) {
                list.add(event)
            }
        }

        list.let { adapter?.setData(it) }

        anniversaryDetail?.hideProgressDialog()

    }

    override fun onClick(view: View?) {

        when (view) {
            binding.btnCreateNew -> {
                val intent = Intent(context, CreateEventActivity::class.java)
                intent.putExtra("anniversary", anniversary)
                activity?.startActivityForResult(
                    intent,
                    AnniversaryDetailActivity.REQUEST_NEW_EVENT
                )
            }
        }
    }

    private fun showAllPeople() {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            setData(data?.getParcelableExtra("anniversary_obj"))
/*            when (requestCode) {
//                AnniversaryDetailActivity.REQUEST_UPDATE_ANNIVERSARY -> {
//                    AnniversaryDetailActivity().getAnniversaryDetail(anniversary.id)

//                    setData(Gson().fromJson(data?.getStringExtra("anniversary_obj"), AnniversaryEvent::class.java))
//                }
//                AnniversaryDetailActivity.REQUEST_NEW_EVENT -> {
//                    AnniversaryDetailActivity().getAnniversaryDetail(anniversary.id)
//                }
            }*/
        }
    }
}