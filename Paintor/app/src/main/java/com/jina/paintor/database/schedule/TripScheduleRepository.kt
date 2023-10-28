package com.jina.paintor.database.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jina.paintor.database.tripHistory.TripHistory
import com.jina.paintor.utils.Application
import kotlinx.coroutines.withContext

class TripScheduleRepository {
    val db by lazy { Application.INSTANCE.database }
    private val dao = db.scheduleDao()
    val tripHistory : LiveData<MutableList<TripSchedule>> = dao.tripHistory()

    fun insertSchedule(schedule: TripSchedule) {
        dao.insertSchedule(schedule)
    }
}