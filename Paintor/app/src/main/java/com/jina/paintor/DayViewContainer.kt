package com.jina.paintor

import android.view.View
import com.jina.paintor.databinding.CalendarDayLayoutBinding
import com.jina.paintor.utils.TAG
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.ViewContainer
import com.orhanobut.logger.Logger
import java.time.LocalDate

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    lateinit var day: CalendarDay
    private var selectDate: LocalDate? = null

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