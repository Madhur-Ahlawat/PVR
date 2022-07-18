package com.net.pvr1.ui.resister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityRegistrationBinding
import com.net.pvr1.databinding.ActivitySplashBinding
import com.net.pvr1.di.preference.AppPreferences

class RegistrationActivity : AppCompatActivity() {

    lateinit var preferences: AppPreferences
    private var binding: ActivityRegistrationBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences(applicationContext)

    }
}