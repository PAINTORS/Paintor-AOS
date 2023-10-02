package com.jina.paintor.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val tripScheduleRepository = TripScheduleRepository()

    fun insertSchedule(schedule: TripSchedule) {
        CoroutineScope(Dispatchers.IO).launch {
            tripScheduleRepository.insertSchedule(schedule)
        }
    }
}