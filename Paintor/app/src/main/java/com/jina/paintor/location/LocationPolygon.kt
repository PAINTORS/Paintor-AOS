package com.jina.paintor.location

sealed class LocationPolygon(val number: Int) {

    open fun returnPolygon():String {
        return ""
    }

    object KOREA : LocationPolygon(1) {
        override fun returnPolygon(): String {
            return super.returnPolygon()
        }
    }
}