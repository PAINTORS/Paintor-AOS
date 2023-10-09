package com.jina.paintor.adapter

import android.view.View
import com.jina.paintor.databinding.ItemCalendarDayBinding
import com.jina.paintor.utils.TAG
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import com.orhanobut.logger.Logger

class DayViewContainer(view: View) : ViewContainer(view) {
    val tvDay = ItemCalendarDayBinding.bind(view).tvDay
    val viewRange = ItemCalendarDayBinding.bind(view).viewRange
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            Logger.t(TAG.CALENDAR).d("click~")
        } // TODO range selection
    }
}