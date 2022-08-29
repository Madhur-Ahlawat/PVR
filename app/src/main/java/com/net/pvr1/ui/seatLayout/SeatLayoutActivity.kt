package com.net.pvr1.ui.seatLayout

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivitySeatLayoutBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.search.searchCinema.viewModel.CinemaSearchViewModel
import com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatLayoutActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySeatLayoutBinding? = null
    private val authViewModel: SeatLayoutViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
    }
}