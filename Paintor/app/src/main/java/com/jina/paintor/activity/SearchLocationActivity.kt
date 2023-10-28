package com.jina.paintor.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.jina.paintor.R
import com.jina.paintor.adapter.SearchLocationAdapter
import com.jina.paintor.database.MainViewModel
import com.jina.paintor.databinding.ActivitySearchLocationBinding
import com.jina.paintor.tripscheudule.ScheduleManager
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger

class SearchLocationActivity : AppCompatActivity() {

    private val binding: ActivitySearchLocationBinding by lazy {
        ActivitySearchLocationBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()
    private lateinit var locationAdapter: SearchLocationAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        locationAdapter = SearchLocationAdapter(this, viewModel)
        Places.initialize(applicationContext, getString(R.string.GOOGLE_API_KEY))
        binding.includeToolbar.ivNewPlan.visibility = View.GONE
        binding.includeToolbar.toolbarTitle = "여행지 검색"

        binding.rvLocation.apply {
            adapter = locationAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.etLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding.rvLocation.visibility = View.GONE
                } else {
                    locationAdapter.filter?.filter(s.toString())
                    binding.rvLocation.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        ScheduleManager.selectedLocation.observe(this, Observer {
            binding.btnSelect.apply {
                if (it == null) {
                    this.setText("여행지를 선택해 주세요.")
                    this.isEnabled = false
                    this.setBackgroundColor(getColor(R.color.disable_color))
                } else {
                    this.setText("날짜 지정하러 가기.")
                    this.isEnabled = true
                    this.setBackgroundColor(getColor(R.color.main_color))
                    this.setOnClickListener {
                        val intent = Intent(this@SearchLocationActivity, CalendarActivity::class.java)
                        intent.putExtra(ScheduleManager.SELECT_LOCATION, true)
                        startActivity(intent)
                    }
                }
            }
        })
    }
}
