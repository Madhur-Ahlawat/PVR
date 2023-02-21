package com.net.pvr1.ui.home.fragment.more.eVoucher.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityEvoucherDetailsBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.home.fragment.more.eVoucher.viewModel.EVoucherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EVoucherDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEvoucherDetailsBinding? = null

    private var loader: LoaderDialog? = null
    private val authViewModel: EVoucherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvoucherDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)
        manageFunction()
    }

    private fun manageFunction() {

    }

}