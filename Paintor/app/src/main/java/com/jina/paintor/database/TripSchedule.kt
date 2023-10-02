package com.jina.paintor.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "TripScheduleTable", primaryKeys = ["id", "area", "latitude", "longitude"])
data class TripSchedule(
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "area")
    val area: String,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "startDate")
    var startDate: Long?,
    @ColumnInfo(name = "endDate")
    var endDate: Long?,
    @ColumnInfo(name = "saveTime")
    var saveTime: Long,
    @ColumnInfo(name = "updateTime")
    var updateTime: Long?,
    @ColumnInfo(name = "deleteTime")
    var deleteTime: Long?
) {
}