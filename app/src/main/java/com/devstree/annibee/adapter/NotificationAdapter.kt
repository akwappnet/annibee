package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.ItemNotificationBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent

class NotificationAdapter(
    val listener: (data: AnniversaryEvent) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.Companion.ViewHolder>() {

    private var notificationList: ArrayList<AnniversaryEvent> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    companion object {
        class ViewHolder(val binding: ItemNotificationBinding, val adapter: NotificationAdapter) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(data: AnniversaryEvent) {

                binding.llNotification.setOnClickListener {
                    adapter.listener.invoke(data)
                }

                when (data.type) {
                    "1" -> {
                        binding.txtNotification.text = data.anniversaryName
                        binding.txtNotification.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.sky_blue)
                        )
                    }
                    "2" -> {
                        binding.txtNotification.text = data.eventName
                        binding.txtNotification.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.light_brown)
                        )
                    }
                    else -> {
                        binding.txtNotification.text = data.anniversaryName
                        binding.txtNotification.setTextColor(
                            ContextCompat.getColor(itemView.context, R.color.green)
                        )
                    }
                }
            }
        }
    }

    fun setItem(list: List<AnniversaryEvent>) {
        notificationList.clear()
        notificationList.addAll(list)
        notifyDataSetChanged()
    }
}