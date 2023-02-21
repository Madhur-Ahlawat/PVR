package com.net.pvr1.ui.home.fragment.more.eVoucher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityEvoucherBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.home.fragment.more.eVoucher.details.EVoucherDetailsActivity
import com.net.pvr1.ui.home.fragment.more.eVoucher.viewModel.EVoucherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EVoucherActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEvoucherBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: EVoucherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvoucherBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        //title
        binding?.include51?.textView108?.text = getString(R.string.vouchers)

        movedNext()
    }

    private fun movedNext() {
//        backPress
        binding?.include51?.imageView58?.setOnClickListener {
            finish()
        }

        binding?.imageView112?.setOnClickListener {
            val intent = Intent(this@EVoucherActivity, EVoucherDetailsActivity::class.java)
            startActivity(intent)
        }
    }
}