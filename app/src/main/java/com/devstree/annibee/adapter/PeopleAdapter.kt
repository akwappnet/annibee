package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.ItemPeopleBinding

class PeopleAdapter : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    lateinit var binding: ItemPeopleBinding
    var peopleList: ArrayList<String> = ArrayList()
    var isShowAll: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(peopleList[position])
    }

    override fun getItemCount(): Int {
        if (peopleList.size >= 3) {
            return if (isShowAll) {
                peopleList.size
            }else 3
        }
        else {
            return peopleList.size
        }
    }

    class ViewHolder(val binding: ItemPeopleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            binding.txtPeopleName.text = str
        }

    }

    fun setData(list: List<String>, isShowAll: Boolean = false) {
        peopleList.clear()
        peopleList.addAll(list)

        this.isShowAll = isShowAll

        notifyDataSetChanged()
    }

}