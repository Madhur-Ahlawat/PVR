package com.net.pvr.ui.payment.paytmpostpaid

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPaytmPostpaidBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.payment.paytmpostpaid.viewModel.PaytmPostPaidViewModel
import com.net.pvr.ui.payment.response.PaymentResponse
import com.net.pvr.ui.payment.webView.PaytmWebActivity
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaytmPostPaidActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPaytmPostpaidBinding? = null
    private val paytmPostPaidViewModel: PaytmPostPaidViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var catFilterPayment = ArrayList<PaymentResponse.Output.Gateway>()
    private var paidAmount = ""
    private var newAmt = "0.0"
    private var title = ""
    var state_text = ""

    companion object{
        var callFc = false
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaytmPostpaidBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.header?.textView108?.text = intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()
        paidAmount = Constant.DECIFORMAT.format(intent.getStringExtra("paidAmount")?.toDouble())
        //paidAmount
        binding?.msg?.text = getString(R.string.currency) + paidAmount

        if (intent.getStringExtra("title").equals("epaylater", ignoreCase = true)) {
            binding?.addPay?.text = "PAY NOW"
            getEpayOTP()
        } else if (intent.getStringExtra("title") == "Paytm") {
            paytmPostPaidViewModel.getWalletHmac(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }else if (intent.getStringExtra("title") == "FreeCharge") {
            binding?.fcEtOtp?.show()
            binding?.etOtp?.hide()
            paytmPostPaidViewModel.freechargeOTP(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
            freechargeOTP()
        }else {
            paytmPostPaidViewModel.getBalance(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }

        binding?.subBtn?.setOnClickListener(View.OnClickListener {
            val text = binding?.etOtp?.getStringFromFields()
            if (title == "epaylater"){

            }else if (title == "Paytm"){
                if (binding?.subBtn?.text.toString().equals("Make Payment", ignoreCase = true))
                    paytmPostPaidViewModel.walletMakePayment(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                else if (text?.length==6  && text !="null") {
                    paytmPostPaidViewModel.walletVerifYOTP(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE,
                        binding?.etOtp?.getStringFromFields().toString(),state_text
                    )
                } else {
                    toast("Please enter OTP!")
                }
            }else if (title == "FreeCharge"){
                if (binding?.subBtn?.text.toString().equals("Make Payment", ignoreCase = true)) {
                    paytmPostPaidViewModel.freechargePayment(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                    freechargePayment()
                }else if (text?.length==4  && text !="null") {
                    paytmPostPaidViewModel.freechargeLogin(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE,
                        binding?.fcEtOtp?.getStringFromFields().toString(),state_text
                    )
                    freechargeLogin()
                } else {
                    toast("Please enter OTP!")
                }
            }else {
                if (binding?.subBtn?.text.toString().equals("Make Payment", ignoreCase = true))
                    paytmPostPaidViewModel.postPaidPay(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                else if (text?.length==6  && text !="null") {
                    println("binding?.etOtp?.getStringFromFields().toString().length"+text.length)
                    paytmPostPaidViewModel.postPaidVerifYOTP(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE,
                        binding?.etOtp?.getStringFromFields().toString()
                    )
                } else {
                    toast("Please enter OTP!")
                }
            }
        })

        binding?.resendOTP?.setOnClickListener(View.OnClickListener {
            binding?.fcEtOtp?.clearText(true)
            binding?.etOtp?.clearText(true)
            if (title.equals("epaylater", ignoreCase = true)) {
                getEpayOTP()
            } else if (title == "Paytm"){
                paytmPostPaidViewModel.walletSendOTP(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.TRANSACTION_ID,
                    Constant.BOOK_TYPE,
                    preferences.getString(Constant.SharedPreference.USER_NUMBER),
                    preferences.getString(Constant.SharedPreference.USER_EMAIL)
                )
            }else if (title == "FreeCharge"){
                paytmPostPaidViewModel.freechargeResend(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.TRANSACTION_ID,
                    Constant.BOOK_TYPE,
                    state_text
                )
                freechargeResend()
            }else{
                paytmPostPaidViewModel.postPaidSendOTP(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.TRANSACTION_ID,
                    Constant.BOOK_TYPE
                )
            }
        })

        binding?.addPay?.setOnClickListener(View.OnClickListener {
            if (title.equals("epaylater", ignoreCase = true)) {
            } else if (title == "Paytm") {
                if (binding?.addPay?.text.toString().equals("PAY NOW", ignoreCase = true)) {
                    paytmPostPaidViewModel.postPaidMakePayment(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                } else {
                    val intent = Intent(this@PaytmPostPaidActivity, PaytmWebActivity::class.java)
                    intent.putExtra("pTypeId", intent.getStringExtra("pid"))
                    intent.putExtra("paidAmount", paidAmount)
                    intent.putExtra("title", title)
                    startActivity(intent)
                }
            }else if (title == "FreeCharge") {
                if (binding?.addPay?.text.toString().equals("PAY NOW", ignoreCase = true)) {
                    paytmPostPaidViewModel.freechargePayment(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                    freechargePayment()
                } else {
                    val intent = Intent(this@PaytmPostPaidActivity, PaytmWebActivity::class.java)
                    intent.putExtra("pTypeId", intent.getStringExtra("pid"))
                    intent.putExtra("paidAmount", newAmt)
                    intent.putExtra("title", title)
                    startActivity(intent)
                }
            }else{
                if (binding?.addPay?.text.toString().equals("PAY NOW", ignoreCase = true)) {
                    paytmPostPaidViewModel.postPaidMakePayment(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                } else {
                    val intent = Intent(this@PaytmPostPaidActivity, PaytmWebActivity::class.java)
                    intent.putExtra("pTypeId", intent.getStringExtra("pid"))
                    intent.putExtra("paidAmount", paidAmount)
                    intent.putExtra("title", title)
                    startActivity(intent)
                }
            }
        })

        binding?.header?.imageView58?.setOnClickListener {
            onBackPressed()
        }

        getBalance()
        postPaidPay()
        postPaidSendOTP()
        postPaidVerifyOTP()
        postPaidMakePayment()

        getHmac()
        walletSendOTP()
        walletVerifyOTP()
        walletMakePayment()

    }

    private fun getEpayOTP() {
        paytmPostPaidViewModel.liveDataEPAYScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {

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

    private fun getBalance() {
        paytmPostPaidViewModel.liveDataBalanceScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {
                        binding?.otpView?.show()
                        toast(it.data.msg)
                    } else {
                        binding?.otpView?.hide()
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
                    binding?.otpView?.hide()
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
    private fun postPaidPay() {
        paytmPostPaidViewModel.liveDatapayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data?.output?.p == true) {
                            Constant().printTicket(this)
                            finish()
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    onBackPressed()
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
                            positiveClick = {
                                            onBackPressed()
                            },
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
    private fun postPaidSendOTP() {
        paytmPostPaidViewModel.liveDatasendOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {
                        binding?.otpView?.show()
                        toast(it.data.msg)
                    } else {
                        binding?.otpView?.hide()
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
                    binding?.otpView?.hide()
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
    private fun postPaidVerifyOTP() {
        paytmPostPaidViewModel.liveDataverifyOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                        if (it.data?.result == ("success")) {
                            try {
                                if (it.data.output.bal.toDouble() >= paidAmount.toDouble()) {
                                    binding?.otpView?.hide()
                                    binding?.balanceView?.show()
                                    binding?.balance?.text = "Rs " + it.data.output.bal
                                    binding?.addPay?.text = "Make Payment"
                                } else {
                                    binding?.addPay?.text = "Add Money"

                                    binding?.balance?.text = "Rs " + it.data.output.bal
                                    binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                        0,
                                        0,
                                        R.drawable.close_notif,
                                        0
                                    )
                                    binding?.otpView?.hide()
                                    binding?.balanceView?.show()
                                    binding?.insuficient?.show()
                                    binding?.subBtn?.isEnabled = false
                                    binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                                }
                            } catch (e: Exception) {
                                binding?.balance?.text = "Rs " + it.data.output.bal
                                binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.close_notif,
                                    0
                                )
                                binding?.otpView?.hide()
                                binding?.balanceView?.show()
                                binding?.insuficient?.show()
                                binding?.subBtn?.isEnabled = false
                                binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                                e.printStackTrace()
                            }
                    } else {
//                        binding?.otpView?.hide()
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
                    binding?.otpView?.hide()
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
    private fun postPaidMakePayment() {
        paytmPostPaidViewModel.liveDataMakePaymentScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data?.output?.p == true) {
                            Constant().printTicket(this)
                            finish()
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    onBackPressed()
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }


   private fun getHmac() {
        paytmPostPaidViewModel.liveDataWalletScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {
                        if (it.data.output.state!=null){
                            state_text = it.data.output.state
                        }else {
                            try {
                                if (it.data.output.b.toDouble() >= paidAmount.toDouble()) {
                                    binding?.otpView?.hide()
                                    binding?.balanceView?.show()
                                    binding?.balance?.text = "Rs " + it.data.output.b
                                    binding?.addPay?.text = "Make Payment"
                                } else {
                                    binding?.addPay?.text = "Add Money"
                                    binding?.balance?.text = "Rs " + it.data.output.b
                                    binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                        0,
                                        0,
                                        R.drawable.close_notif,
                                        0
                                    )
                                    binding?.otpView?.hide()
                                    binding?.balanceView?.show()
                                    binding?.insuficient?.show()
                                    binding?.subBtn?.isEnabled = false
                                    binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                                }
                            } catch (e: Exception) {
                                binding?.balance?.text = "Rs " + it.data.output.b
                                binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.close_notif,
                                    0
                                )
                                binding?.otpView?.hide()
                                binding?.balanceView?.show()
                                binding?.insuficient?.show()
                                binding?.subBtn?.isEnabled = false
                                binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                                e.printStackTrace()
                            }
                        }
                    } else {
                        binding?.otpView?.hide()
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
                    binding?.otpView?.hide()
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
    private fun walletSendOTP() {
        paytmPostPaidViewModel.liveWalletsendOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        binding?.otpView?.show()
                        toast(it.data.msg)
                    } else {
                        binding?.otpView?.hide()
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
                    binding?.otpView?.hide()
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
    private fun walletVerifyOTP() {
        paytmPostPaidViewModel.liveWalletverifyOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                        if (it.data?.result == ("success")) {
                            paytmPostPaidViewModel.getWalletHmac(
                                preferences.getUserId(),
                                Constant.BOOKING_ID,
                                Constant.TRANSACTION_ID,
                                Constant.BOOK_TYPE
                            )
                    } else {
                        //binding?.otpView?.hide()
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
                    binding?.otpView?.hide()
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
    private fun walletMakePayment() {
        paytmPostPaidViewModel.liveWalletMakePaymentScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data?.output?.p == true) {
                            Constant().printTicket(this)
                            finish()
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    onBackPressed()
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun freechargeOTP() {
        paytmPostPaidViewModel.freechargeOTPpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                       state_text = it.data.output.otpId
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
    private fun freechargeResend() {
        paytmPostPaidViewModel.freechargeResendpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
//                        val dialog = OptionDialog(this,
//                            R.mipmap.ic_launcher,
//                            R.string.app_name,
//                            it.data.msg.toString(),
//                            positiveBtnText = R.string.ok,
//                            negativeBtnText = R.string.no,
//                            positiveClick = {},
//                            negativeClick = {})
//                        dialog.show()
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
    private fun freechargeLogin() {
        paytmPostPaidViewModel.freechargeLoginpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        try {
                            if (it.data.output.balance.toDouble() > paidAmount.toDouble() || it.data.output.balance.toDouble() == paidAmount.toDouble()) {
                                binding?.otpView?.hide()
                                binding?.balanceView?.show()
                                binding?.balance?.text = "Rs " + it.data.output.balance
                                binding?.addPay?.text = "Make Payment"
                            } else {
                                binding?.title?.setImageResource(R.drawable.freecharge)
                                newAmt = (paidAmount.toDouble()-it.data.output.balance.toDouble()).toString()
                                binding?.addPay?.text = "Add Money"
                                binding?.balance?.text = "Rs " + it.data.output.balance
                                binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.close_notif,
                                    0
                                )
                                binding?.otpView?.hide()
                                binding?.balanceView?.show()
                                binding?.insuficient?.show()
                                binding?.subBtn?.isEnabled = false
                                binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                            }
                        } catch (e: Exception) {
                            binding?.balance?.text = "Rs " + it.data.output.balance
                            binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.close_notif,
                                0
                            )
                            binding?.otpView?.hide()
                            binding?.balanceView?.show()
                            binding?.insuficient?.show()
                            binding?.subBtn?.isEnabled = false
                            binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                            e.printStackTrace()
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
    private fun freechargeDetail() {
        paytmPostPaidViewModel.freechargeDetailpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        try {
                            if (it.data.output.balance.toDouble() > paidAmount.toDouble()
                                || it.data.output.balance.toDouble() == paidAmount.toDouble()
                            ) {
                                binding?.otpView?.hide()
                                binding?.balanceView?.show()
                                binding?.balance?.text = "Rs " + it.data.output.balance
                                binding?.addPay?.text = "Make Payment"
                            } else {
                                newAmt = (paidAmount.toDouble()-it.data.output.balance.toDouble()).toString()
                                binding?.addPay?.text = "Add Money"
                                binding?.balance?.text = "Rs " + it.data.output.balance
                                binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.close_notif,
                                    0
                                )
                                binding?.otpView?.hide()
                                binding?.balanceView?.show()
                                binding?.insuficient?.show()
                                binding?.subBtn?.isEnabled = false
                                binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                            }
                        } catch (e: Exception) {
                            binding?.balance?.text = "Rs " + it.data.output.balance
                            binding?.balance?.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.close_notif,
                                0
                            )
                            binding?.otpView?.hide()
                            binding?.balanceView?.show()
                            binding?.insuficient?.show()
                            binding?.subBtn?.isEnabled = false
                            binding?.subBtn?.setTextColor(resources.getColor(R.color.disabled_text))
                            e.printStackTrace()
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
    private fun freechargePayment() {
        paytmPostPaidViewModel.freechargePaymentpayScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data?.output?.p == true) {
                            Constant().printTicket(this)
                            finish()
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    onBackPressed()
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

    override fun onResume() {
        super.onResume()
        if (callFc){
            paytmPostPaidViewModel.freechargeDetail(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
            freechargeDetail()
        }
    }

}