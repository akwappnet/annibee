package com.devstree.annibee.dacorator

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.DayOfWeek


/**
 * Highlight and Sundays which is not in current month with a background
 */
class SundayDecorator(val context: Context, val month: Int) : DayViewDecorator {

    private val colorRed = Color.parseColor("#ffebeb")
    var weekDay: CalendarDay? = null

    override fun shouldDecorate(day: CalendarDay): Boolean {
//        weekDay = day
        val dayOfWeek = day.date.dayOfWeek
        if (day.date.monthValue != month) {
            return dayOfWeek == DayOfWeek.SUNDAY
        }
        return false
    }

    override fun decorate(view: DayViewFacade) {

//        if (weekDay?.month != month) {
//            view.addSpan(ForegroundColorSpan(Color.RED))
//        }
//        else {
            view.addSpan(ForegroundColorSpan(Color.argb(90, 255, 0, 0)))
//        }
//            view.setBackgroundDrawable(highlightDrawable)
    }
}