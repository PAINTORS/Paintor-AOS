package com.jina.paintor.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import com.jina.paintor.R
import com.jina.paintor.database.MainViewModel
import com.jina.paintor.databinding.FragmentLocationBinding
import com.jina.paintor.location.GpsTracker
import com.jina.paintor.location.LocationPolygon
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger

class LocationFragment(val mContext: Context) : Fragment(), OnMapReadyCallback {

    private val binding: FragmentLocationBinding by lazy {
        FragmentLocationBinding.inflate(layoutInflater)
    }
    private lateinit var mGoogleMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var gpsTracker: GpsTracker? = null
    private val viewModel: MainViewModel by viewModels()
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
        addKmlLayer()
    }

    private fun addKmlLayer() {
        /**
         * 1. https://gadm.org/download_country.html 해당 링크에서 KMZ 파일 저장할 수 있음. ( level1 )
         * 2. https://mygeodata.cloud/converter/kmz-to-kml 해당 링크에서 KMZ 파일을 KML 파일로 변환.
         * 3. 현재 서버가 없는 관계로 Stringbuffer 로 직접 해야하는데, 사실상 이게 가장 좋은 방법인 듯 함.
         *      -> 영역을 색칠하거나 색을 변경할 때 유용할 것 같음.
         * */
        // TODO : 정리 필요.
        viewModel.tripHistory.observe(this, Observer { tripHistorys ->
            // 1. 여행 일정을 먼저 등록. -> 지역 검색

            if (tripHistorys.isNotEmpty()) {
                val sb = StringBuffer()
                sb.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">")
                sb.append("<Document id=\"root_doc\">")
                sb.append("<Schema name=\"gadm41_KOR_1\" id=\"gadm41_KOR_1\">")
                sb.append("<SimpleField name=\"GID_1\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"GID_0\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"COUNTRY\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"NAME_1\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"VARNAME_1\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"NL_NAME_1\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"TYPE_1\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"ENGTYPE_1\" type=\"string\"></SimpleField>")
                sb.append("<SimpleField name=\"CC_1\" type=\"string\"></SimpleField>")
                sb.append("</Schema>")
                sb.append("<Folder><name>gadm41_KOR_1</name>")
                for (tripArea in tripHistorys) {
                    val locationPolygon = LocationPolygon.findByCityName(tripArea.area)
                    sb.append(locationPolygon.returnPolygon(tripArea.tripStatus, tripArea.tripColor))
                }
                sb.append("</Folder>")
                sb.append("</Document></kml>")

                val inputStream = sb.toString().byteInputStream()
                val layer = KmlLayer(mGoogleMap, inputStream, context)
                layer.addLayerToMap()
            }
        })
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