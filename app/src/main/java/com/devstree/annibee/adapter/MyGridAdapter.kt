package com.devstree.annibee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devstree.annibee.databinding.GridItemBinding
import com.devstree.annibee.utility.BitmapHelper
import com.devstree.annibee.model.GridViewItem
import java.io.File

class MyGridAdapter(
    val context: Context,
    val items: List<GridViewItem>,
    /*val photoListener: PhotoListener*/
) :
    RecyclerView.Adapter<MyGridAdapter.ViewHolder>() {

    var selectedImageList: ArrayList<GridViewItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val  binding = GridItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    private fun removeImage(image: GridViewItem) {
        selectedImageList.remove(image)
    }

    private fun addImage(image: GridViewItem) {
        image.let { selectedImageList.add(it) }
    }

    override fun getItemCount(): Int {
       return items.size
    }

    class ViewHolder(val binding: GridItemBinding,val adapter: MyGridAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GridViewItem) {

            Glide.with(adapter.context).clear(binding.imageView)
            Glide.with(adapter.context).load(data.path).into(binding.imageView)

            binding.rlSelected.visibility = View.VISIBLE.takeIf { data.isSelected  } ?: View.GONE

            binding.root.setOnClickListener {
                data.isSelected = !data.isSelected
                adapter.notifyItemChanged(adapterPosition)
            }
        }
    }

    fun setImageFromPath(path: String, imageView: ImageView) {
        val imageFile = File(path)
        if (imageFile.exists()) {

            val bitmap = BitmapHelper.decodeBitmapFromFile(imageFile.absolutePath, 150, 150)
            imageView.setImageBitmap(bitmap)

        }
    }

    interface PhotoListener {
        fun onPhotoClick(path: String, v: View?)
    }

}