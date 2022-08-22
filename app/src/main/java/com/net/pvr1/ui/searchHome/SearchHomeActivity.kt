package com.net.pvr1.ui.searchHome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.databinding.ActivityHomeBinding
import com.net.pvr1.databinding.ActivitySearchHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.login.viewModel.LoginViewModel
import com.net.pvr1.ui.searchHome.viewModel.HomeSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchHomeActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySearchHomeBinding? = null
    private val authViewModel: HomeSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()

    }
}