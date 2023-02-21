package com.net.pvr1.ui.payment.mobikwik

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityMobikwikLoginBinding
import com.net.pvr1.databinding.ActivityPaytmPostpaidBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.payment.paytmpostpaid.viewModel.PaytmPostPaidViewModel
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.ui.payment.webView.PaytmWebActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MobikwikLoginActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityMobikwikLoginBinding? = null
    private val paytmPostPaidViewModel: PaytmPostPaidViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var catFilterPayment = ArrayList<PaymentResponse.Output.Gateway>()
    private var paidAmount = ""
    private var title = ""
    var state_text = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobikwikLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.header?.textView108?.text = intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()
        paidAmount = intent.getStringExtra("paidAmount").toString()
        //paidAmount
        binding?.msg?.text = getString(R.string.currency) + paidAmount


        binding?.header?.imageView58?.setOnClickListener {
            onBackPressed()
        }


    }


    override fun onBackPressed() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            "Do you want to end the session?",
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
               launchActivity(HomeActivity::class.java,FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
            },
            negativeClick = {
            })
        dialog.show()
    }
}