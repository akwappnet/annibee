package com.devstree.annibee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.ItemUpcomingAnniversaryBinding
import com.devstree.annibee.model.UpcomingAnniversary
import com.devstree.annibee.model.UpcomingAnniversaryDetail

class UpcomingAnniversaryAdapter(
    val context: Context,
    /*private val mList: ArrayList<UpcomingAnniversary>,*/
    private val onItemClickListener: UpcomingAnniversaryDetailAdapter.ClickListener
): RecyclerView.Adapter<UpcomingAnniversaryAdapter.ViewHolder>(), (Int) -> Unit {

    lateinit var binding: ItemUpcomingAnniversaryBinding
    private lateinit var adapter: UpcomingAnniversaryDetailAdapter
    lateinit var mClickListener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemUpcomingAnniversaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        binding.txtYear.text = mList[position].year

//        adapter = UpcomingAnniversaryDetailAdapter(this, /*mList[position].detail*/)
        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener(onItemClickListener)
        binding.rvUpcoming.adapter = adapter

    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(val binding: ItemUpcomingAnniversaryBinding): RecyclerView.ViewHolder(binding.root) {}

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }

    override fun invoke(position: Int) {

    }
}