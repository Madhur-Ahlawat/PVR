package com.net.pvr.ui.payment.promoCode

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPromoCodeBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.payment.PaymentActivity
import com.net.pvr.ui.payment.promoCode.viewModel.PromoCodeViewModel
import com.net.pvr.ui.payment.response.Promomap
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PromoCodeActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPromoCodeBinding? = null
    private var title:String = ""
    private var loader: LoaderDialog? = null
    private var stringtex = ""
    private var hit_count = 0
    private var type = "PROMO"
    private val promoCodeViewModel: PromoCodeViewModel by viewModels()
    private var promomap: ArrayList<Promomap> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromoCodeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include29?.textView108?.text = intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()
        //PaidAmount
        binding?.textView178?.text = getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")
        binding?.include29?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        type = intent.extras?.getString("type").toString();
        if (type == "PROMO" || type == "ACCENTIVE") {
            binding?.ccInputLayout?.hint = "Enter Promo code"
            binding?.ccLayout?.show()
            binding?.hayattView?.hide()
            binding?.include30?.textView5?.text = "APPLY"
        } else if (type == "GYFTR") {
            binding?.ccInputLayout?.hint = "Enter Voucher code"
            binding?.ccLayout?.show()
            binding?.hayattView?.hide()
            binding?.include30?.textView5?.text = "APPLY"
        }else{
            binding?.ccLayout?.hide()
            binding?.hayattView?.show()
            binding?.include30?.textView5?.text = "SEND OTP"
        }
        if (intent.extras?.getBoolean("ca_a") === false)
            binding?.textView371?.text = intent.extras?.getString("ca_t")


        binding?.include30?.constraintLayout10?.setOnClickListener(View.OnClickListener {
            if (validateInputFields())
                setDataToApi()
            Constant().hideKeyboard(this)
        })

        try {
            val bundle = intent.extras
            if (bundle != null) {
                promomap = bundle.getParcelableArrayList<Promomap>("Promomaplist")!!
            }
        } catch (e: Exception) {
        }

        callPromoData()
        callPromoGyftr()
        callACCENTIVE()

    }

    private fun openDialog() {

        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            Html.fromHtml(stringtex).toString(),
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.no,
            positiveClick = {
                usePromoCode()
            },
            negativeClick = {

            })
        dialog.show()

    }

    private fun usePromoCode(){
        if (type == "PROMO") {
            promoCodeViewModel.promoCode(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,
                binding?.ccEditText?.text.toString()
            )
        }else if (type == "GYFTR"){
            promoCodeViewModel.promoGyft(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,
                binding?.ccEditText?.text.toString()
            )
        }else if (type == "ACCENTIVE"){
            promoCodeViewModel.accentivePromo(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,
                binding?.ccEditText?.text.toString()
            )
        }else if (type == "HYATT"){
            if (hit_count==0) {
                if (validatePhone()) {
                    promoCodeViewModel.hyattSendOTP(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE,
                        binding?.phonelIdEditText?.text.toString()
                    )
                }
            }else{
                if (validateInputOTP()) {
                    promoCodeViewModel.hyattVerifyOTP(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        Constant.BOOK_TYPE,
                        binding?.phonelIdEditText?.text.toString(),
                        binding?.otpEditText?.getStringFromFields().toString()
                    )
                }
            }
        }
    }


    private fun checkpromo(): Boolean {
        var retVal = false
        for (data in promomap) {
            if (data.key.equals(binding?.ccEditText?.text.toString().uppercase(Locale.getDefault()))) {
                stringtex = data.value.toString()
                retVal = true
                break
            }
        }
        return retVal
    }

    private fun setDataToApi() {
        if (promomap.isNotEmpty()) {
            if (checkpromo()) {
                openDialog()
            } else {
                usePromoCode()
            }
        } else {
            usePromoCode()
        }
    }
    private fun validateInputFields(): Boolean {
        if (!InputTextValidator.hasText(binding?.ccEditText!!)) {
            binding?.errorText?.text = (getString(R.string.promo_code_msg_required))
        } else {
            binding?.errorText?.text = ""
        }
        if (!InputTextValidator.validatePromoCode(binding?.ccEditText!!)) {
            if (binding?.ccEditText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding?.errorText?.text = (getString(R.string.promo_code_msg_required))
            } else binding?.errorText?.text = (getString(R.string.promo_code_msg_invalid))

        } else {
            binding?.errorText?.text = ""
        }
        return  (InputTextValidator.hasText(binding?.ccEditText!!) && InputTextValidator.validatePromoCode(binding?.ccEditText!!))
    }


    private fun callPromoData() {
        promoCodeViewModel.livePromoCodeScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.p != null) {
                            if (it.data.output.bin != null) {
                                val binSeries: String = it.data.output.bin
                                preferences.saveString(
                                    Constant.SharedPreference.Promo_Bin_Series,
                                    binSeries
                                )
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, true)
                            } else
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, false)
                            PaymentActivity.isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals("LOYALTYUNLIMITED", ignoreCase = true)) {
//                                    val intent =
//                                        Intent(this, Subscription_Promo_Payment::class.java)
//                                    intent.putExtra(
//                                        PCConstants.IntentKey.TICKET_BOOKING_DETAILS,
//                                        paymentIntentData
//                                    )
//                                    intent.putExtra("bookid", bookingId)
//                                    intent.putExtra(PCConstants.IntentKey.BOOK_TYPE, paymentType)
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                    startActivity(intent)
//                                    finish()
                                } else {
                                    Constant.discount_val = it.data.output.di
                                    Constant.discount_txt = it.data.output.txt
                                    Constant.isPromoCode = binding?.ccEditText?.text.toString()
                                    launchPaymentActivity(
                                        PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,intent.getStringExtra("paidAmount").toString()
                                    ,binding?.ccEditText?.text.toString())

                                }
                            }
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
    private fun callPromoGyftr() {
        promoCodeViewModel.liveDatapromoGyftScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.p != null) {
                            if (it.data.output.bin != null) {
                                val binSeries: String = it.data.output.bin
                                preferences.saveString(
                                    Constant.SharedPreference.Promo_Bin_Series,
                                    binSeries
                                )
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, true)
                            } else
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, false)
                            PaymentActivity.isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals("LOYALTYUNLIMITED", ignoreCase = true)) {
//                                    val intent =
//                                        Intent(this, Subscription_Promo_Payment::class.java)
//                                    intent.putExtra(
//                                        PCConstants.IntentKey.TICKET_BOOKING_DETAILS,
//                                        paymentIntentData
//                                    )
//                                    intent.putExtra("bookid", bookingId)
//                                    intent.putExtra(PCConstants.IntentKey.BOOK_TYPE, paymentType)
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                    startActivity(intent)
//                                    finish()
                                } else {
                                    Constant.discount_val = it.data.output.di
                                    Constant.discount_txt = it.data.output.txt
                                    launchPaymentActivity(
                                        PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,intent.getStringExtra("paidAmount").toString()
                                   ,"GYFTR" )

                                }
                            }
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
    private fun callACCENTIVE() {
        promoCodeViewModel.accentivePromoOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.p != null) {
                            if (it.data.output.bin != null) {
                                val binSeries: String = it.data.output.bin
                                preferences.saveString(
                                    Constant.SharedPreference.Promo_Bin_Series,
                                    binSeries
                                )
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, true)
                            } else
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, false)
                            PaymentActivity.isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals("LOYALTYUNLIMITED", ignoreCase = true)) {
//                                    val intent =
//                                        Intent(this, Subscription_Promo_Payment::class.java)
//                                    intent.putExtra(
//                                        PCConstants.IntentKey.TICKET_BOOKING_DETAILS,
//                                        paymentIntentData
//                                    )
//                                    intent.putExtra("bookid", bookingId)
//                                    intent.putExtra(PCConstants.IntentKey.BOOK_TYPE, paymentType)
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                    startActivity(intent)
//                                    finish()
                                } else {
                                    Constant.discount_val = it.data.output.di
                                    Constant.discount_txt = it.data.output.txt
                                    launchPaymentActivity(
                                        PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,intent.getStringExtra("paidAmount").toString()
                                    ,"ACCENTIVE")


                                }
                            }
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


    private fun sendHyattOTP() {
        promoCodeViewModel.hyattSendOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {
                        toast(it.data.msg)
                        hit_count = 1
                        binding?.otpLayout?.show()
                        binding?.include30?.textView5?.text = "APPLY"

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

    private fun verifyHayat() {
        promoCodeViewModel.hyattVerifyOTPScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.p != null) {
                            if (it.data.output.bin != null) {
                                val binSeries: String = it.data.output.bin
                                preferences.saveString(
                                    Constant.SharedPreference.Promo_Bin_Series,
                                    binSeries
                                )
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, true)
                            } else
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, false)
                            PaymentActivity.isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals("LOYALTYUNLIMITED", ignoreCase = true)) {
//                                    val intent =
//                                        Intent(this, Subscription_Promo_Payment::class.java)
//                                    intent.putExtra(
//                                        PCConstants.IntentKey.TICKET_BOOKING_DETAILS,
//                                        paymentIntentData
//                                    )
//                                    intent.putExtra("bookid", bookingId)
//                                    intent.putExtra(PCConstants.IntentKey.BOOK_TYPE, paymentType)
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                    startActivity(intent)
//                                    finish()
                                } else {
                                    Constant.discount_val = it.data.output.di
                                    Constant.discount_txt = it.data.output.txt
                                    launchPaymentActivity(
                                        PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,intent.getStringExtra("paidAmount").toString()
                                    ,"ACCENTIVE")


                                }
                            }
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

    private fun validatePhone(): Boolean {
        if (!InputTextValidator.hasText(binding?.phonelIdEditText!!)) {
            binding?.phoneLayout?.error = getString(R.string.mobile_msg_required)
        } else {
            binding?.phoneLayout?.error = getString(R.string.mobile_msg)
        }
        if (!InputTextValidator.validateNumber(binding?.phonelIdEditText!!)) {
            if (binding?.phonelIdEditText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding?.phoneLayout?.error = getString(R.string.mobile_msg_required)
            } else binding?.phoneLayout?.error = getString(R.string.mobile_msg_invalid)
        } else {
            binding?.phoneLayout?.error = getString(R.string.mobile_msg)
        }
        return  (InputTextValidator.hasText(binding?.phonelIdEditText!!) && InputTextValidator.validateNumber(
                binding?.phonelIdEditText!!
            )
        )
    }

    private fun validateInputOTP(): Boolean {
        if (binding?.otpEditText?.getStringFromFields().toString().isEmpty()) {
            toast(getString(R.string.otp_msg_required))
        } else {
        }
        return (binding?.otpEditText?.getStringFromFields().toString().isNotEmpty())
    }

}