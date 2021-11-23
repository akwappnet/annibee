package com.devstree.annibee.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstree.annibee.R
import com.devstree.annibee.activity.EventPhotoActivity
import com.devstree.annibee.adapter.PeopleAdapter
import com.devstree.annibee.adapter.PhotoAdapter
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentEventDetailBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.Image
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Log

class EventDetailFragment(val event: AnniversaryEvent) : BaseFragment(),
    View.OnClickListener {

    lateinit var binding: FragmentEventDetailBinding

    val peopleList = ArrayList<String>()
    var photoAdapter: PhotoAdapter? = null
    var peopleAdapter: PeopleAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {

        photoAdapter = PhotoAdapter(this::onImageClick)
        binding.rvPhoto.adapter = photoAdapter

        peopleAdapter = PeopleAdapter()
        binding.rvPeople.adapter = peopleAdapter

        binding.btnShowAll.setOnClickListener {
            showAllPeople()
        }
        
        setData(event)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(event: AnniversaryEvent?) {

        peopleList.clear()

        if (event?.photos?.isNotEmpty() == true) binding.mainImage.setUrl(event.photos?.first()?.image)
        binding.txtEventName.text = event?.name

        if (event?.eventDates?.isNotEmpty() == true) {
            binding.txtDate.text =
                "${event.date} - ${event.eventEndDate}"
            if (event.isAllDay == "0") {
                binding.txtTime.visibility = View.VISIBLE
                binding.txtTime.text = "${event.eventFormatTime} - ${event.eventFormatEndTime}"
            }
            else binding.txtTime.visibility = View.GONE
        }

        if (event?.notifications?.isNotEmpty() == true) {
            val notification = when (AppHelper.getAppLanguage()) {
                Constants.ENGLISH -> {
                    "Notify ${event.notifications?.first()?.days ?: 10} days in advance"
                }
                Constants.JAPANESE -> {
                    "${event.notifications?.first()?.days ?: 10}日前までに通知"
                }
                Constants.TRADITIONAL_CHINESE -> {
                    "提前${event.notifications?.first()?.days ?: 10}天通知"
                }
                Constants.SIMPLIFIED_CHINESE -> {
                    "提前${event.notifications?.first()?.days ?: 10}天通知"
                }
                else -> {
                    "Notify ${event.notifications?.first()?.days ?: 10} days in advance"
                }
            }

            binding.txtNotification.text = notification
        }

        getPeopleList(event?.people)

        binding.txtNote.text = event?.note


        val participants = when(AppHelper.getAppLanguage()) {
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
        binding.txtAnniversary.text = event?.anniversaryName


        event?.photos?.let { photoAdapter?.setData(it) }

    }

    private fun onImageClick(image: Image, position: Int) {
        val intent = Intent(context, EventPhotoActivity::class.java)
        intent.putExtra("image", image)
        intent.putExtra("event", event)
        intent.putExtra("position", position)
        startActivity(intent)
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


    override fun onClick(v: View?) {
        when (v) {

        }
    }
}