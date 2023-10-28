package com.jina.paintor.database.schedule

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jina.paintor.database.tripHistory.TripHistory

@Dao
interface TripScheduleDAO {
    @Insert
    fun insertSchedule(schedule: TripSchedule)

    @Query("SELECT * FROM TripScheduleTable")
    fun tripHistory(): LiveData<MutableList<TripSchedule>>
}