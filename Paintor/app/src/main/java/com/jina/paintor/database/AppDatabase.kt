package com.jina.paintor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jina.paintor.database.schedule.TripSchedule
import com.jina.paintor.database.schedule.TripScheduleDAO

@Database(version = 1, entities = [TripSchedule::class], exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): TripScheduleDAO

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "paintor.db"
                    ).build()
                }
            }
            return instance!!
        }
    }
}