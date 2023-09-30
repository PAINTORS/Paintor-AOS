package com.jina.paintor.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.jina.paintor.R
import com.jina.paintor.utils.Permission

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        checkPermission()
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // TODO : 0930 Permission 동의하지 않았을 경우
            }
        } else {
            // TODO : 0930 Permission 동의하지 않았을 경우
        }
    }
}