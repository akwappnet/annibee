package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.ItemAlertNotificationBinding
import com.devstree.annibee.model.response_model.AlertNotification

class AlertNotificationAdapter(): RecyclerView.Adapter<AlertNotificationAdapter.Companion.ViewHolder>() {

    private lateinit var mClickListener: ClickListener
    var notificationList: ArrayList<AlertNotification?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemAlertNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        notificationList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    companion object {
        class ViewHolder(val binding: ItemAlertNotificationBinding, val adapter: AlertNotificationAdapter): RecyclerView.ViewHolder(binding.root){
            fun bind(data: AlertNotification) {
                binding.checkbox.text = data.name

                binding.checkbox.isChecked = data.isChecked

                binding.checkbox.setOnClickListener {
                    adapter.mClickListener.onItemClick(data, binding.checkbox.isChecked)
//                    binding.checkbox.isChecked = !binding.checkbox.isChecked
                }
            }

        }
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(notification: AlertNotification, checked: Boolean)
    }

    fun setData(list: List<AlertNotification>?) {
        if (list != null) {
            notificationList.clear()
            notificationList.addAll(list)
            notifyDataSetChanged()
        }
    }

}