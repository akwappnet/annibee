package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstree.annibee.R
import com.devstree.annibee.databinding.ItemPhotoBinding
import com.devstree.annibee.model.response_model.Event
import com.devstree.annibee.model.response_model.Image

class EventPhotoAdapter : RecyclerView.Adapter<EventPhotoAdapter.ViewHolder>() {

    lateinit var binding: ItemPhotoBinding

    var event: Event? = null
    var photoList: ArrayList<Image> = ArrayList()
    private var mClickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        for (data in eventList) {
        event?.photos?.get(position)?.let { holder.bind(it) }
//        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(val binding: ItemPhotoBinding, val adapter: EventPhotoAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        //        fun bind(data: Event) {
        fun bind(image: Image) {

//            for (data in adapter.eventList) {
                if (adapter.mClickListener != null) {
                    binding.addImage.setOnClickListener {
                        adapter.event?.let { it1 -> adapter.mClickListener!!.onItemClick(it1, adapterPosition) }
                    }
                }

//                for (image in adapter.event?.photos!!) {
                    Glide.with(itemView.context).load(image.image).error(R.drawable.ic_user).into(binding.addImage)
//                }
//            }
        }
    }

    fun setData(event: Event, photos: List<Image>) {
        this.event = event

        photoList.clear()
        photoList.addAll(photos)

        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(item: Event, position: Int)
    }

}