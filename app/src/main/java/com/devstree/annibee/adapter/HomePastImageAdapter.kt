package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstree.annibee.databinding.GridItemBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.Image

class HomePastImageAdapter(
    private var mList: List<Image>,
    private val item: AnniversaryEvent,
) :
    RecyclerView.Adapter<HomePastImageAdapter.ViewHolder>() {

    lateinit var binding: GridItemBinding
    private lateinit var mClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(val binding: GridItemBinding, val adapter: HomePastImageAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Image) {

            binding.imageView.setOnClickListener {
                adapter.mClickListener.onItemClick(
                    adapter.item,
                    adapterPosition
                )
            }

            Glide.with(itemView.context).load(item.image).thumbnail(0.5F)
                .into(binding.imageView)
        }
    }

    fun setOnItemClickListener(clickListener: ItemClickListener) {
        mClickListener = clickListener
    }

    interface ItemClickListener {
        fun onItemClick(item: AnniversaryEvent, position: Int)
    }
}