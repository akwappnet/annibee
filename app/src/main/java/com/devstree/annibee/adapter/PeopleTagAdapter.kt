package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.devstree.annibee.databinding.ItemTagBinding

class PeopleTagAdapter(
    private val onItemClick: (position: Int, data: String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var arrayList: ArrayList<String?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataViewHolder(
            ItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), this
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (viewHolder is DataViewHolder) {
            viewHolder.bind(arrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    companion object {
        class DataViewHolder(
            val binding: ItemTagBinding,
            val adapter: PeopleTagAdapter
        ) :
            ViewHolder(binding.root) {
            fun bind(data: String?) {

                binding.txtName.text = data

                binding.btnClose.setOnClickListener {
                    adapter.onItemClick.invoke(adapterPosition, "")
                }
            }
        }
    }

    fun setItem(list: List<String>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }
}