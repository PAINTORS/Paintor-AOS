package com.jina.paintor.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jina.paintor.R
import com.jina.paintor.databinding.FragmentPreferenceBinding

class PreferenceFragment : Fragment() {
    private val binding: FragmentPreferenceBinding by lazy {
        FragmentPreferenceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}