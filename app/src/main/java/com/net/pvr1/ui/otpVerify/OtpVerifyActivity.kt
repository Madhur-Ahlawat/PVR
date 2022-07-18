package com.net.pvr1.ui.otpVerify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityOtpVerifyBinding
import com.net.pvr1.di.preference.AppPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerifyActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityOtpVerifyBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences(applicationContext)


    }
}