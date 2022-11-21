package com.net.pvr1.ui.contactUs

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityContactUsBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.formats.viewModel.FormatsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity() {
    private var binding: ActivityContactUsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: FormatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
    }
}