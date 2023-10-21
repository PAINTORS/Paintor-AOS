package com.jina.paintor.tripscheudule

import androidx.lifecycle.MutableLiveData
import com.jina.paintor.data.Location

object ScheduleManager {
    var selectedLocation: MutableLiveData<Location> = MutableLiveData()
}