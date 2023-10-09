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
import com.jina.paintor.calendar.ContinuousSelectionHelper
import com.jina.paintor.calendar.ContinuousSelectionHelper.getSelection
import com.jina.paintor.calendar.DateSelection
import com.jina.paintor.databinding.ActivityCalendarBinding
import com.jina.paintor.databinding.ItemCalendarDayBinding
import com.jina.paintor.utils.TAG
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
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
    private var selectDate = DateSelection()
    private var today = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.includeToolbar.toolbarTitle = "여행 계획"
        binding.includeToolbar.ivNewPlan.visibility = View.VISIBLE
        configureBinders()
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

    private fun configureBinders() {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = ItemCalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate &&
                        (day.date == today || day.date.isAfter(today))
                    ) {
                        selectDate = getSelection(
                            clickedDate = day.date,
                            dateSelection = selectDate,
                        )
                        this@CalendarActivity.binding.calendarView.notifyCalendarChanged()
                    }
                }
            }
        }

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.tvDay
                val viewRange = container.binding.viewRange

                viewRange.visibility = View.INVISIBLE

                val (startDate, endDate) = selectDate

                when (data.position) {
                    DayPosition.MonthDate -> {
                        textView.text = data.date.dayOfMonth.toString()
                        when {
                            startDate == data.date && endDate == null -> { // 이게 첫 날짜 선택
                                Logger.t(TAG.CALENDAR)
                                    .d("startDate == data.date && endDate == null\n${data.date}")
                                textView.setTextColor(getColor(R.color.white))
                                textView.setBackgroundResource(R.drawable.shape_bg_single_day)
                            }

                            data.date == startDate -> {
                                // end date 클릭 시 이거 호출
                                Logger.t(TAG.CALENDAR)
                                    .d(" data.date == startDate == null\n${data.date}")
                                textView.setTextColor(getColor(R.color.white))
                                textView.setBackgroundResource(R.drawable.shape_bg_single_day)
                                viewRange.visibility = View.VISIBLE
                                viewRange.setBackgroundResource(R.drawable.shape_bg_start_day)
                            }

                            startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                                // end date 클릭 시 이거 호출
                                Logger.t(TAG.CALENDAR)
                                    .d("startDate != null && endDate != null && (data.date > startDate && data.date < endDate) \n${data.date}")
                                textView.setTextColor(Color.DKGRAY)
                                viewRange.visibility = View.VISIBLE
                                viewRange.setBackgroundResource(R.drawable.shape_bg_range_day)
                            }

                            data.date == endDate -> { // end date 클릭
                                Logger.t(TAG.CALENDAR)
                                    .d("data.date == endDate\n${data.date}")
                                textView.setTextColor(getColor(R.color.white))
                                textView.setBackgroundResource(R.drawable.shape_bg_single_day)
                                viewRange.visibility = View.VISIBLE
                                viewRange.setBackgroundResource(R.drawable.shape_bg_end_day)
                            }

                            data.date == today -> {
                                Logger.t(TAG.CALENDAR)
                                    .d("data.date == today\n${data.date}")
                                textView.setTextColor(getColor(R.color.main_color))
                            }

                            else -> textView.setTextColor(getColor(R.color.black))
                        }
                    }

                    DayPosition.InDate -> {
                        if (startDate != null && endDate != null && ContinuousSelectionHelper.isInDateBetweenSelection(
                                data.date,
                                startDate,
                                endDate
                            )
                        ) {
                            viewRange.setBackgroundResource(R.drawable.shape_bg_single_day)
                        }
                    }

                    DayPosition.OutDate -> {
                        if (startDate != null && endDate != null && ContinuousSelectionHelper.isOutDateBetweenSelection(
                                data.date,
                                startDate,
                                endDate
                            )
                        ) {
                            viewRange.setBackgroundResource(R.drawable.shape_bg_single_day)
                        }
                    }

                }
            }
        }
    }


}