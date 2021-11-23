package com.devstree.annibee.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.core.widget.ContentLoadingProgressBar

class ContentProgressBar : ContentLoadingProgressBar {
    var displayMetrics: DisplayMetrics? = null

    constructor(context: Context) : super(context) {
        setDisplayMatrix()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setDisplayMatrix()
    }

    private fun setDisplayMatrix() {
        displayMetrics = context.resources.displayMetrics
    }

    override fun onDraw(canvas: Canvas) {
        val marginTop = dpToPx(7)
        canvas.translate(0f, -marginTop.toFloat())
        super.onDraw(canvas)
    }

    fun dpToPx(dp: Int): Int {
        return Math.round(dp * (displayMetrics!!.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}