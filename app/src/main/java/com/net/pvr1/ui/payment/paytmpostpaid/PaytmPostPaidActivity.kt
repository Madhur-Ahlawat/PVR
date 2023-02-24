package com.net.pvr1.ui.payment.paytmpostpaid

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
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
class PaytmPostPaidActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPaytmPostpaidBinding? = null
    private val paytmPostPaidViewModel: PaytmPostPaidViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var catFilterPayment = ArrayList<PaymentResponse.Output.Gateway>()
    private var paidAmount = ""
    private var title = ""
    var state_text = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaytmPostpaidBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.header?.textView108?.text = intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()
        paidAmount = intent.getStringExtra("paidAmount").toString()
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
        }else {
            paytmPostPaidViewModel.getBalance(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE
            )
        }

        binding?.subBtn?.setOnClickListener(View.OnClickListener {
            if (title == "epaylater"){

            }else if (title == "Paytm"){
                if (binding?.subBtn?.text.toString().equals("Make Payment", ignoreCase = true))
                    paytmPostPaidViewModel.walletMakePayment(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                else if (binding?.etOtp?.getStringFromFields()?.length!! >= 6) {
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
            }else {
                if (binding?.subBtn?.text.toString().equals("Make Payment", ignoreCase = true))
                    paytmPostPaidViewModel.postPaidPay(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE
                    )
                else if (binding?.etOtp?.getStringFromFields()?.length!! >= 6) {
                    println("binding?.etOtp?.getStringFromFields().toString().length"+binding?.etOtp?.getStringFromFields().toString().length)
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
            if (title.equals("epaylater", ignoreCase = true)) {
                getEpayOTP()
            } else if (title == "Paytm"){
                paytmPostPaidViewModel.walletSendOTP(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.TRANSACTION_ID,
                    Constant.BOOK_TYPE,
                    preferences.getString(Constant.SharedPreference.USER_NUMBER)
                )
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
                                if (it.data.output.bal.toDouble() > paidAmount.toDouble()
                                    || it.data.output.bal.toDouble() == paidAmount.toDouble()
                                ) {
                                    binding?.otpView?.hide()
                                    binding?.balanceView?.show()
                                    binding?.balance?.text = "Rs " + it.data.output.bal
                                    binding?.subBtn?.text = "Make Payment"
                                } else {
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
                                if (it.data.output.b.toDouble() > paidAmount.toDouble()
                                    || it.data.output.b.toDouble() == paidAmount.toDouble()
                                ) {
                                    binding?.otpView?.hide()
                                    binding?.balanceView?.show()
                                    binding?.balance?.text = "Rs " + it.data.output.b
                                    binding?.subBtn?.text = "Make Payment"
                                } else {
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