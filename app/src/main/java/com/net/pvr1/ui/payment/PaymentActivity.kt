package com.net.pvr1.ui.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPaymentBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.payment.adapter.CouponAdapter
import com.net.pvr1.ui.payment.adapter.PaymentAdapter
import com.net.pvr1.ui.payment.adapter.PaymentExclusiveAdapter
import com.net.pvr1.ui.payment.cardDetails.CardDetailsActivity
import com.net.pvr1.ui.payment.mCoupon.MCouponActivity
import com.net.pvr1.ui.payment.promoCode.PromoCodeActivity
import com.net.pvr1.ui.payment.response.CouponResponse
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.payment.starPass.StarPassActivity
import com.net.pvr1.ui.payment.viewModel.PaymentViewModel
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

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
    private var catFilterPayment = ArrayList<PaymentResponse.Output.Gateway>()
    private var paidAmount = ""

    private val UPI_PAYMENT = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include26?.textView108?.text = getString(R.string.payment)

        paidAmount=intent.getStringExtra("paidAmount").toString()
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
        upiStatus()
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: PaymentResponse.Output) {
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
    }

    override fun paymentClick(comingSoonItem: PaymentResponse.Output.Gateway) {
        if (comingSoonItem.name=="UPI"){
            authViewModel.paytmHMAC(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                false,
                "",
                Constant.BOOK_TYPE,
                comingSoonItem.name,
                "no",
                "NO"
            )
//            payUsingUpi("","","","")
        }else{
        val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
        intent.putExtra("pTypeId", comingSoonItem.id)
        intent.putExtra("paidAmount", paidAmount)
        startActivity(intent)
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun upiStatus() {
        authViewModel.upiStatusResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

//                        retrieveDataUpi(it.data.output)
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }


    private fun retrieveDataUpi(output: PaytmHmacResponse.Output) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(output.deepLink)
        startActivityForResult(intent, 120)
        startActivity(intent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            if (data != null && data.extras != null) {

//                getUPICustomCall(
//                    context,
//                    com.net.pvr.ui.otherpaymentoptions.PCOtherPaymentOptionsActivity.bookType,
//                    com.net.pvr.ui.otherpaymentoptions.PCOtherPaymentOptionsActivity.paymentIntentData,
//                    paymentType
//                )
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.pleasewait),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
                if (BOOK_TYPE=="LOYALTYUNLIMITED" ){
                    toast("LOYALTYUNLIMITED")
                }
                else {
                    authViewModel.upiStatus(
                        BOOK_TYPE,
                        BOOKING_ID
                    )
                }

            }
        }
    }


    override fun paymentExclusiveClick(comingSoonItem: PaymentResponse.Output.Offer) {
        when (comingSoonItem.name) {
            "Star Pass" -> {
                val intent = Intent(this@PaymentActivity, StarPassActivity::class.java)
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
            "M-Coupon" -> {
                val intent = Intent(this@PaymentActivity, MCouponActivity::class.java)
                intent.putExtra("paidAmount", paidAmount)
                startActivity(intent)
            }
            "Promocode" -> {
                val intent = Intent(this@PaymentActivity, PromoCodeActivity::class.java)
                intent.putExtra("paidAmount",paidAmount)
                startActivity(intent)
            }
            "Hyatt Dining Club" -> {
                val intent = Intent(this@PaymentActivity, CardDetailsActivity::class.java)
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


}