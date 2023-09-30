package com.jina.paintor.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationBarView
import com.jina.paintor.R
import com.jina.paintor.databinding.ActivityMainBinding
import com.jina.paintor.fragment.ListFragment
import com.jina.paintor.fragment.LocationFragment
import com.jina.paintor.fragment.PreferenceFragment
import com.jina.paintor.fragment.SearchFragment
import com.jina.paintor.utils.Permission
import com.jina.paintor.utils.TAG
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
        checkPermission()
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


    private fun checkPermission() { // TODO : 0930 SplashActivity
        val permissions = Permission.checkBuildPermission(Build.VERSION.SDK_INT)
        val deniedPermissions = permissions.filterNot { Permission.checkPermission(this, it) }
        if (deniedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, deniedPermissions.toTypedArray(),
                Permission.REQUEST_CODE_PERMISSION
            )
        } else {

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permission.REQUEST_CODE_PERMISSION) {
            val allPermissionGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionGranted) {
                // TODO : 0930 Login 으로 넘겨야함.
            } else {
                // TODO : 0930 Permission 동의하지 않았을 경우
            }
        } else {
            // TODO : 0930 Permission 동의하지 않았을 경우
        }
    }
}