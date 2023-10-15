package com.jina.paintor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding.rvLocation.apply {
            adapter = locationAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.etLocation.addTextChangedListener {
            if (it.toString() != "") {
                locationAdapter.filter?.filter(it.toString())
                binding.rvLocation.visibility == View.VISIBLE
            } else {
                binding.rvLocation.visibility == View.GONE
            }
        }
    }
}