package com.devstree.annibee.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.R
import com.devstree.annibee.databinding.HomeItemPastBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class HomePastAdapter(
    val onItemClickListener: HomePastImageAdapter.ItemClickListener

) : RecyclerView.Adapter<HomePastAdapter.ViewHolder>() {

    lateinit var binding: HomeItemPastBinding
    var pastlist: ArrayList<AnniversaryEvent> = ArrayList()
    private lateinit var mClickListener: ClickListener
    private var position: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HomeItemPastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pastlist[position], position)
    }

    override fun getItemCount(): Int {
        return pastlist.size
    }

    class ViewHolder(
        val binding: HomeItemPastBinding,
        val adapter: HomePastAdapter,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnniversaryEvent, position: Int) {

            if (item.isAdvertise == 0.toLong()) {

                binding.card.visibility = View.VISIBLE
                binding.adView.visibility = View.GONE

                binding.txtName.text = item.name
                binding.txtDate.text = item.date

                if (item.photos != null) {
                    val imageAdapter = HomePastImageAdapter(item.photos!!, item)
                    imageAdapter.setOnItemClickListener(adapter.onItemClickListener)
                    binding.recyclerView.adapter = imageAdapter
//                    binding.recyclerView.clipToPadding = false
//                    binding.recyclerView.setPadding(0,0,60,0)
                }

                if (item.type == "1") {
                    binding.txtName.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.sky_blue
                        )
                    )
                } else if (item.type == "2") {
                    binding.txtName.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.light_brown
                        )
                    )
                    binding.txtDate.text = "${item.date} /\n${item.eventEndDate}"
                }
            } else {

                binding.card.visibility = View.GONE

                if (position != 0){
                    binding.adView.visibility = View.VISIBLE
                    adapter.adView(itemView.context)
                }
                else{
                    binding.adView.visibility = View.GONE
                }
            }

            binding.card.setOnClickListener {
                adapter.mClickListener.onItemClick(layoutPosition, item)
            }


                /*if (adapter.pastlist.size <= 5) {
                    if (adapter.position == adapter.pastlist.size - 1) {
                        adapter.adView(itemView.context)
                        adapter.position = 0
                    } else {
                        binding.adView.visibility = View.GONE
                        ++adapter.position
                    }
                } else {
                    binding.adView.visibility = View.GONE
                    if ((position + 1) % 5 == 0 && position != 0) {
                        adapter.adView(itemView.context)
                    } else {
                        binding.adView.visibility = View.GONE
                    }
                }*/

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

    fun adView(context: Context) {
        binding.adView.visibility = View.VISIBLE
        MobileAds.initialize(context)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    fun setData(list: List<AnniversaryEvent>) {
        pastlist.clear()
        pastlist.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, item: AnniversaryEvent)
    }

}