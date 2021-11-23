package com.devstree.annibee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.ItemListBinding

class ListAdapter(
    val context: Context,
//    private val mList: List<UpcomingAnniversaryDetail>
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private lateinit var mClickListener: ClickListener
    lateinit var binding: ItemListBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* val data = mList[position]

         binding.txtMonth.text = data.month
         binding.txtDay.text = data.day
         binding.txtTitle.text = data.title
         binding.txtName.text = data.name*/

        if (position != 0) {
            binding.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.sky_blue_alpha))
        }else {
            binding.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_pink))
        }

        holder.itemView.setOnClickListener {
            mClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}