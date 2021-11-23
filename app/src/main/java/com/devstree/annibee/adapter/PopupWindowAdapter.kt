package com.devstree.annibee.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.devstree.annibee.R
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.utility.getClassName
import java.util.*

class PopupWindowAdapter(
    activity: Activity,
    dataSource: List<PopUpModel>,
    private val callBack: ObjectCallback<PopUpModel>
) : BaseAdapter() {
    private var mDataSource: List<PopUpModel> = ArrayList()
    private val layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return mDataSource.size
    }

    override fun getItem(position: Int): String {
        return mDataSource[position].title
    }

    private fun getItemModel(position: Int): PopUpModel {
        return mDataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = layoutInflater.inflate(R.layout.item_list_popup_menu, null)
            holder.tvTitle = view.findViewById(R.id.textview)
            holder.imgView = view.findViewById(R.id.imageView)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.tvTitle?.text = getItem(position)

        if (view != null) {
            holder.imgView?.visibility = View.VISIBLE.takeIf { holder.tvTitle?.text!!.toString() == view.resources.getString(R.string.default_anniversary) } ?: View.GONE
        }

        holder.tvTitle?.setOnClickListener { callBack.response(getItemModel(position)) }
        return view
    }

    class ViewHolder {
        var tvTitle: TextView? = null
        var imgView: ImageView? = null
    }

    init {
        mDataSource = dataSource
        layoutInflater = activity.layoutInflater
    }
}