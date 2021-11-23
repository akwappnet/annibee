package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstree.annibee.databinding.ItemEventPhotoBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.Event
import com.devstree.annibee.model.response_model.Image

class PhotoAdapter(
    val listener: (image: Image, position: Int) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    lateinit var binding: ItemEventPhotoBinding

    var photoList: ArrayList<Image> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemEventPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photoList, position)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(val binding: ItemEventPhotoBinding, val adapter: PhotoAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: List<Image>, position: Int) {
            Glide.with(itemView.context).asBitmap().load(data[position].image).into(binding.addImage)

            binding.addImage.setOnClickListener{
                adapter.listener.invoke(data[position], adapterPosition)
            }
        }

    }

    fun setData(list: List<Image>) {
        photoList.clear()
        photoList.addAll(list)
        notifyDataSetChanged()
    }

}