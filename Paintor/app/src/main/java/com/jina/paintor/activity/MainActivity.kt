package com.jina.paintor.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.jina.paintor.R
import com.jina.paintor.databinding.ActivityMainBinding
import com.jina.paintor.fragment.ListFragment
import com.jina.paintor.fragment.LocationFragment
import com.jina.paintor.fragment.PreferenceFragment
import com.jina.paintor.fragment.SearchFragment
import com.orhanobut.logger.Logger


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val locationFragment = LocationFragment(this)
    private val listFragment = ListFragment()
    private val searchFragment = SearchFragment()
    private val preferenceFragment = PreferenceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.flLayout, locationFragment)
            .commitAllowingStateLoss()

        binding.includeToolbar.toolbarTitle = "PAINTOR"

        binding.bottomNaviView.setOnItemSelectedListener(this)
        binding.includeToolbar.ivCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

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



}