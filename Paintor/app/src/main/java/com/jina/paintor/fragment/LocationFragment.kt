package com.jina.paintor.fragment

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.jina.paintor.R
import com.jina.paintor.databinding.FragmentLocationBinding
import com.jina.paintor.location.GpsTracker
import com.jina.paintor.utils.TAG

import com.orhanobut.logger.Logger
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.Locale

class LocationFragment(val mContext: Context) : Fragment(), OnMapReadyCallback {

    private val binding: FragmentLocationBinding by lazy {
        FragmentLocationBinding.inflate(layoutInflater)
    }
    private lateinit var mGoogleMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var gpsTracker: GpsTracker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        val option = GoogleMapOptions().mapId("3de7cfc68cf7b3e9")
        val mapFragment = SupportMapFragment.newInstance(option)
        mapFragment.getMapAsync(this)
        childFragmentManager.beginTransaction().replace(R.id.mapView, mapFragment).commit()
        getLocation()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ivMyLocation.setOnClickListener {
            getLocation()
            moveToMyLocation()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        val location = LatLng(latitude, longitude)
        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 6F))
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun moveToMyLocation() {
        if (::mGoogleMap.isInitialized) {
            val targetLocation = LatLng(latitude, longitude)
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 6F))
        }
    }

    private fun getLocation() {
        gpsTracker = GpsTracker(mContext)

        latitude = gpsTracker!!.latitude
        longitude = gpsTracker!!.longitude

        Logger.t(TAG.LOCATION).d("location :: $latitude, $longitude")
    }
}