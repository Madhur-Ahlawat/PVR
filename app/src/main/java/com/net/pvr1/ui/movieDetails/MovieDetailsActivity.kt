package com.net.pvr1.ui.movieDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.net.pvr1.databinding.ActivityMovieDetailsBinding
import com.net.pvr1.databinding.ActivitySearchHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityMovieDetailsBinding? = null
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
    }
}