package com.net.pvr1.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.net.pvr1.databinding.ActivityLoginBinding
import com.net.pvr1.databinding.ActivitySplashBinding
import com.net.pvr1.di.preference.AppPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var preferences: AppPreferences
    private var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
    }
}