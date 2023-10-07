package com.jina.paintor.adapter

import android.view.View
import com.jina.paintor.databinding.ItemCalendarDayBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val tvDay = ItemCalendarDayBinding.bind(view).tvDay
    lateinit var day: CalendarDay

    init {
//        view.setOnClickListener {
//            Logger.t(TAG.CALENDAR).d("click~")
//            if (day.position == DayPosition.MonthDate) {
//                val currentSelection = selectDate
//                if (currentSelection == day.date) {
//                    selectDate = null
//                }
//            }
//        }
    }
}