package com.net.pvr1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.net.pvr1.databinding.ActivityMainBinding
import com.net.pvr1.di.preference.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    lateinit var preferences: AppPreferences
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        val view = binding?.root

        setContentView(view)
    }
}