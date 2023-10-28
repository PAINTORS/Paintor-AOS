package com.jina.paintor.database.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "TripScheduleTable")
data class TripSchedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "placeId")
    val placeId: String,
    @ColumnInfo(name = "countryName")
    val countryName: String,
    @ColumnInfo(name = "cityName")
    val cityName: String,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "tripStatus")
    var tripStatus: Boolean,
    @ColumnInfo(name = "tripColor")
    var tripColor: Int,
    @ColumnInfo(name = "startDate")
    var startDate: String?,
    @ColumnInfo(name = "endDate")
    var endDate: String?,
    @ColumnInfo(name = "saveTime")
    var saveTime: Long,
    @ColumnInfo(name = "updateTime")
    var updateTime: Long?,
    @ColumnInfo(name = "deleteTime")
    var deleteTime: Long?
) {
}