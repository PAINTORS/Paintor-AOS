package com.jina.paintor.tripscheudule

import androidx.lifecycle.MutableLiveData
import com.jina.paintor.data.Location

object ScheduleManager {
    val SELECT_LOCATION = "selectLocation"
    var selectedLocation: MutableLiveData<Location> = MutableLiveData()
    var selectedColor: MutableLiveData<Int> = MutableLiveData()
}