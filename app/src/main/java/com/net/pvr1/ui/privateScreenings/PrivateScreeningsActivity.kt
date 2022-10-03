package com.net.pvr1.ui.privateScreenings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityPrivateScreeningsBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.privateScreenings.viewModel.PrivateScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrivateScreeningsActivity : AppCompatActivity() {
//    @Inject
//    lateinit var preferences: AppPreferences
    private var binding: ActivityPrivateScreeningsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: PrivateScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivateScreeningsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        sendYourRequest()
    }

    private fun sendYourRequest() {

    }
}