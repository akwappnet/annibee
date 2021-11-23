package com.devstree.annibee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.devstree.annibee.databinding.ItemFaqBinding
import com.devstree.annibee.model.response_model.FAQ

class FaqAdapter() : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    lateinit var binding: ItemFaqBinding
    private lateinit var mClickListener: ClickListener
    private var faqList = ArrayList<FAQ>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(faqList[position])
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class ViewHolder(val binding: ItemFaqBinding, val adapter: FaqAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(faq: FAQ) {
            binding.txtQuestion.text = faq.question
            binding.txtAnswer.text = HtmlCompat.fromHtml(faq.answer.toString(), 0)
        }
    }

    fun setItem(list: List<FAQ>) {
        faqList.clear()
        faqList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        mClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}