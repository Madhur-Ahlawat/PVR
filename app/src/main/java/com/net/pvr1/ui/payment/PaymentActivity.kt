package com.net.pvr1.ui.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.SystemClock
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPaymentBinding
import com.net.pvr1.databinding.ItemPaymentPrivlegeBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.GiftCardActivity
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.payment.adapter.*
import com.net.pvr1.ui.payment.bankoffers.BankOffersActivity
import com.net.pvr1.ui.payment.cardDetails.CardDetailsActivity
import com.net.pvr1.ui.payment.cred.CredActivity
import com.net.pvr1.ui.payment.giftcardredeem.GiftCardRedeemActivity
import com.net.pvr1.ui.payment.mCoupon.MCouponActivity
import com.net.pvr1.ui.payment.paytmpostpaid.PaytmPostPaidActivity
import com.net.pvr1.ui.payment.promoCode.PromoCodeActivity
import com.net.pvr1.ui.payment.response.*
import com.net.pvr1.ui.payment.viewModel.PaymentViewModel
import com.net.pvr1.ui.payment.webView.PaytmWebActivity
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.ACCENTIVE
import com.net.pvr1.utils.Constant.Companion.AIRTEL
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.CRED
import com.net.pvr1.utils.Constant.Companion.CREDIT_CARD
import com.net.pvr1.utils.Constant.Companion.DEBIT_CARD
import com.net.pvr1.utils.Constant.Companion.DISCOUNT
import com.net.pvr1.utils.Constant.Companion.GEIFT_CARD
import com.net.pvr1.utils.Constant.Companion.GYFTR
import com.net.pvr1.utils.Constant.Companion.HYATT
import com.net.pvr1.utils.Constant.Companion.M_COUPON
import com.net.pvr1.utils.Constant.Companion.NET_BANKING
import com.net.pvr1.utils.Constant.Companion.PAYTMPOSTPAID
import com.net.pvr1.utils.Constant.Companion.PAYTM_WALLET
import com.net.pvr1.utils.Constant.Companion.PHONE_PE
import com.net.pvr1.utils.Constant.Companion.PROMOCODE
import com.net.pvr1.utils.Constant.Companion.STAR_PASS
import com.net.pvr1.utils.Constant.Companion.TRANSACTION_ID
import com.net.pvr1.utils.Constant.Companion.UPI
import com.net.pvr1.utils.Constant.Companion.ZAGGLE
import com.net.pvr1.utils.Constant.Companion.discount_val
import com.net.pvr1.utils.Constant.Companion.isPromoCode
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.TransactionRequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentAdapter.RecycleViewItemClickListenerCity,
    PaymentExclusiveAdapter.RecycleViewItemClickListenerCity,
    CouponAdapter.RecycleViewItemClickListenerCity,
    PaymentPromoCatAdapter.RecycleViewItemClickListenerCity,
    PaymentPromoListAdapter.RecycleViewItemClickListenerCity {

    @Inject
    lateinit var preferences: PreferenceManager
    private var newBinding: ItemPaymentPrivlegeBinding? = null
    private var vocCount = 0
    private var voucherData: CouponResponse.Output.Voucher? = null
    private var binding: ActivityPaymentBinding? = null
    private val authViewModel: PaymentViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var paymentItemHold: PaymentResponse.Output.Gateway? = null
    private var catFilterPayment = ArrayList<PaymentResponse.Output.Gateway>()
    private var paidAmount = ""
    private var promoDialog: Dialog? = null
    private var promoCodeList = ArrayList<PromoCodeList.Output>()
    private var promoNewCodeList = ArrayList<PromoCodeList.Output>()
    private var promoList: RecyclerView? = null
    private var catList: RecyclerView? = null
    private var promoListAdapter: PaymentPromoListAdapter? = null
    private var dcInfo = ""
    private var dc = false
    private var upiCount = 0
    private var upiLoader = false
    private var showPopup = true
    private var actualAmt = "0.0"

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    companion object {
        var subsId = ""
        var subsToken = ""
        var createdAt = ""
        var isPromoCodeApplied = false
        var offerList: ArrayList<PaymentResponse.Output.Binoffer> = ArrayList()

        @SuppressLint("SetTextI18n")
        fun showTncDialog(mContext: Context?, priceText: String, code: String) {
            val dialog = Dialog(mContext!!, R.style.AppTheme_Dialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.promo_success)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window!!.setGravity(Gravity.CENTER)
            val done = dialog.findViewById<View>(R.id.done) as TextView?
            val price = dialog.findViewById<View>(R.id.price) as TextView?
            val promCode = dialog.findViewById<View>(R.id.promcode) as TextView?
            price?.text = "$priceText\nsaved with this coupon code"
            if (code != "") {
                promCode?.text = "'$code'\nApplied"
            } else {
                promCode?.text = ""
            }

            done?.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    @SuppressLint("SetTextI18n")
    private fun manageFunction() {
        Constant.viewModel = authViewModel

        extendTime()

//        title
        binding?.include26?.textView108?.text = getString(R.string.payment)

        //paidAmount
        binding?.textView178?.text =
            getString(R.string.currency) + intent.getStringExtra("paidAmount")

        paidAmount = intent.getStringExtra("paidAmount").toString()
        actualAmt = intent.getStringExtra("paidAmount").toString()


        authViewModel.promoList()

//        //payMode
        authViewModel.payMode(
            CINEMA_ID,
            BOOK_TYPE,
            preferences.getUserId(),
            preferences.geMobileNumber(),
            "",
            "no",
            "",
            false
        )

//        voucherDataLoad()
        if (BOOK_TYPE == "RECURRING") {
            authViewModel.recurringInit(
                preferences.getUserId(), BOOKING_ID
            )
            recurringInit()
        }

        if (BOOK_TYPE == "BOOKING" || BOOK_TYPE == "FOOD") {
            binding?.constraintLayout110?.show()
            //voucher
            val time = SystemClock.uptimeMillis()
            authViewModel.voucher(
                Constant.getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + time),
                preferences.getToken().toString(),
                preferences.getCityName(),
                preferences.getUserId(),
                time.toString()
            )
        } else {
            binding?.constraintLayout110?.hide()
        }

        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()

        payModeDataLoad()
        paytmHMAC()
        credCheck()
        upiStatus()
        phonePeHmac()
        phonePeStatus()
        voucherDataLoad()
        redeemLoyaltyVoucher()
        promoListData()
        removePromo()
        callPromoData()
        movedNext()
    }

    private fun movedNext() {
        binding?.include26?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.cutPrice?.paintFlags =
            binding?.cutPrice?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG

        binding?.availOffers?.setOnClickListener {
            openPromo()
        }

        binding?.apply?.setOnClickListener {
            if (validateInputFields()) {
                authViewModel.promoCode(
                    preferences.getUserId(),
                    BOOKING_ID,
                    TRANSACTION_ID,
                    BOOK_TYPE,
                    binding?.promoCode?.text.toString()
                )

            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        registerReceiver(br, IntentFilter(BroadcastService.COUNTDOWN_BR))

        if (discount_val != "0.0") {
            binding?.cutPrice?.show()
            actualAmt = (intent.getStringExtra("paidAmount")
                ?.toDouble()!! - discount_val.toDouble()).toString()
            binding?.textView178?.text = getString(R.string.currency) + actualAmt
            binding?.cutPrice?.text =
                getString(R.string.currency) + intent.getStringExtra("paidAmount").toString()
            if (isPromoCode != "") {
                binding?.promoCode?.setText(isPromoCode)
                binding?.cutPrice?.paintFlags =
                    binding?.cutPrice?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG
                binding?.applyCou?.show()
                binding?.cutPrice?.show()
                binding?.couponView?.hide()
                DISCOUNT += discount_val.toDouble()
                binding?.promoCodetxt?.text = "'${binding?.promoCode?.text}' Applied"
                binding?.applyco?.text = getString(R.string.currency) + discount_val + " Saved"
                binding?.removeoff?.setOnClickListener {
                    authViewModel.removePromo(
                        preferences.getString(Constant.SharedPreference.USER_NUMBER),
                        BOOKING_ID,
                        BOOK_TYPE
                    )
                }
                actualAmt =
                    (intent.getStringExtra("paidAmount")?.toDouble()!! - DISCOUNT).toString()
                binding?.textView178?.text = getString(R.string.currency) + actualAmt
                binding?.cutPrice?.text =
                    getString(R.string.currency) + intent.getStringExtra("paidAmount").toString()
                showTncDialog(this, discount_val, isPromoCode)
            } else {
                showTncDialog(this, discount_val, "")
            }
        }

        if (Constant.CALL_PAYMODE == 1) {
            authViewModel.payMode(
                CINEMA_ID,
                BOOK_TYPE,
                preferences.getUserId(),
                preferences.geMobileNumber(),
                "",
                "no",
                "",
                false
            )
            //payModeDataLoad()
        }
    }

    private fun validateInputFields(): Boolean {
        if (!InputTextValidator.hasText(binding?.promoCode!!)) {
            binding?.promoCodeValidationMessage?.text = getString(R.string.promo_code_msg_required)
        }

        if (!InputTextValidator.validatePromoCode(binding?.promoCode!!)) {
            // promoCode.setBackground(ContextCompat.getDrawable(this, R.drawable.offeraply));
            if (binding?.promoCode?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding?.promoCodeValidationMessage?.text =
                    getString(R.string.promo_code_msg_required)
            } else binding?.promoCodeValidationMessage?.text =
                getString(R.string.promo_code_msg_invalid)
        } else {
            //  promoCode.setBackground(ContextCompat.getDrawable(this, R.drawable.offeraply));
//            promoCodeValidationMessage.setText(getString(R.string.promo_code_msg));
        }

        return InputTextValidator.hasText(binding?.promoCode!!) && InputTextValidator.validatePromoCode(
            binding?.promoCode!!
        )
    }

    private fun voucherDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveDataCoupon(it.data.output)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveDataCoupon(output: ArrayList<CouponResponse.Output>) {
        if (output.isNotEmpty()) {
            binding?.recyclerView46?.show()
            binding?.textView180?.show()
            val list = ArrayList<CouponResponse.Output.Voucher>()
            for (data in output) {
                list.addAll(data.vouchers)
            }
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val foodBestSellerAdapter = CouponAdapter(list, this, this, preferences)
            binding?.recyclerView46?.layoutManager = layoutManagerCrew
            binding?.recyclerView46?.adapter = foodBestSellerAdapter
            binding?.recyclerView46?.setHasFixedSize(true)
        } else {
            binding?.textView180?.hide()
            binding?.recyclerView46?.hide()
        }
    }

    private fun promoListData() {
        authViewModel.promoListLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        promoCodeList = it.data.output
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun removePromo() {
        authViewModel.removePromoCodeScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        binding?.couponView?.show()
                        binding?.applyCou?.hide()
                        binding?.promoCode?.setText("")
                        actualAmt = (actualAmt.toDouble() + it.data.output.di.toDouble()).toString()
                        binding?.textView178?.text = getString(R.string.currency) + actualAmt
                        binding?.cutPrice?.hide()
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun payModeDataLoad() {
        authViewModel.payModeResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result) {
                        catFilterPayment = it.data.output.gateway
                        retrieveData(it.data.output)
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

    private fun retrieveData(output: PaymentResponse.Output) {
        //design
        binding?.constrainLayout166?.show()

        //shimmer
        binding?.constraintLayout145?.hide()

        if (output.dcinfo != null) {
            dcInfo = output.dcinfo
            dc = output.dc
        }
        //Bank Offer
        if (output.binoffers.isNotEmpty()) {
            binding?.bankOffers?.show()
            binding?.bnView?.show()
            offerList = output.binoffers
        } else {
            binding?.bnView?.hide()
            binding?.bankOffers?.hide()
        }

        //Pay Mode
        if (output.gateway.isNotEmpty()) {
            binding?.recyclerView42?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter =
                PaymentAdapter(payMethodFilter("PAYMENT-METHOD"), this, this)
            binding?.recyclerView42?.layoutManager = layoutManagerCrew
            binding?.recyclerView42?.adapter = foodBestSellerAdapter
            binding?.recyclerView42?.setHasFixedSize(true)
        } else {
            binding?.recyclerView42?.hide()
        }

        //Other Payment Method
        if (output.gateway.isNotEmpty() && payMethodFilter("GATEWAY").size > 0) {
            binding?.otherPayView?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = PaymentAdapter(payMethodFilter("GATEWAY"), this, this)
            binding?.recyclerView44?.layoutManager = layoutManagerCrew
            binding?.recyclerView44?.adapter = foodBestSellerAdapter
            binding?.recyclerView44?.setHasFixedSize(true)
        } else {
            binding?.otherPayView?.hide()
        }

        //Wallets
        if (output.gateway.isNotEmpty()) {
            binding?.walletView?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = PaymentAdapter(payMethodFilter("WALLET"), this, this)
            binding?.recyclerView43?.layoutManager = layoutManagerCrew
            binding?.recyclerView43?.adapter = foodBestSellerAdapter
            binding?.recyclerView43?.setHasFixedSize(true)
        } else {
            binding?.walletView?.hide()
        }

        // Offer
        if (output.offers.isNotEmpty()) {
            binding?.offerView?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val paymentExclusiveAdapter = PaymentExclusiveAdapter(output.offers, this, this)
            binding?.recyclerView45?.layoutManager = layoutManagerCrew
            binding?.recyclerView45?.adapter = paymentExclusiveAdapter
            binding?.recyclerView45?.setHasFixedSize(true)
        } else {
            binding?.offerView?.hide()
        }

        binding?.bankOffers?.setOnClickListener {
            val intent = Intent(this@PaymentActivity, BankOffersActivity::class.java)
            intent.putExtra("paidAmount", actualAmt)
            startActivity(intent)
        }

    }

    //Payment Option Clicks
    override fun paymentClick(paymentItem: PaymentResponse.Output.Gateway) {
        when (paymentItem.id.uppercase(Locale.getDefault())) {
            UPI -> {
                authViewModel.paytmHMAC(
                    preferences.getUserId(),
                    BOOKING_ID,
                    TRANSACTION_ID,
                    false,
                    "",
                    BOOK_TYPE,
                    paymentItem.name,
                    "no",
                    "NO"
                )
            }
            CREDIT_CARD -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("paidAmount", actualAmt)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("tc", paymentItem.tc)
                startActivity(intent)
            }
            CRED -> {
                paymentItemHold = paymentItem
                authViewModel.credCheck(
                    preferences.getUserId(),
                    BOOKING_ID,
                    BOOK_TYPE,
                    TRANSACTION_ID,
                    "false",
                    Constant.isPackageInstalled(packageManager).toString(),
                    "false"
                )

            }
            AIRTEL -> {
                val intent = Intent(this@PaymentActivity, PaytmWebActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("paidAmount", actualAmt)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("tc", paymentItem.tc)
                startActivity(intent)
            }
            DEBIT_CARD -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("paidAmount", actualAmt)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("tc", paymentItem.tc)
                startActivity(intent)
            }
            NET_BANKING -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            PHONE_PE -> {
                authViewModel.phonepeHMAC(
                    preferences.getUserId(), BOOKING_ID, BOOK_TYPE, TRANSACTION_ID
                )
            }
            PAYTMPOSTPAID -> {
                val intent = Intent(this, PaytmPostPaidActivity::class.java)
                intent.putExtra("type", "PP")
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            PAYTM_WALLET -> {
                val intent = Intent(this, PaytmPostPaidActivity::class.java)
                intent.putExtra("type", "P")
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            GYFTR -> {
                val intent = Intent(this, PromoCodeActivity::class.java)
                intent.putExtra("type", "GYFTR")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            GEIFT_CARD -> {
                val intent = Intent(this, GiftCardRedeemActivity::class.java)
                intent.putExtra("type", "GEIFT_CARD")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("c", paymentItem.c)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            ZAGGLE -> {
                val intent = Intent(this, GiftCardRedeemActivity::class.java)
                intent.putExtra("type", "ZAGGLE")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("c", paymentItem.c)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            ACCENTIVE -> {
                val intent = Intent(this, PromoCodeActivity::class.java)
                intent.putExtra("type", "ACCENTIVE")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
        }
    }

    private fun credCheck() {
        authViewModel.credCheckLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.state == "true") {
                            val intent = Intent(this@PaymentActivity, CredActivity::class.java)
                            intent.putExtra("bannertext", it.data.output.banner_txt)
                            intent.putExtra("icon", it.data.output.icon)
                            intent.putExtra("paidAmount", actualAmt)
                            intent.putExtra("msg", it.data.output.msg)
                            intent.putExtra("mode", it.data.output.mode)
                            intent.putExtra("pid", paymentItemHold?.id)
                            intent.putExtra("tc", paymentItemHold?.tc)
                            intent.putExtra("ca_a", paymentItemHold?.ca_a)
                            intent.putExtra("ca_t", paymentItemHold?.ca_t)
                            intent.putExtra("title", paymentItemHold?.name)
                            startActivity(intent)
                        } else {
                            toast(it.data.msg)
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

    private fun paytmHMAC() {
        authViewModel.liveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveDataUpi(it.data.output)
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

    private fun recurringInit() {
        authViewModel.recurringInitLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        subsId = it.data.output.subscriptionid
                        subsToken = it.data.output.token
                        createdAt = it.data.output.createdAt
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

    private fun upiStatus() {
        authViewModel.upiStatusResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveStatusUpi(it.data.output)
                    } else {
                        loader?.dismiss()
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
                    if (!upiLoader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
                }
            }
        }

    }

    private fun retrieveStatusUpi(output: UPIStatusResponse.Output) {
        upiLoader = true
        if (output.p.equals("PAID", ignoreCase = true)) {
            loader?.dismiss()
            Constant().printTicket(this@PaymentActivity)
        } else if (output.p.equals("PENDING", ignoreCase = true)) {
            if (upiCount <= 6) {
                val handler = Handler()
                upiCount += 1
                handler.postDelayed({ // close your dialog
                    authViewModel.upiStatus(
                        BOOKING_ID, BOOK_TYPE
                    )
                }, 10000)
            } else {
                loader?.dismiss()
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "If you want to try again then press retry otherwise press cancel.",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }
        } else {
            loader?.dismiss()
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "Transaction Failed!",
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        }
    }

    private fun retrieveDataUpi(output: PaytmHmacResponse.Output) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(output.deepLink)
        startActivityForResult(intent, 120)
//        resultLauncher.launch(intent,120)
    }

//    var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data: Intent? = result.data
//            doSomeOperations()
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            val intent = Intent(this, PaymentStatusActivity::class.java)
            intent.putExtra("title", "UPI")
            intent.putExtra("paidAmount", actualAmt)
            startActivity(intent)

        } else if (resultCode == 300) {
            val intent = Intent(this, PaymentStatusActivity::class.java)
            intent.putExtra("title", "PhonePe")
            intent.putExtra("paidAmount", actualAmt)
            startActivity(intent)
//            var bundle: Bundle
//            val txnResult: String? = null
//            if (data != null) {
//                if (resultCode == RESULT_OK) {
//                    authViewModel.phonepeStatus(
//                        preferences.getUserId(), BOOKING_ID, BOOK_TYPE, TRANSACTION_ID
//                    )
//                } else if (resultCode == RESULT_CANCELED) {
//                    val dialog = OptionDialog(this,
//                        R.mipmap.ic_launcher,
//                        R.string.app_name,
//                        "Transaction Failed!",
//                        positiveBtnText = R.string.ok,
//                        negativeBtnText = R.string.no,
//                        positiveClick = {
//
//                        },
//                        negativeClick = {})
//                    dialog.show()
//                }
//            }
        }
    }

    override fun paymentExclusiveClick(paymentItem: PaymentResponse.Output.Offer) {
        when (paymentItem.id) {
            PROMOCODE -> {
                val intent = Intent(this, PromoCodeActivity::class.java)
                intent.putExtra("type", "PROMO")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)

                //intent.putExtra(PCConstants.BI_PT,bi_pt);
                try {
                    if (paymentItem.promomap != null) {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList(
                            "Promomaplist", paymentItem.promomap as ArrayList<out Parcelable?>
                        )
                        intent.putExtras(bundle)
                    }
                } catch (e: Exception) {
                }
                startActivity(intent)
            }
            GYFTR -> {
                val intent = Intent(this, PromoCodeActivity::class.java)
                intent.putExtra("type", "PROMO")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            HYATT -> {
                val intent = Intent(this, PromoCodeActivity::class.java)
                intent.putExtra("type", "HYATT")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            ACCENTIVE -> {
                val intent = Intent(this, PromoCodeActivity::class.java)
                intent.putExtra("type", "ACCENTIVE")
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            STAR_PASS -> {
                val intent = Intent(this@PaymentActivity, MCouponActivity::class.java)
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
            M_COUPON -> {
                val intent = Intent(this@PaymentActivity, MCouponActivity::class.java)
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", actualAmt)
                startActivity(intent)
            }
        }


    }

    override fun couponClick(
        data: CouponResponse.Output.Voucher, position: Int, binding: ItemPaymentPrivlegeBinding
    ) {
        newBinding = binding
        voucherData = data
        if (showPopup && dc) {
            showPopup(this, dcInfo)
        } else {
            if (data.type == "SUBSCRIPTION") {
                authViewModel.voucherApply(
                    data.cd,
                    preferences.getUserId(),
                    BOOK_TYPE,
                    BOOKING_ID,
                    TRANSACTION_ID,
                    data.tp,
                    "YES",
                    (data.amt * 100).toString()
                )
            } else {
                authViewModel.voucherApply(
                    data.cd,
                    preferences.getUserId(),
                    BOOK_TYPE,
                    BOOKING_ID,
                    TRANSACTION_ID,
                    data.tp,
                    "NO",
                    (data.amt * 100).toString()
                )
            }
            // view.setAlpha(0.5f);
        }

    }

    private fun payMethodFilter(
        category: String
    ): ArrayList<PaymentResponse.Output.Gateway> {
        val categoryPaymentNew = ArrayList<PaymentResponse.Output.Gateway>()
        when (category) {
            "WALLET" -> {
                for (data in catFilterPayment) {
                    if (data.pty == category) categoryPaymentNew.add(data)
                }
            }
            "GATEWAY" -> {
                for (data in catFilterPayment) {
                    if (data.pty == category) categoryPaymentNew.add(data)
                }
            }
            "PAYMENT-METHOD" -> {
                for (data in catFilterPayment) {
                    if (data.pty == category) categoryPaymentNew.add(data)
                }
            }
        }
        return categoryPaymentNew
    }

    private fun phonePeHmac() {
        authViewModel.phonepeLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val transactionRequest2 =
                            TransactionRequestBuilder().setData(it.data.output.bs)
                                .setChecksum(it.data.output.bs).setUrl(it.data.output.bs).build()

                        startActivityForResult(
                            PhonePe.getTransactionIntent(
                                transactionRequest2
                            )!!, 300
                        )

                    } else {
                        loader?.dismiss()
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
                    if (!upiLoader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun phonePeStatus() {
        authViewModel.phonepeStatusLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        if (it.data.output.p != "false") {
                            Constant().printTicket(this@PaymentActivity)
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        }

                    } else {
                        loader?.dismiss()
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
                    if (!upiLoader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
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
                when (BOOK_TYPE) {
                    "GIFTCARD" -> {
                        launchActivity(
                            GiftCardActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    }
                    "BOOKING", "FOOD" -> {
                        launchActivity(
                            HomeActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    }
                    else -> {
                        launchPrivilegeActivity(
                            HomeActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,
                            "",
                            "",
                            "",
                            "P"
                        )
                    }
                }
            },
            negativeClick = {})
        dialog.show()
    }

    private fun showPopup(mContext: Context, text: String?) {
        val dialog = BottomSheetDialog(mContext, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loyalty_instruction)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val title: TextView = dialog.findViewById<TextView>(R.id.title) as TextView
        val crossImg = dialog.findViewById<ImageView>(R.id.crossImg)
        val proceed: TextView = dialog.findViewById<TextView>(R.id.tvSubmitFeedback) as TextView
        title.text = Html.fromHtml(text)
        crossImg!!.setOnClickListener { dialog.dismiss() }
        proceed.setOnClickListener { dialog.dismiss() }
        dialog.show()
        showPopup = false
    }

    @SuppressLint("SetTextI18n")
    private fun redeemLoyaltyVoucher() {
        authViewModel.voucherApplyLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val isP: String = it.data.output.p
                        vocCount += 1
                        DISCOUNT += it.data.output.di.toDouble()
                        binding?.discountVocher?.show()
                        binding?.cutPrice?.show()
                        binding?.discountVocher?.text =
                            "$vocCount Vouchers worth $DISCOUNT Redeemed!"
                        actualAmt = (intent.getStringExtra("paidAmount")
                            ?.toDouble()!! - DISCOUNT).toString()
                        binding?.textView178?.text = getString(R.string.currency) + actualAmt
                        binding?.cutPrice?.text =
                            getString(R.string.currency) + intent.getStringExtra("paidAmount")
                                .toString()
                        newBinding?.parrentView?.isEnabled = false
                        printLog(voucherData?.voucher_type + voucherData?.type)
                        if (voucherData?.type.equals("SUBSCRIPTION", ignoreCase = true)) {
                            newBinding?.ivSubs?.setImageResource(R.drawable.subs_gray)
                        } else {
                            when (voucherData?.tp) {
                                ("D") -> {
                                    newBinding?.ivSubs?.setImageResource(R.drawable.voucher_ticket_grey)
                                }
                                ("C") -> {
                                    when (voucherData?.sc) {
                                        27 -> {
                                            newBinding?.ivSubs?.setImageResource(R.drawable.voucher_popcorn_grey)
                                        }
                                        35 -> {
                                            newBinding?.ivSubs?.setImageResource(R.drawable.voucher_popcorn_grey)
                                        }
                                        2 -> {
                                            newBinding?.ivSubs?.setImageResource(R.drawable.voucher_popcorn_grey)
                                        }
                                        else -> newBinding?.ivSubs?.setImageResource(R.drawable.voucher_f_n_b_grey)
                                    }
                                }
                                ("T") -> {
                                    newBinding?.ivSubs?.setImageResource(R.drawable.voucher_ticket_grey)
                                }
                                ("27") -> {
                                    newBinding?.ivSubs?.setImageResource(R.drawable.voucher_popcorn_grey)
                                }
                                ("35") -> {
                                    newBinding?.ivSubs?.setImageResource(R.drawable.voucher_popcorn_grey)
                                }
                            }
                        }

                        if (isP.equals("false", ignoreCase = true)) {
                            newBinding?.ivRedeemed?.visibility = View.GONE
                            val newColor = resources.getColor(R.color.gray)
                            if (voucherData?.type == "SUBSCRIPTION") {
                                showDialogLoyalty(
                                    this, "Congratulations!", "YES", true, it.data.output.di
                                )
                            } else {
                                showDialogLoyalty(
                                    this, "Congratulations!", "NO", true, it.data.output.di
                                )
                            }
                        } else if (isP.equals("true", ignoreCase = true)) {
                            Constant().printTicket(this)
                        }
                    } else {
                        loader?.dismiss()
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
                    if (!upiLoader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showDialogLoyalty(
        mContext: Context?, title_text: String?, message_text: String, success: Boolean, dis: String
    ) {
        val dialog = BottomSheetDialog(mContext!!, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.pp_payment_success)
        dialog.setCancelable(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val title = dialog.findViewById<TextView>(R.id.title) as TextView
        val message = dialog.findViewById<TextView>(R.id.message) as TextView
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn) as TextView
        if (success) {
            title.setTextColor(resources.getColor(R.color.black))
        } else {
            title.setTextColor(resources.getColor(R.color.red))
        }
        title.text = title_text
        if (message_text.equals("YES", ignoreCase = true)) {
            message.text = "You have saved ₹$dis using your PVR Passport voucher! Keep booking!"
        } else if (message_text.equals("NO", ignoreCase = true)) {
            message.text = "You have saved ₹$dis using your PVR Privilege voucher! Keep booking!"
        } else {
            message.text = Html.fromHtml(message_text)
        }
        okBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun openPromo() {
        promoDialog = Dialog(this, R.style.AppTheme_FullScreenDialog)
        promoDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        promoDialog?.setContentView(R.layout.promo_dialog)
        catList = promoDialog?.findViewById(R.id.recyclerView) as RecyclerView
        val searchTextView: EditText = promoDialog?.findViewById(R.id.searchTextView) as EditText
        val clearBtn: ImageView = promoDialog?.findViewById(R.id.clearBtn) as ImageView
        val cancel: ImageView = promoDialog?.findViewById(R.id.imageView4) as ImageView
        cancel.setOnClickListener { promoDialog?.dismiss() }
        promoList = promoDialog?.findViewById(R.id.promoList) as RecyclerView
        catList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val promoCatAdapter = PaymentPromoCatAdapter(getCatList()!!, this, this)
        catList?.adapter = promoCatAdapter
        promoList?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        promoListAdapter = PaymentPromoListAdapter(promoCodeList, promoCodeList, this, this)
        promoList?.adapter = promoListAdapter

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        promoDialog?.window?.setLayout(width, height)
        promoDialog?.window?.setWindowAnimations(R.style.AppTheme_Slide)
        promoDialog?.show()
        setSearchFilter(searchTextView, clearBtn, this)
        clearBtn.setOnClickListener { searchTextView.setText("") }
    }

    private fun getCatList(): ArrayList<String>? {
        val list = ArrayList<String>()
        list.add("ALL")
        for (i in promoCodeList.indices) {
            if (!list.contains(promoCodeList[i].category)) list.add(promoCodeList[i].category)
        }
        return list
    }

    override fun onItemCatClick(cat: String, position: Int) {
        promoList!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        if (cat.equals("ALL", ignoreCase = true)) {
            promoListAdapter = PaymentPromoListAdapter(promoCodeList, promoCodeList, this, this)
            promoNewCodeList = promoCodeList
        } else {
            val list = ArrayList<PromoCodeList.Output>()
            for (i in promoCodeList.indices) {
                if (promoCodeList[i].category == (cat)) {
                    list.add(promoCodeList[i])
                }
            }
            promoNewCodeList = list
            promoListAdapter = PaymentPromoListAdapter(list, list, this, this)
        }
        promoList!!.adapter = promoListAdapter
        catList?.smoothScrollToPosition(position)
    }

    private fun setSearchFilter(
        searchPcTextView: EditText, clearBtn: ImageView, activity: Activity
    ) {
        searchPcTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int
            ) {
            }

            override fun afterTextChanged(cs: Editable) {
                if (cs.toString().isEmpty()) {
                    clearBtn.visibility = View.INVISIBLE
                } else {
                    clearBtn.visibility = View.VISIBLE
                }
                if (promoListAdapter != null) {
                    promoListAdapter?.filter?.filter(cs.toString())
                    promoList!!.visibility = View.VISIBLE
                }
                if (cs.toString().isEmpty()) {
                    promoListAdapter = PaymentPromoListAdapter(
                        promoNewCodeList, promoNewCodeList, activity, this@PaymentActivity
                    )
                    promoList!!.adapter = promoListAdapter
                }
            }
        })
    }

    override fun applyClick(data: PromoCodeList.Output) {

        showPromDialog(data)

    }

    private fun showPromDialog(data: PromoCodeList.Output) {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.promo_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val titleText = dialog.findViewById<View>(R.id.titleText) as TextView?
        val tncTxt = dialog.findViewById<View>(R.id.tncTxt) as TextView?
        val btnApplyCoupon = dialog.findViewById<View>(R.id.btnApplyCoupon) as TextView?

        tncTxt?.text = Html.fromHtml(data.tnc)
        titleText?.text = Html.fromHtml(data.title)
        if (data.promocode != null && data.promocode != "") {
            btnApplyCoupon?.show()
        }
        btnApplyCoupon?.setOnClickListener {
            binding?.promoCode?.setText(data.promocode)
            promoDialog!!.dismiss()
            authViewModel.promoCode(
                preferences.getUserId(),
                BOOKING_ID,
                TRANSACTION_ID,
                BOOK_TYPE,
                binding?.promoCode?.text.toString()
            )
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun callPromoData() {
        authViewModel.livePromoCodeScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.p != null) {
                            showTncDialog(
                                this, it.data.output.di, binding?.promoCode?.text.toString()
                            )
                            if (it.data.output.bin != null) {
                                val binSeries: String = it.data.output.bin
                                preferences.saveString(
                                    Constant.SharedPreference.Promo_Bin_Series, binSeries
                                )
                                preferences.saveBoolean(
                                    Constant.SharedPreference.Has_Bin_Series, true
                                )
                            } else preferences.saveBoolean(
                                Constant.SharedPreference.Has_Bin_Series, false
                            )
                            isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                println("callled this....")
                                binding?.cutPrice?.paintFlags =
                                    binding?.cutPrice?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG
                                binding?.applyCou?.show()
                                binding?.cutPrice?.show()
                                binding?.couponView?.hide()
                                DISCOUNT += it.data.output.di.toDouble()
                                binding?.promoCodetxt?.text =
                                    "'${binding?.promoCode?.text}' Applied"
                                binding?.applyco?.text =
                                    getString(R.string.currency) + it.data.output.di + " Saved"
                                binding?.removeoff?.setOnClickListener {
                                    authViewModel.removePromo(
                                        preferences.getString(Constant.SharedPreference.USER_NUMBER),
                                        BOOKING_ID,
                                        BOOK_TYPE
                                    )
                                }
                                actualAmt = (intent.getStringExtra("paidAmount")
                                    ?.toDouble()!! - DISCOUNT).toString()
                                binding?.textView178?.text =
                                    getString(R.string.currency) + actualAmt
                                binding?.cutPrice?.text =
                                    getString(R.string.currency) + intent.getStringExtra("paidAmount")
                                        .toString()
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

    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    //timerManage

    private fun timerManage() {
        startService(Intent(this, BroadcastService::class.java))
    }

    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            updateGUI(intent) // or whatever method used to update your GUI fields
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(br)
    }

    override fun onStop() {
        try {
            unregisterReceiver(br)
        } catch (e: java.lang.Exception) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        stopService(Intent(this, BroadcastService::class.java))
        super.onDestroy()
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun updateGUI(intent: Intent) {
        if (intent.extras != null) {
            val millisUntilFinished = intent.getLongExtra("countdown", 0)
            val second = millisUntilFinished / 1000 % 60
            val minutes = millisUntilFinished / (1000 * 60) % 60
            val display = java.lang.String.format("%02d:%02d", minutes, second)

            binding?.include47?.textView394?.text = display + " " + getString(R.string.minRemaining)

            if (millisUntilFinished.toInt() <= Constant.AVAILABETIME) {
                binding?.constraintLayout168?.show()
            } else {
                binding?.constraintLayout168?.hide()
            }

            binding?.include47?.textView395?.setOnClickListener {
                binding?.constraintLayout168?.hide()
                unregisterReceiver(br)
                authViewModel.extendTime(TRANSACTION_ID, BOOKING_ID, CINEMA_ID)
            }

        }
    }

    private fun extendTime() {
        authViewModel.extendTimeLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveExtendData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveExtendData(output: ExtendTimeResponse.Output) {
        //extandTime
        Constant.EXTANDTIME = Constant().convertTime(output.et)

        //AVAIL TIME
        Constant.AVAILABETIME = Constant().convertTime(output.at)

        timerManage()
    }


}