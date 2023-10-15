package com.jina.paintor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.jina.paintor.R
import com.jina.paintor.adapter.SearchLocationAdapter
import com.jina.paintor.databinding.ActivitySearchLocationBinding

class SearchLocationActivity : AppCompatActivity() {

    private val binding: ActivitySearchLocationBinding by lazy {
        ActivitySearchLocationBinding.inflate(layoutInflater)
    }
    private val locationAdapter = SearchLocationAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
    }
}