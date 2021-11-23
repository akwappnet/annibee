package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.ItemAnniversaryEventPhotoBinding
import com.devstree.annibee.model.response_model.Event

class AnniversaryEventPhotoAdapter(
    val onItemClickListener: EventPhotoAdapter.ClickListener

) : RecyclerView.Adapter<AnniversaryEventPhotoAdapter.ViewHolder>() {

    lateinit var binding: ItemAnniversaryEventPhotoBinding
    var pastlist: ArrayList<Event> = ArrayList()
//    private lateinit var mClickListener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAnniversaryEventPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pastlist[position])
    }

    override fun getItemCount(): Int {
        return pastlist.size
    }

    class ViewHolder(
        val binding: ItemAnniversaryEventPhotoBinding,
        val adapter: AnniversaryEventPhotoAdapter,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {

            if (item.photos != null) {
                val imageAdapter = EventPhotoAdapter()
                imageAdapter.setOnItemClickListener(adapter.onItemClickListener)
                binding.rvEventPhoto.adapter = imageAdapter

                imageAdapter.setData(item, item.photos!!)
            }
            else {

            }

            /*    binding.recyclerView.setOnClickListener {
                    adapter.mClickListener.onItemClick(layoutPosition, item)
                }*/

//            binding.layoutImage.visibility = View.VISIBLE.takeIf { item.photos?.size!! >= 2 } ?: View.GONE

            /*if (item.photos?.isNotEmpty() == true){
                binding.mainImage1.setUrl(item.photos?)

                when {
                    item.photos?.size!! == 2 -> {
                        binding.mainImage2.setUrl(item.photos?.get(1)?.image)
                        binding.mainImage3.visibility = View.GONE
                    }
                    item.photos?.size!! >= 3 -> {
                        binding.mainImage2.setUrl(item.photos?.get(1)?.image)
                        binding.mainImage3.setUrl(item.photos?.get(2)?.image)
                        binding.mainImage3.visibility = View.VISIBLE
                    }
                    else -> {

                    }
                }
            }else {

            }*/
        }
    }

    fun setData(list: List<Event>) {
        pastlist.clear()
        pastlist.addAll(list)
        notifyDataSetChanged()
    }

    /*fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, item: Event)
    }*/

}