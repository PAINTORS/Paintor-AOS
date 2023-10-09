package com.jina.paintor.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jina.paintor.database.schedule.TripSchedule
import com.jina.paintor.database.schedule.TripScheduleRepository
import com.jina.paintor.database.tripHistory.TripHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val tripScheduleRepository = TripScheduleRepository()
    private val _tripHistory = tripScheduleRepository.tripHistory
    val tripHistory: LiveData<MutableList<TripHistory>>
        get() = _tripHistory

    fun insertSchedule(schedule: TripSchedule) {
        CoroutineScope(Dispatchers.IO).launch {
            tripScheduleRepository.insertSchedule(schedule)
        }
    }
}