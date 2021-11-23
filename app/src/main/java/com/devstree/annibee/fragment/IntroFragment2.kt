package com.devstree.annibee.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstree.annibee.R
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentIntro2Binding
import com.devstree.annibee.databinding.FragmentIntro3Binding

class IntroFragment2 : BaseFragment() {

    lateinit var binding: FragmentIntro2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntro2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

}