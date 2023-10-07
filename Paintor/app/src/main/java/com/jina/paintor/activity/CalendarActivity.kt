package com.jina.paintor.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import com.jina.paintor.R
import com.jina.paintor.adapter.DayViewContainer
import com.jina.paintor.databinding.ActivityCalendarBinding
import com.jina.paintor.utils.TAG
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.orhanobut.logger.Logger
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class CalendarActivity : AppCompatActivity() {

    private val binding: ActivityCalendarBinding by lazy {
        ActivityCalendarBinding.inflate(layoutInflater)
    }
    private var startDate = LocalDate.now()
    private var selectDate: LocalDate? = startDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.includeToolbar.toolbarTitle = "여행 계획"
        binding.includeToolbar.ivNewPlan.visibility = View.VISIBLE

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val day = container.day
                val textView = container.tvDay
                container.tvDay.text = data.date.dayOfMonth.toString()
                container.tvDay.setTextColor(if (data.position == DayPosition.MonthDate) Color.BLACK else Color.GRAY)

                container.view.setOnClickListener {
                    Logger.t(TAG.CALENDAR).d("click~")
                    if (day.position == DayPosition.MonthDate) {
                        val currentSelection = selectDate
                        if (currentSelection == day.date) {
                            selectDate = null
                            binding.calendarView.notifyCalendarChanged()
                        } else {
                            selectDate = day.date
                            binding.calendarView.notifyDateChanged(day.date)
                            if (currentSelection != null) {
                                binding.calendarView.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }

                if (day.position == DayPosition.MonthDate) {
                    textView.visibility = View.VISIBLE
                    if (day.date == selectDate) {
                        textView.setTextColor(Color.WHITE)
                        textView.setBackgroundResource(R.drawable.shape_bg_select_day)
                    } else {
                        textView.setTextColor(Color.BLACK)
                        textView.background = null
                    }
                } else {
                    textView.visibility == View.INVISIBLE
                }

            }

            override fun create(view: View) = DayViewContainer(view)

        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        binding.layoutDayOfWeek.root.children.map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }
        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

    }
}