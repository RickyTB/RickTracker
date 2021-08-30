package com.rtb.ricktracker.ui.today

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.rtb.ricktracker.databinding.LayoutCalendarDayBinding
import java.time.LocalDate

class DayViewContainer(view: View, selectDate: (date: LocalDate) -> Unit) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = LayoutCalendarDayBinding.bind(view)

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                selectDate(day.date)
            }
        }
    }
}