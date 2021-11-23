package com.devstree.annibee.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.adapter.ListAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityListBinding

class ListActivity : NavigationActivity() {

    lateinit var binding: ActivityListBinding
    var adapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {

        setUpToolBar(getString(R.string.list), true)

        binding.rvList.layoutManager = LinearLayoutManager(this)
        adapter = ListAdapter(this)
        adapter!!.setOnItemClickListener(object : ListAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                openEventDetailActivity()
            }

        })
        binding.rvList.adapter = adapter
    }
}