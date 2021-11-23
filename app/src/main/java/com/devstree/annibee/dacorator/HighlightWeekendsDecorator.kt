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
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Month


/**
 * Highlight Saturdays and Sundays with a background
 */
class HighlightWeekendsDecorator(val context: Context, val month: Int) : DayViewDecorator {
//    private var highlightDrawable: Drawable

    //    var weekDay: DayOfWeek? = DayOfWeek.SUNDAY
    override fun shouldDecorate(day: CalendarDay): Boolean {
        val weekDay = day.date.dayOfWeek
        /*if (weekDay === DayOfWeek.SATURDAY) {
            highlightDrawable = ColorDrawable(colorBlue)
            return true
        }
        if (weekDay === DayOfWeek.SUNDAY) {
            highlightDrawable = ColorDrawable(colorRed)
            return true
        }*/
        if (day.month == month) {
            return weekDay == DayOfWeek.SATURDAY
        }
        return false
    }

    override fun decorate(view: DayViewFacade) {
        /*if (weekDay == DayOfWeek.SATURDAY) {
            view.setBackgroundDrawable(ColorDrawable(Color.parseColor("#e7f8f8")))
        }
        if (weekDay == DayOfWeek.SUNDAY) {*/

        view.addSpan(ForegroundColorSpan(Color.BLUE))
//            view.setBackgroundDrawable(highlightDrawable)

    }

    /*companion object {
        private val colorBlue = Color.parseColor("#e7f8f8")
    }

    init {

        highlightDrawable = ColorDrawable(colorBlue)
    }*/
}