package com.devstree.annibee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.activity.HomeActivity
import com.devstree.annibee.adapter.ListAdapter
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.databinding.FragmentListBinding
import java.text.SimpleDateFormat
import java.util.*

class ListFragment : BaseFragment() {

    lateinit var binding: FragmentListBinding
    var adapter: ListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy")
        val dateString: String = sdf.format(calendar.time)

        (activity as HomeActivity).binding.homeToolbar.spinner.text = dateString

        (activity as HomeActivity).binding.homeToolbar.spinner.setOnClickListener {

            (activity as HomeActivity).showPopupMenu(
                activity!!,
                (activity as HomeActivity).binding.homeToolbar.spinner,
                (activity as HomeActivity).yearList(),
                true,
                object :
                    ObjectCallback<PopUpModel> {
                    override fun response(obj: PopUpModel?) {
                        if (obj == null) return
                        (activity as HomeActivity).binding.homeToolbar.spinner.text = obj.title
                    }

                })
        }


        /*binding.rvList.layoutManager = LinearLayoutManager(context)
        adapter = ListAdapter(context!!)
        adapter!!.setOnItemClickListener(object : ListAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).openEventDetailActivity()
            }

        })
        binding.rvList.adapter = adapter*/
    }
}