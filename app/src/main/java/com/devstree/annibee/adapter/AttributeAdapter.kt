package com.devstree.annibee.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.devstree.annibee.R
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.model.response_model.Attribute
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import java.util.*

class AttributeAdapter(
    activity: Activity,
    dataSource: List<Attribute>,
    private val callBack: ObjectCallback<Attribute>
) : BaseAdapter() {
    private var mDataSource: List<Attribute> = ArrayList()
    private val layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return mDataSource.size
    }

    override fun getItem(position: Int): String {
        /*  return if (!mDataSource[position].parentName.isNullOrEmpty() ) {
              mDataSource[position].parentName.toString()
          } else {*/
        return when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                mDataSource[position].enName.toString()
            }
            Constants.JAPANESE -> {
                mDataSource[position].jaName.toString()
            }
            Constants.SIMPLIFIED_CHINESE -> {
                mDataSource[position].ch1Name.toString()
            }
            Constants.TRADITIONAL_CHINESE -> {
                mDataSource[position].ch2Name.toString()
            }
            else -> mDataSource[position].enName.toString()
        }
//        }
    }

    private fun getItemModel(position: Int): Attribute {
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
            holder.imgView?.visibility = View.VISIBLE.takeIf {
                holder.tvTitle?.text!!.toString() == view.resources.getString(R.string.default_anniversary)
            } ?: View.GONE
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