package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstree.annibee.databinding.ItemAddPhotoBinding
import com.devstree.annibee.model.GridViewItem

class AddEventPhotoAdapter: RecyclerView.Adapter<AddEventPhotoAdapter.ViewHolder>() {

    lateinit var binding: ItemAddPhotoBinding
    var photoList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAddPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).asBitmap().load(photoList[position]).into(binding.addImage)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(val binding: ItemAddPhotoBinding): RecyclerView.ViewHolder(binding.root){

    }

    fun setData(list: ArrayList<String>) {
        photoList.clear()
        photoList.addAll(list)
        notifyDataSetChanged()
    }

}