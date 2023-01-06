package com.net.pvr1.ui.payment.cred

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCredBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.payment.promoCode.viewModel.PromoCodeViewModel
import com.net.pvr1.ui.payment.viewModel.PaymentViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CredActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCredBinding? = null
    private val authViewModel: PaymentViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var count : Int = 0

    private val promoCodeViewModel: PromoCodeViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCredBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include29?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        binding?.include29?.textView108?.text = getString(R.string.bank_offer)

        //PaidAmount
        binding?.textView178?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")


        if (intent.hasExtra("ca_t") && !TextUtils.isEmpty(intent.getStringExtra("ca_t"))) {
            binding?.dataTextView?.text = intent.getStringExtra("ca_t")
        }
        if (intent.hasExtra("icon") && !TextUtils.isEmpty(intent.getStringExtra("icon"))) {
            Glide.with(this)
                .load(intent.hasExtra("icon"))
                .error(R.drawable.app_icon)
                .into(binding?.credLogo!!)
        }

        if (intent.hasExtra("bannertext") && !TextUtils.isEmpty(intent.getStringExtra("bannertext"))) {
            binding?.bannerText?.text = intent.getStringExtra("bannertext")
        }

        if (intent.hasExtra("msg") && !TextUtils.isEmpty(intent.getStringExtra("msg"))) {
            binding?.vpaValidationMessage?.text = intent.getStringExtra("msg")
        }

        binding?.include30?.textView5?.setOnClickListener {
            authViewModel.credHmac(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.BOOK_TYPE,
                Constant.TRANSACTION_ID,
                "false",
                Constant.isPackageInstalled(packageManager).toString(),
                "false",
                "CRED"
            )
        }

        binding?.circleView?.maxValue = 100f
        binding?.circleView?.setValue(0f)
        binding?.circleView?.setValueAnimated(10f)
        binding?.circleView?.unit = "%"
        binding?.circleView?.isShowTextWhileSpinning = true // Show/hide text in spinning mode

        binding?.circleView?.setText("Waiting...")

        credHmac()
        credStatus()

    }

    private fun credHmac() {
        authViewModel.credHmacLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(it.data.output.intent)
                        startActivityForResult(i, 35)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun credStatus() {
        binding?.circleView?.stopSpinning()
        authViewModel.credCheckLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.p == "PAID"){
                            Constant().printTicket(this)
                        }else if (it.data.output.p == "PENDING"){
                            if (count == 18){

                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "Transaction Failed!",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                                    launchActivity(HomeActivity::class.java,Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    },
                                    negativeClick = {})
                                dialog.show()
                            }else {
                                count += 1
                                binding?.circleView?.spin()
                                val handler = Handler()
                                handler.postDelayed({ // close your dialog
                                    authViewModel.credStatus(
                                        preferences.getUserId(),
                                        Constant.BOOKING_ID,
                                        Constant.BOOK_TYPE,
                                        Constant.TRANSACTION_ID,
                                    )
                                }, 10000)
                            }
                        }else{
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                "Transaction Failed!",
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    launchActivity(HomeActivity::class.java,Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                },
                                negativeClick = {})
                            dialog.show()
                        }
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.credStatus(
            preferences.getUserId(),
            Constant.BOOKING_ID,
            Constant.BOOK_TYPE,
            Constant.TRANSACTION_ID,
        )
    }

}