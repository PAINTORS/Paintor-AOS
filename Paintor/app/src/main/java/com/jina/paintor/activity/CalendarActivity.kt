package com.jina.paintor.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.jina.paintor.R
import com.jina.paintor.adapter.ColorAdapter
import com.jina.paintor.calendar.ContinuousSelectionHelper
import com.jina.paintor.calendar.ContinuousSelectionHelper.getSelection
import com.jina.paintor.calendar.DateSelection
import com.jina.paintor.data.Location
import com.jina.paintor.database.MainViewModel
import com.jina.paintor.database.schedule.TripSchedule
import com.jina.paintor.databinding.ActivityCalendarBinding
import com.jina.paintor.databinding.ItemCalendarDayBinding
import com.jina.paintor.tripscheudule.ScheduleManager
import com.jina.paintor.utils.TAG
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
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
    private val viewModel: MainViewModel by viewModels()
    private var selectDate = DateSelection()
    private var today = LocalDate.now()
    private var location: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val isNewSchedule = intent.getBooleanExtra(ScheduleManager.SELECT_LOCATION, false)
        binding.isNewPlan = isNewSchedule
        binding.layoutSelectSchedule.isNewPlan = isNewSchedule
        if (!isNewSchedule) { // All plan
            binding.includeToolbar.toolbarTitle = "여행 계획"
            binding.includeToolbar.ivNewPlan.visibility = View.VISIBLE
            binding.includeToolbar.ivNewPlan.setOnClickListener {
                startActivity(Intent(this, SearchLocationActivity::class.java))
            }
            setCalendarView()
        } else { // new plan
            binding.includeToolbar.toolbarTitle = "일정 등록"
            binding.includeToolbar.ivNewPlan.visibility = View.GONE

            binding.layoutSelectSchedule.rvColor.apply {
                layoutManager = GridLayoutManager(this@CalendarActivity, 4)
                adapter = ColorAdapter(this@CalendarActivity)
            }


            ScheduleManager.selectedLocation.observe(this, Observer {
                // 여기 이미 옵저빙을 하고 있으니까....... 여기서 한번 확인해봐죽?
                setCalendarView(it)
                location = it
                binding.layoutSelectSchedule.tvCountry.text = it.countryName
                binding.layoutSelectSchedule.tvCity.text = it.cityName
            })
        }

        binding.tvToday.setOnClickListener {
            binding.calendarView.smoothScrollToMonth(YearMonth.now())
        }
    }

    private fun setCalendarView(selectLocation: Location? = null) {
        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = ItemCalendarDayBinding.bind(view)

            init {
                if (selectLocation != null) {
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
        }

        // 요일
        binding.layoutDayOfWeek.root.children.map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

        binding.calendarView.apply {
            dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
                    bindDate(data, container.binding.tvDay, container.binding.viewRange)
                }
            }
            monthScrollListener = { updateMonth() }
            setup(startMonth, endMonth, daysOfWeek().first())
            scrollToMonth(currentMonth)
        }
    }

    fun bindDate(data: CalendarDay, textView: TextView, viewRange: View) {
        val (startDate, endDate) = selectDate

        fun resetViews(color: Int) {
            textView.setTextColor(color)
            textView.background = null
            viewRange.background = null

            binding.btnFinish.apply {
                this.isEnabled = false
                this.setBackgroundColor(getColor(R.color.disable_color))
            }
        }

        when (data.position) {
            DayPosition.MonthDate -> {
                textView.text = data.date.dayOfMonth.toString()
                when {
                    // Start Date 를 클릭 시 해당 스코프를 진행
                    startDate == data.date && endDate == null -> {
                        Logger.t(TAG.CALENDAR).i("Start Date ${data.date}")
                        textView.setTextColor(Color.WHITE)
                        textView.setBackgroundResource(R.drawable.shape_bg_single_day)
                        binding.layoutSelectSchedule.tvStartDate.text = data.date.toString()
                        binding.layoutSelectSchedule.tvEndDate.text = ""
                    }
                    // End Date 를 클릭 시 해당 스코프를 진행
                    data.date == startDate -> {
                        Logger.t(TAG.CALENDAR).i("Start Date 인데 endDate 클릭 시 지정 됨 ${data.date}")
                        textView.setBackgroundResource(R.drawable.shape_bg_single_day)
                        viewRange.visibility = View.VISIBLE
                        viewRange.setBackgroundResource(R.drawable.shape_bg_start_day)
                    }
                    // Range Date
                    startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                        Logger.t(TAG.CALENDAR).d("Range Date : ${data.date}")
                        textView.setTextColor(Color.DKGRAY)
                        viewRange.visibility = View.VISIBLE
                        viewRange.setBackgroundResource(R.drawable.shape_bg_range_day)
                    }
                    // end date 클릭
                    data.date == endDate -> {
                        Logger.t(TAG.CALENDAR).i("endDate : ${data.date}")
                        textView.setTextColor(getColor(R.color.white))
                        textView.setBackgroundResource(R.drawable.shape_bg_single_day)
                        viewRange.visibility = View.VISIBLE
                        viewRange.setBackgroundResource(R.drawable.shape_bg_end_day)
                        binding.layoutSelectSchedule.tvEndDate.text = data.date.toString()
                    }

                    data.date == today -> {
                        Logger.t(TAG.CALENDAR).d("today : ${data.date}")
                        textView.setTextColor(getColor(R.color.main_color))
                        resetViews(R.color.main_color)
                    }

                    data.date.dayOfWeek == DayOfWeek.SUNDAY && !data.date.isBefore(today) -> {
                        textView.setTextColor(Color.RED)
                        resetViews(Color.RED)
                    }

                    data.date.dayOfWeek == DayOfWeek.SATURDAY && !data.date.isBefore(today) -> {
                        textView.setTextColor(Color.BLUE)
                        resetViews(Color.BLUE)
                    }

                    data.date.isBefore(today) -> {
                        textView.setTextColor(Color.GRAY)
                    }


                    else -> {
                        resetViews(Color.BLACK)
                    }
                }
            }

            DayPosition.InDate, DayPosition.OutDate -> {
                val isInDate = data.position == DayPosition.InDate &&
                        startDate != null && endDate != null &&
                        ContinuousSelectionHelper.isInDateBetweenSelection(
                            data.date,
                            startDate,
                            endDate
                        )
                val isOutDate = data.position == DayPosition.OutDate &&
                        startDate != null && endDate != null &&
                        ContinuousSelectionHelper.isOutDateBetweenSelection(
                            data.date,
                            startDate,
                            endDate
                        )

                if (isInDate || isOutDate) {
                    Logger.t(TAG.CALENDAR).d("${data.position} : ${data.date}")
                    viewRange.setBackgroundResource(R.drawable.shape_bg_range_day)
                    textView.visibility = View.INVISIBLE
                }
            }
        }

        if (startDate != null && endDate != null) {
            binding.layoutSelectSchedule.rvColor.visibility = View.VISIBLE
            // TODO : 10/22 btnFisnish 는 color 까지 지정 되면!
            ScheduleManager.selectedColor.observe(this, Observer { color ->
                binding.btnFinish.apply {
                    color?.let {
                        this.isEnabled = true
                        this.setBackgroundColor(getColor(R.color.main_color))
                        this.setOnClickListener {
                            val tripSchedule = TripSchedule(
                                placeId = location?.placeId.toString() ?: "",
                                countryName = location?.countryName.toString(),
                                cityName = location?.cityName.toString() ?: "",
                                latitude = "",
                                longitude = "",
                                tripStatus = false,
                                tripColor = color,
                                startDate = startDate.toString(),
                                endDate = endDate.toString(),
                                saveTime = System.currentTimeMillis(),
                                updateTime = null,
                                deleteTime = null
                            )
                            viewModel.insertSchedule(tripSchedule)
                            val intent = Intent(this@CalendarActivity, CalendarActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } ?: run {
                        Logger.t(TAG.CALENDAR).i("do nothing")
                        this.isEnabled = false
                        this.setBackgroundColor(getColor(R.color.disable_color))
                    }
                }
            })
        }
    }

    private fun updateMonth() {
        val month = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.tvMonth.text = month.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }
}