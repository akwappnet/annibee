package com.devstree.annibee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentIntro1Binding

class IntroFragment1 : BaseFragment() {

    lateinit var binding: FragmentIntro1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIntro1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

}