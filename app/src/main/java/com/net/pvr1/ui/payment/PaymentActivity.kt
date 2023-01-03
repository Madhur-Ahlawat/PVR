package com.net.pvr1.ui.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPaymentBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.payment.adapter.CouponAdapter
import com.net.pvr1.ui.payment.adapter.PaymentAdapter
import com.net.pvr1.ui.payment.adapter.PaymentExclusiveAdapter
import com.net.pvr1.ui.payment.bankoffers.BankOffersActivity
import com.net.pvr1.ui.payment.cardDetails.CardDetailsActivity
import com.net.pvr1.ui.payment.cred.CredActivity
import com.net.pvr1.ui.payment.giftcardredeem.GiftCardRedeemActivity
import com.net.pvr1.ui.payment.mCoupon.MCouponActivity
import com.net.pvr1.ui.payment.paytmpostpaid.PaytmPostPaidActivity
import com.net.pvr1.ui.payment.promoCode.PromoCodeActivity
import com.net.pvr1.ui.payment.response.CouponResponse
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.payment.response.UPIStatusResponse
import com.net.pvr1.ui.payment.viewModel.PaymentViewModel
import com.net.pvr1.ui.payment.webView.PaytmWebActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.ACCENTIVE
import com.net.pvr1.utils.Constant.Companion.AIRTEL
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.CRED
import com.net.pvr1.utils.Constant.Companion.CREDIT_CARD
import com.net.pvr1.utils.Constant.Companion.DEBIT_CARD
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
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.TransactionRequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentAdapter.RecycleViewItemClickListenerCity,
    PaymentExclusiveAdapter.RecycleViewItemClickListenerCity,
    CouponAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPaymentBinding? = null
    private val authViewModel: PaymentViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var paymentItemHold: PaymentResponse.Output.Gateway? = null
    private var catFilterPayment = ArrayList<PaymentResponse.Output.Gateway>()
    private var paidAmount = ""

    private val UPI_PAYMENT = 0
    private var upi_count = 0
    private var upi_loader = false

    companion object {
        var isPromocodeApplied = false
        var offerList:ArrayList<PaymentResponse.Output.Binoffer> = ArrayList()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include26?.textView108?.text = getString(R.string.payment)

        paidAmount = intent.getStringExtra("paidAmount").toString()
        //paidAmount
        binding?.textView178?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")
        //voucher
//        authViewModel.voucher(
//            preferences.getToken().toString(),
//            preferences.getUserId().toString(),
//            CITY,
//            "",
//            "false",
//            Constant().getDeviceId(this),
//            ""
//        )

//        //payMode
        authViewModel.payMode(
            CINEMA_ID,
            "BOOKING",
            preferences.getUserId().toString(),
            preferences.geMobileNumber().toString(),
            "",
            "no",
            "",
            false
        )


//        authViewModel.coupons(
//            preferences.geMobileNumber().toString(),
//            CITY,
//            "",
//            false,
//            Constant().getDeviceId(this)
//        )
//
//        //payMode
//        authViewModel.payMode(
//            "GURM",
//            "BOOKING",
//            "pGnnlj1MEjb0MOKBx1EH5w==",
//            "7800049994",
//            "",
//            "no",
//            "",
//            false
//        )

//        voucherDataLoad()
        payModeDataLoad()
        paytmHMAC()
        credCheck()
        upiStatus()
        phonePeHmac()
        phonePeStatus()
        binding?.include26?.imageView58?.setOnClickListener {
            onBackPressed()
        }
    }


//    private fun voucherDataLoad() {
//        authViewModel.userResponseLiveData.observe(this) {
//            when (it) {
//                is NetworkResult.Success -> {
//                    loader?.dismiss()
//                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
//                        retrieveDataCoupon(it.data.output)
//                    } else {
//                        val dialog = OptionDialog(this,
//                            R.mipmap.ic_launcher,
//                            R.string.app_name,
//                            it.data?.msg.toString(),
//                            positiveBtnText = R.string.ok,
//                            negativeBtnText = R.string.no,
//                            positiveClick = {
//                            },
//                            negativeClick = {
//                            })
//                        dialog.show()
//                    }
//                }
//                is NetworkResult.Error -> {
//                    loader?.dismiss()
//                    val dialog = OptionDialog(this,
//                        R.mipmap.ic_launcher,
//                        R.string.app_name,
//                        it.message.toString(),
//                        positiveBtnText = R.string.ok,
//                        negativeBtnText = R.string.no,
//                        positiveClick = {
//                        },
//                        negativeClick = {
//                        })
//                    dialog.show()
//                }
//                is NetworkResult.Loading -> {
//                    loader = LoaderDialog(R.string.pleasewait)
//                    loader?.show(supportFragmentManager, null)
//                }
//            }
//        }
//    }

    private fun retrieveDataCoupon(output: ArrayList<CouponResponse.Output>) {
        if (output.isNotEmpty()) {
            binding?.recyclerView46?.show()
            binding?.textView180?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val foodBestSellerAdapter = CouponAdapter(output, this, this)
            binding?.recyclerView46?.layoutManager = layoutManagerCrew
            binding?.recyclerView46?.adapter = foodBestSellerAdapter
            binding?.recyclerView46?.setHasFixedSize(true)
        } else {
            binding?.textView180?.hide()
            binding?.recyclerView46?.hide()
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
        //Bank Offer
        if (output.binoffers.isNotEmpty()) {
            binding?.bankOffers?.show()
            offerList = output.binoffers
        } else {
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
        if (output.gateway.isNotEmpty()) {
            binding?.recyclerView44?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = PaymentAdapter(payMethodFilter("GATEWAY"), this, this)
            binding?.recyclerView44?.layoutManager = layoutManagerCrew
            binding?.recyclerView44?.adapter = foodBestSellerAdapter
            binding?.recyclerView44?.setHasFixedSize(true)
        } else {
            binding?.recyclerView44?.hide()
        }

        //Wallets
        if (output.gateway.isNotEmpty()) {
            binding?.recyclerView43?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = PaymentAdapter(payMethodFilter("WALLET"), this, this)
            binding?.recyclerView43?.layoutManager = layoutManagerCrew
            binding?.recyclerView43?.adapter = foodBestSellerAdapter
            binding?.recyclerView43?.setHasFixedSize(true)
        } else {
            binding?.recyclerView43?.hide()
        }

        // Offer
        if (output.offers.isNotEmpty()) {
            binding?.recyclerView45?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val paymentExclusiveAdapter = PaymentExclusiveAdapter(output.offers, this, this)
            binding?.recyclerView45?.layoutManager = layoutManagerCrew
            binding?.recyclerView45?.adapter = paymentExclusiveAdapter
            binding?.recyclerView45?.setHasFixedSize(true)
        } else {
            binding?.recyclerView45?.hide()
        }

        binding?.bankOffers?.setOnClickListener {
            val intent = Intent(this@PaymentActivity, BankOffersActivity::class.java)
            intent.putExtra("paidAmount", paidAmount)
            startActivity(intent)
        }
    }

    //Payment Option Clicks
    override fun paymentClick(paymentItem: PaymentResponse.Output.Gateway) {

        when (paymentItem.id.toUpperCase()) {
            UPI -> {
                authViewModel.paytmHMAC(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.TRANSACTION_ID,
                    false,
                    "",
                    Constant.BOOK_TYPE,
                    paymentItem.name,
                    "no",
                    "NO"
                )
            }
            CREDIT_CARD -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("paidAmount", paidAmount)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("tc", paymentItem.tc)
                startActivity(intent)
            }
            CRED -> {
                paymentItemHold = paymentItem
                authViewModel.credCheck(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.BOOK_TYPE,
                    Constant.TRANSACTION_ID,
                    "false",
                    Constant.isPackageInstalled(packageManager).toString(),
                    "false"
                )

            }
            AIRTEL -> {
                val intent = Intent(this@PaymentActivity, PaytmWebActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("paidAmount", paidAmount)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("tc", paymentItem.tc)
                startActivity(intent)
            }
            DEBIT_CARD -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("paidAmount", paidAmount)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("tc", paymentItem.tc)
                startActivity(intent)
            }
            NET_BANKING -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
                intent.putExtra("pTypeId", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
            PHONE_PE -> {
                authViewModel.phonepeHMAC(
                    preferences.getUserId(),
                    BOOKING_ID,
                    BOOK_TYPE,
                    TRANSACTION_ID
                )
            }
            PAYTMPOSTPAID -> {
                val intent = Intent(this, PaytmPostPaidActivity::class.java)
                intent.putExtra("type", "PP")
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
            PAYTM_WALLET -> {
                val intent = Intent(this, PaytmPostPaidActivity::class.java)
                intent.putExtra("type", "P")
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", paidAmount)
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
                intent.putExtra("paidAmount", paidAmount)
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
                intent.putExtra("paidAmount", paidAmount)
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
                intent.putExtra("paidAmount", paidAmount)
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
                intent.putExtra("paidAmount", paidAmount)
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
                        if (it.data.output.state == "true"){
                            val intent = Intent(this@PaymentActivity, CredActivity::class.java)
                            intent.putExtra("bannertext",it.data.output.banner_txt)
                            intent.putExtra("icon", it.data.output.icon)
                            intent.putExtra("paidAmount", paidAmount)
                            intent.putExtra("msg", it.data.output.msg)
                            intent.putExtra("mode",it.data.output.mode)
                            intent.putExtra("pid", paymentItemHold?.id)
                            intent.putExtra("tc", paymentItemHold?.tc)
                            intent.putExtra("ca_a", paymentItemHold?.ca_a)
                            intent.putExtra("ca_t", paymentItemHold?.ca_t)
                            intent.putExtra("title", paymentItemHold?.name)
                            startActivity(intent)
                        }else{
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
                    if (!upi_loader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
                }
            }
        }

    }

    private fun retrieveStatusUpi(output: UPIStatusResponse.Output) {
        upi_loader = true;
        if (output.p.equals("PAID", ignoreCase = true)) {
            loader?.dismiss()
            Constant().printTicket(this@PaymentActivity)
        } else if (output.p.equals("PENDING", ignoreCase = true)) {
            if (upi_count <= 6) {
                val handler = Handler()
                upi_count += 1
                handler.postDelayed({ // close your dialog
                    authViewModel.upiStatus(
                        BOOKING_ID,
                        BOOK_TYPE
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            if (data != null && data.extras != null) {
                if (BOOK_TYPE == "LOYALTYUNLIMITED") {
                    toast("LOYALTYUNLIMITED")
                } else {
                    authViewModel.upiStatus(
                        BOOKING_ID,
                        BOOK_TYPE
                    )
                }

            } else {
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
        } else if (resultCode == 300) {
            var bundle: Bundle
            val txnResult: String? = null
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    authViewModel.phonepeStatus(
                        preferences.getUserId(),
                        BOOKING_ID,
                        BOOK_TYPE,
                        TRANSACTION_ID
                    )
                } else if (resultCode == RESULT_CANCELED) {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Transaction Failed!",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {

                        },
                        negativeClick = {})
                    dialog.show()
                }
            }
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
                intent.putExtra("paidAmount", paidAmount)

                //intent.putExtra(PCConstants.BI_PT,bi_pt);
                try {
                    if (paymentItem?.promomap != null) {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList(
                            "Promomaplist",
                            paymentItem.promomap as ArrayList<out Parcelable?>
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
                intent.putExtra("paidAmount", paidAmount)
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
                intent.putExtra("paidAmount", paidAmount)
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
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
            STAR_PASS -> {
                val intent = Intent(this@PaymentActivity, MCouponActivity::class.java)
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
            M_COUPON -> {
                val intent = Intent(this@PaymentActivity, MCouponActivity::class.java)
                intent.putExtra("pid", paymentItem.id)
                intent.putExtra("tc", paymentItem.tc)
                intent.putExtra("ca_a", paymentItem.ca_a)
                intent.putExtra("ca_t", paymentItem.ca_t)
                intent.putExtra("title", paymentItem.name)
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
        }


    }

    override fun couponClick(comingSoonItem: CartModel, position: Int) {
//        val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
////        intent.putExtra("ptype",comingSoonItem.id.toString())
//        startActivity(intent)
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
                        val transactionRequest2 = TransactionRequestBuilder()
                            .setData(it.data.output.bs)
                            .setChecksum(it.data.output.bs)
                            .setUrl(it.data.output.bs)
                            .build()

                        startActivityForResult(
                            PhonePe.getTransactionIntent(
                                transactionRequest2
                            ), 300
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
                    if (!upi_loader) {
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
                                it.data?.msg.toString(),
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
                    if (!upi_loader) {
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
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
                launchActivity(
                    HomeActivity::class.java,
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            },
            negativeClick = {
            })
        dialog.show()
    }


}