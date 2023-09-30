package com.jina.paintor.location

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking

class GpsTracker() : Service(), LocationListener {

    private lateinit var mContext: Context
    private lateinit var locationManager: LocationManager
    private lateinit var location: Location
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10
    var longitude = 0.0
    var latitude = 0.0

    constructor(context: Context) : this() {
        this.mContext = context
        getLocation()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        Logger.t(TAG.LOCATION).d("location $location")
    }


    fun getLocation(): Location? {
        try {
            locationManager =
                mContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
                else return null

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        location =
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                        if (location != null) {
                            latitude = location.getLatitude()
                            longitude = location.getLongitude()
                        }
                    }
                }


                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                            if (location != null) {
                                latitude = location.getLatitude()
                                longitude = location.getLongitude()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Logger.t(TAG.LOCATION).e("GPS Error\n${e.message}")
        }
        return location
    }

}