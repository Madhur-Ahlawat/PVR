package com.net.pvr1.ui.summery.coupons

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityCouponsBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.summery.coupons.viewModel.CouponsViewModel
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CouponsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCouponsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: CouponsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        //coupons_dialog
    }
}