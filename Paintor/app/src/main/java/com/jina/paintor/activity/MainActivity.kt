package com.jina.paintor.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.jina.paintor.R
import com.jina.paintor.database.MainViewModel
import com.jina.paintor.database.schedule.TripSchedule
import com.jina.paintor.databinding.ActivityMainBinding
import com.jina.paintor.fragment.ListFragment
import com.jina.paintor.fragment.LocationFragment
import com.jina.paintor.fragment.PreferenceFragment
import com.jina.paintor.fragment.SearchFragment
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val locationFragment = LocationFragment(this)
    private val listFragment = ListFragment()
    private val searchFragment = SearchFragment()
    private val preferenceFragment = PreferenceFragment()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.flLayout, locationFragment)
            .commitAllowingStateLoss()

        binding.includeToolbar.isMain = true
        binding.includeToolbar.toolbarTitle = "PAINTOR"

        binding.bottomNaviView.setOnItemSelectedListener(this)
        binding.includeToolbar.ivCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
        dummyData()
    }

    // NOTE : 23/09/21 tab 눌렀을 때 샥샥 넘어가는 애니메이션 추가?
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tabLocation -> {
                supportFragmentManager.beginTransaction().replace(R.id.flLayout, locationFragment)
                    .commitAllowingStateLoss()
            }

            R.id.tabList -> {
                supportFragmentManager.beginTransaction().replace(R.id.flLayout, listFragment)
                    .commitAllowingStateLoss()
            }

            R.id.tabSearch -> {
                supportFragmentManager.beginTransaction().replace(R.id.flLayout, searchFragment)
                    .commitAllowingStateLoss()
            }

            R.id.tabPreference -> {
                supportFragmentManager.beginTransaction().replace(R.id.flLayout, preferenceFragment)
                    .commitAllowingStateLoss()
            }

            else -> Logger.d("do nothing")
        }
        return true
    }


    private fun dummyData() {
        val areaList = listOf<String>("서울", "부산")
        val latitudeList = listOf<String>("37.4882542", "35.1795543")
        val longitudeList = listOf<String>("126.988473", "129.0756416")
        val tripStatus = listOf<Boolean>(true, false)
        val tripColor = listOf<Int>(getColor(R.color.purple_200), getColor(R.color.black))
        CoroutineScope(Dispatchers.IO).launch {
            areaList.forEachIndexed { index, s ->

                val schedule = TripSchedule(
                    area = areaList[index],
                    latitude = latitudeList[index],
                    longitude = longitudeList[index],
                    tripStatus = tripStatus[index],
                    tripColor = tripColor[index],
                    startDate = null,
                    endDate = null,
                    saveTime = System.currentTimeMillis(),
                    updateTime = null,
                    deleteTime = null
                )
                viewModel.insertSchedule(schedule)
            }
        }
    }
}