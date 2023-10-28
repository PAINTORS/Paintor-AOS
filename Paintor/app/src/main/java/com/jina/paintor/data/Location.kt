package com.jina.paintor.data

import org.json.JSONObject

data class Location(
    var placeId: CharSequence,
    var cityName: CharSequence,
    val countryName: CharSequence,
    var fullName: CharSequence,
    val placeColor: Int = 0
) {

    override fun toString(): String {
        val obj = JSONObject()
        obj.put("placeId", placeId)
        obj.put("cityName", cityName)
        obj.put("countryName", countryName)
        obj.put("fullName", fullName)
        return obj.toString()
    }
}