package com.devstree.annibee.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.HomeItemHolidayBinding
import com.devstree.annibee.databinding.HomeItemUpcomingBinding
import java.util.*
import kotlin.collections.ArrayList

class HolidayListAdapter : RecyclerView.Adapter<HolidayListAdapter.ViewHolder>() {

    lateinit var binding: HomeItemHolidayBinding
    var upcomingList: ArrayList<String> = ArrayList()
    private lateinit var mClickListener: ClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            HomeItemHolidayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(upcomingList[position])
    }

    override fun getItemCount(): Int {
        return upcomingList.size
    }

    @SuppressLint("SetTextI18n")
    class ViewHolder(val binding: HomeItemHolidayBinding, val adapter: HolidayListAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
           binding.txtName.text = item
        }
    }

    fun setData(list: Array<String>) {
        upcomingList.clear()
        upcomingList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(item: String, position: Int)
    }

}