package com.devstree.annibee.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstree.annibee.R
import com.devstree.annibee.activity.HomeActivity
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.databinding.FragmentAddAnniEventBinding

class AddAnniEventFragment : BaseFragment() {

    lateinit var binding: FragmentAddAnniEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddAnniEventBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        binding.txtCreateAnni.setOnClickListener {
            (activity as HomeActivity).openCreateAnniversaryActivity()
        }

        binding.txtCreateEvent.setOnClickListener {
            (activity as HomeActivity).openCreateEventActivity()
        }
    }
}