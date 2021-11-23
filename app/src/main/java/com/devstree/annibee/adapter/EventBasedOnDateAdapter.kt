package com.devstree.annibee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.ItemListBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventBasedOnDateAdapter(
    val context: Context
) : RecyclerView.Adapter<EventBasedOnDateAdapter.ViewHolder>() {

    private lateinit var mClickListener: ClickListener
    lateinit var binding: ItemListBinding
   /* var date: Int? = 0
    var month: Int? = 0
    private var year: Int? = 0*/
    val eventList: ArrayList<AnniversaryEvent> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (eventList[position].type == "1") {
            binding.card.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.sky_blue_alpha
                )
            )
        } else if (eventList[position].type == "2") {
            binding.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_pink))
        } else if (eventList[position].type == "3") {
//            binding.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
            binding.main.background = ContextCompat.getDrawable(context, R.color.light_green)
//            binding.card.setBackgroundResource(R.drawable.rectangle_light_green)
        }

        holder.bind(eventList[position], position)

        holder.itemView.setOnClickListener {
            mClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(val binding: ItemListBinding, val adapter: EventBasedOnDateAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AnniversaryEvent, position: Int) {
            binding.txtTitle.text = data.name


/*            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("MMM", Locale.ENGLISH)
            calendar.set(adapter.year!!, adapter.month!!, adapter.date!!)
            val dateString: String = sdf.format(calendar.time)
            binding.txtMonth.text = dateString
            binding.txtDate.text = adapter.date.toString()*/
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }

    fun setData(event: ArrayList<AnniversaryEvent>/*, date: Int = 0, month: Int = 0, year: Int = 0*/) {
        /*this.date = date
        this.month = month
        this.year = year*/
        eventList.clear()
        eventList.addAll(event)
        notifyDataSetChanged()
    }
}