package com.devstree.annibee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstree.annibee.databinding.ItemAddPhotoBinding
import com.devstree.annibee.model.response_model.Image

class AddPhotoAdapter(
    private val onItemClick : (position: Int) -> Unit
) : RecyclerView.Adapter<AddPhotoAdapter.Companion.ViewHolder>() {
    var photoList: ArrayList<Image?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAddPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        photoList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    companion object {
        class ViewHolder(val binding: ItemAddPhotoBinding, val adapter: AddPhotoAdapter) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Image) {
                Glide.with(itemView.context).asBitmap().load(data.image).into(binding.addImage)

                binding.imgClose.setOnClickListener {
                    adapter.onItemClick.invoke(adapterPosition)
                }
            }
        }
    }

    fun setData(list: ArrayList<Image?>) {
        photoList.clear()
        photoList.addAll(list)
        notifyDataSetChanged()
    }

}