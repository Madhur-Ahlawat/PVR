package com.net.pvr1.ui.summery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.databinding.ActivitySummeryBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.ui.summery.viewModel.SummeryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummeryActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySummeryBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SummeryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummeryBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
    }
}