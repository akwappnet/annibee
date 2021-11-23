package com.devstree.annibee.model

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay

data class DateEventModel(
    val date: CalendarDay,
    val event: List<EventModel>
)

data class EventModel(
    val color: Int,
    val name: String
)
