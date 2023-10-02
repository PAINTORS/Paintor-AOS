package com.jina.paintor.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TripScheduleDAO {
    @Insert
    fun insertSchedule(schedule: TripSchedule)
}