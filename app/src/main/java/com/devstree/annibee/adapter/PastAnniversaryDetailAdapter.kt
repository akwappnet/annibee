package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.RowUpcomingAnniBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PastAnniversaryDetailAdapter(
    val callback: (data: AnniversaryEvent) -> Unit
) : RecyclerView.Adapter<PastAnniversaryDetailAdapter.ViewHolder>() {

    private lateinit var mClickListener: ClickListener
    lateinit var binding: RowUpcomingAnniBinding

    private val mList: ArrayList<AnniversaryEvent> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RowUpcomingAnniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(
        val binding: RowUpcomingAnniBinding,
        val adapter: PastAnniversaryDetailAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AnniversaryEvent) {

            val month = SimpleDateFormat("MMM", Locale.ENGLISH)
            val day = SimpleDateFormat("dd", Locale.ENGLISH)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            val date = sdf.parse(data.date!!)

            val monthName: String = month.format(date!!)
            val dayName = day.format(date)

            binding.txtMonth.text = monthName
            binding.txtDay.text = dayName
            binding.txtTitle.text = data.name
            binding.txtName.text = data.note

            binding.rootView.setOnClickListener {
                adapter.callback.invoke(data)
            }

            when (data.type) {
                "1" -> {
                    binding.txtTitle.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.sky_blue)
                    )
                }
                "2" -> {
                    binding.txtTitle.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.light_brown)
                    )
                }
                else -> {
                    binding.txtTitle.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.green)
                    )
                }
            }
        }
    }

    fun setData(list: List<AnniversaryEvent>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}