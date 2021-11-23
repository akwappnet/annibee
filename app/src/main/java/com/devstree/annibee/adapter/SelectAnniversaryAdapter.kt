package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.ItemSelectAnniversaryBinding
import com.devstree.annibee.model.response_model.AlertNotification

class SelectAnniversaryAdapter(): RecyclerView.Adapter<SelectAnniversaryAdapter.Companion.ViewHolder>() {

    private lateinit var mClickListener: ClickListener
    var notificationList: ArrayList<AlertNotification?> = ArrayList()
    var lastSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = ItemSelectAnniversaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 5
    }

    companion object {
        class ViewHolder(val binding: ItemSelectAnniversaryBinding, val adapter: SelectAnniversaryAdapter): RecyclerView.ViewHolder(binding.root){
            fun bind(/*data: AlertNotification*/) {
                /*binding.radioButton.text = data.name

                binding.radioButton.isChecked = data.isChecked*/

                binding.radioButton.setOnClickListener {
                    adapter.mClickListener.onItemClick(/*binding.radioButton.isChecked,*/ layoutPosition)
//                    binding.checkbox.isChecked = !binding.checkbox.isChecked
                }
                binding.radioButton.isChecked = layoutPosition == adapter.lastSelectedPosition
            }

        }
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(/*notification: AlertNotification,*/ /*checked: Boolean, */position: Int)
    }

    fun setData(list: List<AlertNotification>?) {
        if (list != null) {
            notificationList.clear()
            notificationList.addAll(list)
            notifyDataSetChanged()
        }
    }

}