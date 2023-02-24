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
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.TRANSACTION_ID
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
    private var pid = ""
    var state_text = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobikwikLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.header?.textView108?.text = intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()
        pid = intent.getStringExtra("pid").toString()
        paidAmount = intent.getStringExtra("paidAmount").toString()
        //paidAmount
        binding?.msg?.text = getString(R.string.currency) + paidAmount


        binding?.header?.imageView58?.setOnClickListener {
            onBackPressed()
        }

        binding?.mobileNumber?.setText(preferences.getString(Constant.SharedPreference.USER_NUMBER))
        binding?.emailNumber?.setText(preferences.getString(Constant.SharedPreference.USER_EMAIL))


        // GET OTP
        binding?.sendOtp?.setOnClickListener {
            if (binding?.sendOtp?.text == "Create Wallet") {
                if (binding?.emailNumber?.text.toString().isNotEmpty()) {
                    if (InputTextValidator.validateEmail(binding?.emailNumber?.text.toString())) {
                        binding?.errorText?.text = ""
                        paytmPostPaidViewModel.mobikwikCreateWallet(
                            preferences.getUserId(), BOOKING_ID, TRANSACTION_ID,
                            BOOK_TYPE, binding?.mobileNumber?.text.toString(),binding?.etOtp?.getStringFromFields().toString(),binding?.emailNumber?.text.toString()
                        )
                    } else {
                        binding?.errorText?.text = getString(R.string.email_msg_invalid)
                    }
                } else {
                    binding?.errorText?.text = getString(R.string.email_msg_required)
                }
            }else{
                if (binding?.mobileNumber?.text.toString().isNotEmpty()) {
                    if (InputTextValidator.validatePhoneNumber(binding?.mobileNumber?.text.toString())) {
                        binding?.errorText?.text = ""
                        paytmPostPaidViewModel.mobikwikOTP(
                            preferences.getUserId(), BOOKING_ID, TRANSACTION_ID,
                            BOOK_TYPE, binding?.mobileNumber?.text.toString()
                        )
                    } else {
                        binding?.errorText?.text = getString(R.string.mobile_msg_invalid)
                    }
                } else {
                    binding?.errorText?.text = getString(R.string.enterMobile)
                }
            }
        }
        mobikwikOTP()
        mobikwikCreateWallet()

        // VERIFY OTP

        binding?.subBtn?.setOnClickListener {
            if (binding?.etOtp?.getStringFromFields().toString().isNotEmpty()) {
                if (binding?.etOtp?.getStringFromFields().toString().length==6) {
                    binding?.errorText?.text = ""
                    paytmPostPaidViewModel.mobikwikPay(
                        preferences.getUserId(), BOOKING_ID, TRANSACTION_ID,
                        BOOK_TYPE, binding?.mobileNumber?.text.toString(),binding?.etOtp?.getStringFromFields().toString(),
                        CINEMA_ID
                    )
                }else{
                   toast("Enter valid OTP")
                }
            }else{
                toast(getString(R.string.enterOtp))
            }
        }

        mobikwikPay()

    }

    private fun mobikwikOTP() {
        paytmPostPaidViewModel.mobikwikOTPpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        if (it.data.output.statuscode == "0"){
                            binding?.loginView?.hide()
                            binding?.otpView?.show()
                        }else{
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.output.statusdescription,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun mobikwikPay() {
        paytmPostPaidViewModel.mobikwikPaypayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.statuscode == "33"){
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                getString(R.string.you_have_insufficient_balance),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    val intent = Intent(this@MobikwikLoginActivity, PaytmWebActivity::class.java)
                                    intent.putExtra("pTypeId", pid)
                                    intent.putExtra("mobile", binding?.mobileNumber?.text.toString())
                                    intent.putExtra("otp", binding?.etOtp?.getStringFromFields().toString())
                                    intent.putExtra("paidAmount", paidAmount)
                                    intent.putExtra("title", title)
                                    startActivity(intent)
                                },
                                negativeClick = {})
                            dialog.show()
                        }else if (it.data.output.statuscode == "159") {
                            // Create Wallet
                            binding?.sendOtp?.text = "Create Wallet"
                            binding?.emailtext?.text = "Enter Email ID"
                            binding?.textView382?.hide()
                            binding?.otpMsg?.hide()
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.output.statusdescription,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                                binding?.loginView?.show()
                                                binding?.otpView?.hide()
                                                binding?.mobileLayout?.hide()
                                                binding?.emailLayout?.show()

                                },
                                negativeClick = {})
                            dialog.show()
                        }else if (it.data.output.statuscode == "164"){
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.output.statusdescription,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        }else if (it.data.output.statuscode == "0"){
                            if (it.data.output.status == "SUCCESS"){
                                Constant().printTicket(this)
                                finish()
                            }else{
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
                        }else{
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun mobikwikCreateWallet() {
        paytmPostPaidViewModel.mobikwikCreateWalletpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.statuscode == "0"){
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.output.statusdescription,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    paytmPostPaidViewModel.mobikwikPay(
                                        preferences.getUserId(), BOOKING_ID, TRANSACTION_ID,
                                        BOOK_TYPE, binding?.mobileNumber?.text.toString(),binding?.etOtp?.getStringFromFields().toString(),CINEMA_ID
                                    )
                                },
                                negativeClick = {})
                            dialog.show()

                        }else{
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.output.statusdescription,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }



    override fun onBackPressed() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            "Do you want to end the session?",
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.cancel,
            positiveClick = {
               launchActivity(HomeActivity::class.java,FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK)
            },
            negativeClick = {
            })
        dialog.show()
    }
}