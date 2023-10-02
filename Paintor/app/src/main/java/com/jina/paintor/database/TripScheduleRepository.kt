package com.jina.paintor.database

import com.jina.paintor.utils.Application

class TripScheduleRepository {
    val db by lazy { Application.INSTANCE.database }
    private val dao = db.scheduleDao()

    fun insertSchedule(schedule: TripSchedule) {
        dao.insertSchedule(schedule)
    }
}