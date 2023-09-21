package com.jina.paintor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jina.paintor.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {

    private val binding: ActivityCalendarBinding by lazy {
        ActivityCalendarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}