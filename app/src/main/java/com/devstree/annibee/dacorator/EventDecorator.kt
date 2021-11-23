package com.devstree.annibee.dacorator

import com.devstree.annibee.model.response_model.CalendarData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


open class EventDecorator(
//    private val color: Int,
    val days: ArrayList<String>,
    val months: ArrayList<String>,
    val dates: ArrayList<String>,
    var events: ArrayList<CalendarData>
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return days.contains(day.day.toString()) && months.contains(day.month.toString())
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(CalendarViewSpan(events))
    }
}