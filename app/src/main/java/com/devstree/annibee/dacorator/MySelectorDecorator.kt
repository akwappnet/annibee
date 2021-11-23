package com.devstree.annibee.dacorator

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.devstree.annibee.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


/**
 * Use a custom selector
 */
class MySelectorDecorator(context: Context) : DayViewDecorator {

    private val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.my_selector)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
    }
}
