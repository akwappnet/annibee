package com.devstree.annibee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devstree.annibee.databinding.FragmentPhotoBinding
import com.yanzhenjie.album.mvp.BaseFragment

class PhotoFragment(val image: String?) : BaseFragment() {

    lateinit var binding : FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(image).into(binding.imageView)
    }
}