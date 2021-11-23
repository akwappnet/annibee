package com.devstree.annibee.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.devstree.annibee.adapter.PhotoViewPagerAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityEventPhotoBinding
import com.devstree.annibee.fragment.PhotoFragment
import com.devstree.annibee.model.response_model.AnniversaryEvent

class EventPhotoActivity : NavigationActivity() {

    lateinit var binding: ActivityEventPhotoBinding
    var event: AnniversaryEvent? = null
    var position: Int = 0
    var adapter: PhotoViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setUpToolBar("", true)


        event = intent.getParcelableExtra("event")
        position = intent.getIntExtra("position", 0)

//        image = intent.getParcelableExtra("image")
//
        Glide.with(this).load(event?.photos?.get(position)?.image).into(binding.imageView)

        adapter = PhotoViewPagerAdapter(this)

        if (event?.photos?.isNotEmpty() == true) {
            for (photos in event?.photos!!) {
                adapter!!.add(PhotoFragment(photos.image))
            }
        }
        binding.viewPager.adapter = adapter

        binding.viewPager.offscreenPageLimit = event?.photos?.size!!
        binding.viewPager.currentItem = position
    }
}