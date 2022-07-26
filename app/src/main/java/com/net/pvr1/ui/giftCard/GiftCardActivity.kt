package com.net.pvr1.ui.giftCard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.databinding.ActivityGiftCardBinding
import com.net.pvr1.databinding.ActivityOfferBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.offer.viewModel.OfferViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GiftCardActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: OfferViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        movedNext()
    }

    private fun movedNext() {

    }
}