package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.ItemListBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AnniversaryListAdapter : RecyclerView.Adapter<AnniversaryListAdapter.ViewHolder>() {

    private lateinit var mClickListener: ClickListener
    lateinit var binding: ItemListBinding
    private val eventList: ArrayList<AnniversaryEvent> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventList[position])

        holder.itemView.setOnClickListener {
            mClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(val binding: ItemListBinding, val adapter: AnniversaryListAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AnniversaryEvent) {

            binding.llDate.visibility = View.VISIBLE

            when (data.type) {
                "1" -> {
                    binding.card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.sky_blue_alpha))
                }
                "2" -> {
                    binding.card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.light_pink))
                }
                "3" -> {
                    binding.card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
            }

            val month = SimpleDateFormat("MMM", Locale.ENGLISH)
            val day = SimpleDateFormat("dd", Locale.ENGLISH)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            val date = sdf.parse(data.date!!)

            val monthName: String = month.format(date!!)
            val dayName = day.format(date)

            binding.txtMonth.text = monthName
            binding.txtDate.text = dayName

            binding.txtTitle.text = data.name
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }

    fun setData(event: ArrayList<AnniversaryEvent>) {
        eventList.clear()
        eventList.addAll(event)
        notifyDataSetChanged()
    }
}