package com.jina.paintor.fragment

import android.content.Context
import android.os.Bundle
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

import com.orhanobut.logger.Logger
import org.json.JSONException
import java.io.IOException
import java.io.InputStream

class LocationFragment(val mContext: Context) : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentLocationBinding
    private lateinit var mGoogleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        val option = GoogleMapOptions().mapId("3de7cfc68cf7b3e9")
        val mapFragment = SupportMapFragment.newInstance(option)
        childFragmentManager.beginTransaction().replace(R.id.mapView, mapFragment).commit()
        val location = LatLng(37.554891, 126.970814)
        mGoogleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }
}