package com.jina.paintor.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.data.kml.KmlContainer
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlPolygon
import com.jina.paintor.R
import com.jina.paintor.databinding.FragmentLocationBinding
import com.jina.paintor.location.GpsTracker
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

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
        addKmlLayer()
    }

    private fun addKmlLayer() {
        // TODO : 정리 필요.
        /**
         * 1. https://gadm.org/download_country.html 해당 링크에서 KMZ 파일 저장할 수 있음. ( level1 )
         * 2. https://mygeodata.cloud/converter/kmz-to-kml 해당 링크에서 KMZ 파일을 KML 파일로 변환.
         * 3. 현재 서버가 없는 관계로 Stringbuffer 로 직접 해야하는데, 사실상 이게 가장 좋은 방법인 듯 함.
         *      -> 영역을 색칠하거나 색을 변경할 때 유용할 것 같음.
         * */
        val ggg = StringBuffer()
        ggg.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">")
        ggg.toString().byteInputStream()
        val test = "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "<Document id=\"root_doc\">\n" +
                "<Schema name=\"gadm41_KOR_1\" id=\"gadm41_KOR_1\">\n" +
                "\t<SimpleField name=\"GID_1\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"GID_0\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"COUNTRY\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"NAME_1\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"VARNAME_1\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"NL_NAME_1\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"TYPE_1\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"ENGTYPE_1\" type=\"string\"></SimpleField>\n" +
                "\t<SimpleField name=\"CC_1\" type=\"string\"></SimpleField>\n" +
                "</Schema>\n" +
                "<Folder><name>gadm41_KOR_1</name>\n" +
                "  <Placemark>\n" +
                "\t<name>KOR.1_1</name>\n" +
                "\t<description>KOR</description>\n" +
                "\t<Style><LineStyle><color>ff0000ff</color></LineStyle><PolyStyle><fill>0</fill></PolyStyle></Style>\n" +
                "\t<ExtendedData><SchemaData schemaUrl=\"#gadm41_KOR_1\">\n" +
                "\t\t<SimpleData name=\"GID_1\">South Korea</SimpleData>\n" +
                "\t\t<SimpleData name=\"GID_0\">Busan</SimpleData>\n" +
                "\t\t<SimpleData name=\"COUNTRY\">Pusan|Busan Gwang'yeogsi|Pusan-g</SimpleData>\n" +
                "\t\t<SimpleData name=\"NAME_1\">부산광역시 | 釜山廣域市</SimpleData>\n" +
                "\t\t<SimpleData name=\"VARNAME_1\">Gwangyeoksi</SimpleData>\n" +
                "\t\t<SimpleData name=\"NL_NAME_1\">Metropolitan City</SimpleData>\n" +
                "\t\t<SimpleData name=\"TYPE_1\">NA</SimpleData>\n" +
                "\t\t<SimpleData name=\"ENGTYPE_1\">KR.PU</SimpleData>\n" +
                "\t\t<SimpleData name=\"CC_1\">KR-26</SimpleData>\n" +
                "\t</SchemaData></ExtendedData>\n" +
                "      <MultiGeometry><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9919,34.9811 128.994,34.9815 128.992,34.9794 128.988,34.9786 128.9902,34.9819 128.9919,34.9811</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>129.0264,35.0957 129.0252,35.0935 129.0267,35.0853 129.0246,35.0763 129.0202,35.077 129.0169,35.0743 129.0213,35.0697 129.0223,35.0619 129.0164,35.0572 129.0157,35.0532 129.0106,35.0517 129.0116,35.0525 129.0088,35.0605 129.0111,35.063 129.0055,35.0711 129.004,35.0813 129.0052,35.0844 129.0021,35.0861 129.0,35.0836 129.0012,35.0798 128.9983,35.0805 128.9973,35.0831 128.9931,35.0839 128.9954,35.0634 128.9977,35.0574 129.0014,35.0484 129.007,35.049 129.0001,35.0476 128.9986,35.0444 128.9925,35.0452 128.9924,35.0493 128.9884,35.058 128.9846,35.0598 128.9774,35.0567 128.9728,35.0569 128.9737,35.0533 128.9828,35.0514 128.9799,35.0477 128.973,35.0492 128.9697,35.0474 128.9697,35.0449 128.978,35.0402 128.9731,35.0409 128.9672,35.0362 128.9674,35.0333 128.9656,35.0324 128.9645,35.0365 128.9677,35.0435 128.9593,35.0486 128.9527,35.072 128.9569,35.0728 128.9526,35.0722 128.9503,35.0818 128.9548,35.0934 128.956,35.1064 128.9488,35.1071 128.9431,35.0864 128.9321,35.0738 128.9294,35.0739 128.9286,35.0787 128.9319,35.0847 128.933,35.0984 128.9363,35.1064 128.9391,35.1087 128.9331,35.1103 128.9327,35.1067 128.9308,35.1049 128.9302,35.0927 128.9247,35.0924 128.9132,35.0876 128.9112,35.0789 128.8952,35.0789 128.8953,35.0864 128.8978,35.0929 128.8994,35.0926 128.9006,35.1114 128.9056,35.1181 128.8976,35.1203 128.8895,35.1078 128.8877,35.0895 128.8833,35.0824 128.8842,35.0793 128.8692,35.0814 128.8707,35.084 128.8724,35.0829 128.8708,35.0887 128.8708,35.0851 128.8682,35.0832 128.8401,35.0832 128.8389,35.0821 128.8352,35.0884 128.8321,35.0884 128.8218,35.0977 128.8207,35.097 128.8236,35.102 128.8348,35.1018 128.8417,35.106 128.843,35.1132 128.8385,35.1152 128.8359,35.119 128.8378,35.124 128.833,35.1299 128.8274,35.1282 128.8242,35.131 128.8154,35.1326 128.8076,35.1391 128.8067,35.1425 128.8021,35.1419 128.8018,35.1466 128.7937,35.1572 128.7945,35.1585 128.8006,35.1581 128.8059,35.1604 128.8087,35.1589 128.816,35.1602 128.823,35.1562 128.8275,35.1556 128.836,35.1591 128.8429,35.158 128.8463,35.1647 128.8597,35.1682 128.8674,35.1677 128.8715,35.1651 128.8721,35.1626 128.8692,35.1586 128.8666,35.1598 128.8652,35.1582 128.8717,35.1505 128.8763,35.1517 128.8747,35.1536 128.8744,35.1586 128.8808,35.1595 128.8814,35.1613 128.877,35.165 128.8758,35.1705 128.8724,35.1723 128.8735,35.1741 128.8811,35.1718 128.88,35.1789 128.8817,35.1822 128.8761,35.1881 128.8762,35.1926 128.8704,35.2016 128.879,35.21 128.8816,35.2104 128.8864,35.2145 128.8968,35.2132 128.9084,35.2168 128.905,35.2201 128.9093,35.2229 128.9092,35.2206 128.9103,35.2229 128.9125,35.2225 128.9167,35.217 128.9201,35.2164 128.944,35.2283 128.9485,35.2252 128.9567,35.2253 128.9736,35.2274 128.9897,35.2317 128.9988,35.2381 129.0148,35.2705 129.0135,35.2738 129.0295,35.2769 129.0475,35.2742 129.049,35.2784 129.0538,35.2821 129.0522,35.2859 129.0584,35.2948 129.0702,35.2943 129.0743,35.2908 129.0884,35.3026 129.0967,35.3035 129.1056,35.3065 129.1086,35.3046 129.1124,35.3101 129.112,35.3167 129.1137,35.3202 129.1181,35.3225 129.1211,35.3287 129.1258,35.3321 129.124,35.3379 129.128,35.3433 129.1271,35.3454 129.1346,35.3515 129.1332,35.3564 129.1261,35.363 129.1222,35.3638 129.1182,35.369 129.1212,35.3674 129.1276,35.3677 129.1322,35.3657 129.1501,35.3639 129.1611,35.3594 129.1628,35.3568 129.1686,35.3538 129.1761,35.3514 129.1857,35.3586 129.1934,35.3612 129.1938,35.3633 129.1988,35.3665 129.1978,35.3714 129.1992,35.376 129.1936,35.3821 129.1994,35.389 129.2079,35.3825 129.2185,35.379 129.2302,35.3811 129.2354,35.3803 129.237,35.3836 129.2402,35.3843 129.2488,35.3832 129.2509,35.3847 129.2506,35.3874 129.258,35.3841 129.2655,35.3871 129.2681,35.3806 129.2768,35.3721 129.2794,35.3661 129.2822,35.3653 129.2805,35.3562 129.2849,35.3507 129.2839,35.3461 129.2797,35.3414 129.2898,35.3393 129.2919,35.3368 129.2999,35.336 129.3048,35.3299 129.3057,35.3246 129.3037,35.3222 129.3052,35.3244 129.3042,35.3301 129.3019,35.3202 129.2992,35.3184 129.2915,35.3181 129.2865,35.3203 129.2883,35.32 129.2882,35.3234 129.2849,35.3247 129.285,35.3262 129.2807,35.3265 129.2682,35.3217 129.262,35.3154 129.2605,35.3089 129.2579,35.3057 129.261,35.2978 129.2607,35.2917 129.2573,35.2889 129.2592,35.2843 129.2556,35.2804 129.2556,35.2748 129.2521,35.2711 129.2473,35.2718 129.2466,35.2698 129.2468,35.2725 129.2447,35.2717 129.2431,35.2642 129.2358,35.2648 129.234,35.2622 129.235,35.2584 129.239,35.2574 129.2434,35.2597 129.2459,35.258 129.2521,35.2602 129.2534,35.2465 129.2504,35.2439 129.2445,35.2443 129.2455,35.2421 129.2499,35.2408 129.2453,35.2376 129.2431,35.2382 129.245,35.2314 129.2416,35.2291 129.2393,35.2212 129.2355,35.2183 129.2293,35.2213 129.2304,35.2241 129.2273,35.2243 129.227,35.2176 129.222,35.2126 129.2237,35.2089 129.2301,35.2041 129.2302,35.1982 129.2274,35.1957 129.2251,35.196 129.2232,35.1897 129.2241,35.1858 129.2193,35.1838 129.2158,35.1873 129.2102,35.1826 129.2009,35.1794 129.1976,35.1751 129.1968,35.1649 129.1944,35.1602 129.1873,35.1595 129.1817,35.1548 129.1777,35.1549 129.1699,35.1591 129.1642,35.159 129.1559,35.1569 129.1531,35.1519 129.15,35.1514 129.151,35.1561 129.1533,35.158 129.1482,35.1563 129.1463,35.1534 129.1416,35.1551 129.1405,35.157 129.142,35.1586 129.1389,35.1613 129.1339,35.1624 129.1309,35.1613 129.1352,35.1566 129.1346,35.1548 129.1243,35.153 129.1219,35.1545 129.1185,35.1526 129.1151,35.1474 129.1179,35.1452 129.1178,35.1404 129.112,35.1363 129.1141,35.1358 129.1089,35.1328 129.1129,35.1351 129.1178,35.1321 129.1169,35.13 129.1133,35.1262 129.1128,35.125 129.1205,35.1339 129.1224,35.1321 129.1224,35.1269 129.129,35.1182 129.1272,35.1135 129.128,35.1108 129.1236,35.102 129.1249,35.1007 129.124,35.0993 129.1155,35.1002 129.1132,35.103 129.1105,35.1011 129.1145,35.0979 129.1095,35.0915 129.114,35.0978 129.1106,35.1007 129.1023,35.0945 129.1051,35.0919 129.0996,35.0912 129.0952,35.0939 129.093,35.1093 129.0904,35.1087 129.0904,35.1036 129.075,35.1036 129.0655,35.1091 129.0674,35.1082 129.0695,35.111 129.0684,35.1124 129.0695,35.114 129.0738,35.1168 129.0722,35.1188 129.071,35.1174 129.0695,35.1183 129.073,35.1222 129.0653,35.1229 129.0655,35.129 129.0561,35.118 129.0523,35.1205 129.0553,35.1229 129.0554,35.1253 129.0527,35.1257 129.05,35.1236 129.0487,35.1207 129.0553,35.116 129.0544,35.115 129.0501,35.1176 129.0471,35.1164 129.0508,35.114 129.0508,35.1125 129.0466,35.1079 129.044,35.1076 129.0456,35.1056 129.0468,35.1068 129.0452,35.105 129.0403,35.107 129.0398,35.1058 129.0436,35.1034 129.0422,35.102 129.0382,35.1044 129.0399,35.1004 129.0378,35.0969 129.0264,35.0957</coordinates></LinearRing></outerBoundaryIs><innerBoundaryIs><LinearRing><coordinates>129.1033,35.0934 129.1022,35.0943 129.0988,35.0954 129.0997,35.0914 129.1033,35.0934</coordinates></LinearRing></innerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.7764,35.0108 128.7725,35.0103 128.7706,35.0137 128.7656,35.0119 128.7654,35.0136 128.7683,35.0161 128.7712,35.0157 128.7742,35.0148 128.7764,35.0108</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9752,35.0295 128.9738,35.0254 128.9733,35.0252 128.9712,35.0274 128.9717,35.0292 128.9742,35.0312 128.9752,35.0295</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.8466,35.0562 128.8499,35.0521 128.8512,35.042 128.8488,35.034 128.8432,35.0307 128.8432,35.0267 128.8385,35.0178 128.835,35.0159 128.8344,35.012 128.841,35.0069 128.8374,35.0 128.8399,34.9975 128.8375,34.9971 128.8277,34.9887 128.8302,34.9946 128.8271,34.9957 128.8218,34.9946 128.8223,35.0009 128.8182,35.0043 128.8232,35.0046 128.8237,35.007 128.8201,35.0092 128.8262,35.0104 128.8272,35.0136 128.8234,35.0168 128.811,35.0212 128.8062,35.0212 128.8048,35.0241 128.8131,35.0237 128.8154,35.0265 128.8143,35.03 128.8075,35.0317 128.8077,35.0339 128.8116,35.0342 128.8124,35.0397 128.8057,35.0475 128.7995,35.0476 128.7975,35.0493 128.7969,35.0539 128.8033,35.0554 128.791,35.0534 128.7909,35.0616 128.7944,35.0625 128.7952,35.0597 128.8059,35.0615 128.805,35.0668 128.8324,35.0713 128.8324,35.0776 128.8115,35.0776 128.8115,35.083 128.8185,35.083 128.8215,35.0844 128.8215,35.0858 128.8264,35.0862 128.8287,35.09 128.8352,35.0864 128.8353,35.0673 128.8327,35.0578 128.8384,35.0537 128.8459,35.0564 128.8458,35.0595 128.8466,35.0562</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9088,35.057 128.9197,35.0546 128.9168,35.0516 128.9117,35.0551 128.8896,35.0595 128.8864,35.0612 128.886,35.0633 128.8881,35.065 128.892,35.0616 128.8934,35.0636 128.9088,35.057</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.8464,35.0603 128.8458,35.0595 128.8458,35.0597 128.8464,35.0603</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9106,35.0619 128.9155,35.0622 128.92,35.0613 128.9169,35.0562 128.9083,35.0587 128.9052,35.0621 128.9109,35.0636 128.9106,35.0619</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9541,35.0556 128.9526,35.0563 128.9529,35.0589 128.9456,35.0619 128.9508,35.057 128.9502,35.0545 128.9423,35.0567 128.9378,35.0543 128.9411,35.0537 128.9457,35.0555 128.9537,35.0532 128.9567,35.0496 128.9564,35.0469 128.9305,35.0491 128.9242,35.0548 128.9287,35.0569 128.9309,35.0553 128.9298,35.0538 128.9306,35.0517 128.9361,35.0505 128.9302,35.0583 128.9444,35.0746 128.9534,35.0622 128.9541,35.0556</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.8461,35.0638 128.8368,35.0674 128.8375,35.0748 128.8399,35.0743 128.8422,35.0695 128.8549,35.0692 128.8586,35.0641 128.8464,35.0603 128.8478,35.0625 128.8461,35.0638</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.8743,35.071 128.8856,35.0685 128.8859,35.066 128.8802,35.0638 128.8741,35.0638 128.8578,35.0687 128.8606,35.0713 128.8685,35.0722 128.8743,35.071</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.79,35.0683 128.7894,35.0699 128.7908,35.0709 128.7906,35.0689 128.79,35.0683</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9226,35.0763 128.9215,35.0747 128.9204,35.0759 128.9156,35.0756 128.9054,35.0707 128.9031,35.0709 128.9131,35.0756 128.922,35.0767 128.9154,35.0778 128.922,35.0778 128.9226,35.0763</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>129.0159,35.048 129.0139,35.0481 129.0144,35.0503 129.0156,35.0496 129.0159,35.048</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>129.0466,35.076 129.0344,35.085 129.0312,35.0937 129.0367,35.0926 129.0361,35.0945 129.0385,35.0936 129.04,35.0956 129.0455,35.0971 129.0489,35.0953 129.0478,35.0979 129.0521,35.0966 129.0511,35.0996 129.0593,35.101 129.0624,35.104 129.0614,35.0979 129.0638,35.0981 129.0709,35.0939 129.0784,35.0872 129.0794,35.0847 129.0777,35.0836 129.0793,35.082 129.0827,35.0836 129.0832,35.0858 129.0832,35.0833 129.0793,35.0815 129.0834,35.0752 129.0857,35.0747 129.0926,35.0803 129.096,35.0804 129.0947,35.0765 129.0908,35.0738 129.0869,35.0727 129.0858,35.0741 129.08,35.0714 129.0811,35.0698 129.0842,35.0697 129.0879,35.0627 129.0956,35.06 129.0922,35.0538 129.0931,35.0523 129.0876,35.0494 129.0827,35.0505 129.0807,35.0563 129.0774,35.0603 129.0744,35.0606 129.0708,35.0586 129.0695,35.0606 129.0651,35.0614 129.065,35.0684 129.0569,35.0695 129.0466,35.076</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>128.9147,35.0766 128.9116,35.0757 128.9056,35.0722 128.9102,35.0756 128.9147,35.0766</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>129.104,35.0793 129.1052,35.0792 129.0976,35.0786 129.0976,35.0788 129.104,35.0793</coordinates></LinearRing></outerBoundaryIs></Polygon><Polygon><outerBoundaryIs><LinearRing><coordinates>129.1055,35.0846 129.113,35.0912 129.1132,35.0911 129.1057,35.0844 129.1055,35.0846</coordinates></LinearRing></outerBoundaryIs></Polygon></MultiGeometry>\n" +
                "  </Placemark></Folder>\n" +
                "</Document></kml>"
        val inputStream = test.byteInputStream()
//        val inputStream: InputStream? = mContext.assets.open("korea_1.kml")
        val layer = KmlLayer(mGoogleMap, inputStream, context)
        layer.addLayerToMap()
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