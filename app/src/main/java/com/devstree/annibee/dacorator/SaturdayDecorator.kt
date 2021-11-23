package com.devstree.annibee.dacorator

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.threeten.bp.DayOfWeek


/**
 * Highlight and Saturday which is not in current month with a background
 */
class SaturdayDecorator(val context: Context, val month: Int) : DayViewDecorator {

    private val colorRed = Color.parseColor("#ffebeb")
    var weekDay: CalendarDay? = null

    override fun shouldDecorate(day: CalendarDay): Boolean {
//        weekDay = day
        val dayOfWeek = day.date.dayOfWeek
        if (day.date.monthValue != month) {
            return dayOfWeek == DayOfWeek.SATURDAY
        }
        return false
    }

    override fun decorate(view: DayViewFacade) {

//        if (weekDay?.month != month) {
//            view.addSpan(ForegroundColorSpan(Color.RED))
//        }
//        else {
            view.addSpan(ForegroundColorSpan(Color.argb(90, 0, 0, 255)))
//        }
//            view.setBackgroundDrawable(highlightDrawable)
    }
}