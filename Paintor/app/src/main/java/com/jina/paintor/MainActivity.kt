package com.jina.paintor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jina.paintor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.includeToolbar.toolbarTitle = "PAINTOR"
    }
}