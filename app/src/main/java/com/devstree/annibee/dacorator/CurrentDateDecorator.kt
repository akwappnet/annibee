package com.devstree.annibee.dacorator

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.devstree.annibee.R

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*


class CurrentDateDecorator(context: Context) : DayViewDecorator{


    private var highlightDrawable: Drawable? = null
    private val color: Int = ContextCompat.getColor(context, R.color.light_pink)
    private var date: CalendarDay? = null

    init {
        highlightDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_sky_blue)
        date = CalendarDay.today()
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return date != null && day == date
    }

    override fun decorate(view: DayViewFacade) {
//        view.addSpan(ForegroundColorSpan(Color.WHITE))
        view.setSelectionDrawable(highlightDrawable!!)
    }
}