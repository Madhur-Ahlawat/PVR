package com.net.pvr1.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.databinding.ActivityPaymentBinding
import com.net.pvr1.databinding.ActivityPlayerBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.player.viewModel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityPaymentBinding? = null
    private val authViewModel: PlayerViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
    }
}