package com.jina.paintor.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jina.paintor.R
import com.jina.paintor.databinding.FragmentLocationBinding

//import com.orhanobut.logger.Logger

class LocationFragment() : Fragment(), OnMapReadyCallback {

    private val binding: FragmentLocationBinding by lazy {
        FragmentLocationBinding.inflate(layoutInflater)
    }

    private lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.554891, 126.970814)

        mGoogleMap = googleMap
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능

        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15F)) //카메라 이동
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}