package com.net.pvr1.ui.formats

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.databinding.ActivityFormatsBinding
import com.net.pvr1.databinding.ActivityGiftCardBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.formats.viewModel.FormatsViewModel
import com.net.pvr1.ui.giftCard.viewModel.GiftCardViewModel

@AndroidEntryPoint
class FormatsActivity : AppCompatActivity() {
    private var binding: ActivityFormatsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: FormatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormatsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)


    }
}