package com.net.pvr1.ui.payment.mCoupon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityMcouponBinding
import com.net.pvr1.databinding.ActivityPaymentBinding
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MCouponActivity : AppCompatActivity() { @Inject
lateinit var preferences: PreferenceManager
    private var binding: ActivityMcouponBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMcouponBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        //PaidAmount
        binding?.textView178?.text = getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")
    }
}