package com.jina.paintor.database.tripHistory

data class TripHistory(
    val area: String,
    val latitude: String,
    val longitude: String,
    val tripStatus: Boolean = false,
    val tripColor: Int
)
