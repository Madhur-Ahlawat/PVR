package com.net.pvr.ui.home.fragment.more.eVoucher.purchaseDetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.databinding.ActivityPurchaseEvoucherDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseEVoucherDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPurchaseEvoucherDetailsBinding? = null
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseEvoucherDetailsBinding.inflate(layoutInflater, null, false)

        setContentView(binding?.root)
    }
}