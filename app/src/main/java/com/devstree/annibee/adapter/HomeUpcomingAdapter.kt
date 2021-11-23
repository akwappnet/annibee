package com.devstree.annibee.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.HomeItemUpcomingBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Log
import com.ibm.icu.text.RuleBasedNumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeUpcomingAdapter : RecyclerView.Adapter<HomeUpcomingAdapter.ViewHolder>() {

    lateinit var binding: HomeItemUpcomingBinding
    var upcomingList: ArrayList<AnniversaryEvent> = ArrayList()
    private lateinit var mClickListener: ClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            HomeItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(upcomingList[position])
    }

    override fun getItemCount(): Int {
        return upcomingList.size
    }

    @SuppressLint("SetTextI18n")
    class ViewHolder(val binding: HomeItemUpcomingBinding, val adapter: HomeUpcomingAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnniversaryEvent) {
            binding.txtName.text = item.name
            binding.txtDate.text = item.date

            binding.mainLayout.setOnClickListener {
                adapter.mClickListener.onItemClick(item, adapterPosition)
            }

            if (item.type == "1") {
                binding.txtName.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.sky_blue)
                )

               /* when (AppHelper.getAppLanguage()) {
                    Constants.ENGLISH -> {
                        binding.txtRemainDays.text = "(${item.remainDays} day remaining)"
                    }
                    Constants.JAPANESE -> {
                        binding.txtRemainDays.text = "(あと${item.remainDays}日)"
                    }
                    Constants.TRADITIONAL_CHINESE -> {

                        binding.txtRemainDays.text = "(剩餘${item.remainDays}天)"
                    }
                    Constants.SIMPLIFIED_CHINESE -> {

                        binding.txtRemainDays.text = "(剩余${item.remainDays}天)"
                    }
                }*/
                var dateDifference = item.totalYears;
                if(!item.date.isNullOrEmpty() && !item.date.isNullOrBlank()){
                    dateDifference = if(Calendar.getInstance().get(Calendar.YEAR) > Integer.valueOf( item.date!!.split("-")[0])){
                        (Calendar.getInstance().get(Calendar.YEAR) -  Integer.valueOf( item.date!!.split("-")[0])).toLong()
                    }else{
                        (Calendar.getInstance().get(Calendar.YEAR) -  Integer.valueOf( item.date!!.split("-")[0])).toLong()
                    }
                }

               Log.e("Year Diff@@@ $dateDifference")
                if(!dateDifference.toString().isNullOrEmpty() && dateDifference > 0 ) {
                    binding.txtRemainDays.text = when (AppHelper.getAppLanguage()) {
                        Constants.ENGLISH -> {
                            val formatter =
                                RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL)
                            formatter.format(dateDifference)
                        }
                        Constants.JAPANESE -> {
                            RuleBasedNumberFormat(Locale.JAPANESE, RuleBasedNumberFormat.ORDINAL)
                            "${dateDifference}回目"
                        }
                        Constants.TRADITIONAL_CHINESE -> {
                            RuleBasedNumberFormat(
                                Locale.TRADITIONAL_CHINESE,
                                RuleBasedNumberFormat.ORDINAL
                            )
                            "第${dateDifference}次"
                        }
                        Constants.SIMPLIFIED_CHINESE -> {
                            RuleBasedNumberFormat(
                                Locale.SIMPLIFIED_CHINESE,
                                RuleBasedNumberFormat.ORDINAL
                            )
                            "第${dateDifference}次"
                        }
                        else -> {
                            val formatter =
                                RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL)
                            formatter.format(dateDifference)
                        }
                    }
                }
            } else if (item.type == "2") {
                binding.txtName.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.light_brown)
                )
                binding.txtRemainDays.text = ""
                binding.txtDate.text = "${item.date} /\n${item.eventEndDate}"
            }
        }
    }

    fun setData(list: List<AnniversaryEvent>) {
        upcomingList.clear()
        upcomingList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(item: AnniversaryEvent, position: Int)
    }

}