package com.devstree.annibee.view

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.devstree.annibee.utility.glide.GlideUtil.BuildRequestOptionRounded
import com.devstree.annibee.utility.glide.GlideUtil.defaultRequestOption
import com.devstree.mediafilepicker.model.Media
import com.makeramen.roundedimageview.RoundedImageView

class CircularImageView : RoundedImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun setUrl(url: String?) {
        if (url.isNullOrEmpty()
            || url.equals("null", true)
            || url.equals("false", true)
        ) {
            return
        }

        Glide.with(context).load(url).apply(defaultRequestOption!!).into(this)
    }

    fun setUrl(url: String?, res_id: Int) {
        if (url.isNullOrEmpty()
            || url.equals("null", true)
            || url.equals("false", true)
        ) {
            set(res_id)
            return
        }
        Glide.with(context).load(url).apply(BuildRequestOptionRounded(res_id)).into(this)
    }

    fun set(res_id: Int) {
        Glide.with(this).load(res_id).into(this)
    }

    fun set(media: Media) {
        Glide.with(this).load(media.localFile).into(this)
    }

}