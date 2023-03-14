package com.net.pvr.ui.summery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySummeryBinding
import com.net.pvr.databinding.CarveryDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.CartModel
import com.net.pvr.ui.food.FoodActivity
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.payment.PaymentActivity
import com.net.pvr.ui.payment.cardDetails.CardDetailsActivity
import com.net.pvr.ui.payment.cred.CredActivity
import com.net.pvr.ui.payment.giftcardredeem.GiftCardRedeemActivity
import com.net.pvr.ui.payment.paytmpostpaid.PaytmPostPaidActivity
import com.net.pvr.ui.payment.promoCode.PromoCodeActivity
import com.net.pvr.ui.payment.response.PaytmHmacResponse
import com.net.pvr.ui.payment.response.UPIStatusResponse
import com.net.pvr.ui.payment.webView.PaytmWebActivity
import com.net.pvr.ui.seatLayout.adapter.AddFoodCartAdapter
import com.net.pvr.ui.summery.adapter.SeatListAdapter
import com.net.pvr.ui.summery.response.ExtendTimeResponse
import com.net.pvr.ui.summery.response.SummeryResponse
import com.net.pvr.ui.summery.viewModel.SummeryViewModel
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.BOOKING_ID
import com.net.pvr.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr.utils.Constant.Companion.CINEMA_ID
import com.net.pvr.utils.Constant.Companion.DONATION
import com.net.pvr.utils.Constant.Companion.FOODENABLE
import com.net.pvr.utils.Constant.Companion.SeatBack
import com.net.pvr.utils.Constant.Companion.TRANSACTION_ID
import com.net.pvr.utils.Constant.Companion.foodCartModel
import com.net.pvr.utils.ga.GoogleAnalytics
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.TransactionRequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SummeryActivity : AppCompatActivity(), AddFoodCartAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySummeryBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: SummeryViewModel by viewModels()
    private var cartModel: ArrayList<CartModel> = arrayListOf()
    private var itemDescription: String = ""
    private var pp: SummeryResponse.Output.PP? = null
    private var showTaxes = false
    private var paidAmount = 0.0
    private var upiLoader = false
    private var upiCount = 0
    private var payableAmount = 0.0
    private var ticketPrice = 0.0


    //Bottom Dialog
    private var dialog: BottomSheetDialog? = null

    private var addFoodCartAdapter: AddFoodCartAdapter? = null

    private var summeryResponse: SummeryResponse.Output? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummeryBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageLogin()
    }

    //manage Login
    @SuppressLint("SuspiciousIndentation")
    private fun manageLogin() {
        if (preferences.getIsLogin()) {
            if (intent.getStringExtra("from") == "summery") {
                cartModel = foodCartModel
                manageFunction()
            } else {
                try {
                    //if (intent.hasExtra("food"))
                    cartModel = intent.getSerializableExtra("food") as ArrayList<CartModel>
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                manageFunction()
            }
        } else {
            if (intent.hasExtra("food"))
                foodCartModel = intent.getSerializableExtra("food") as ArrayList<CartModel>
            val intent = Intent(this@SummeryActivity, LoginActivity::class.java)
            intent.putExtra("from", "summery")
            startActivity(intent)
            finish()
        }

        printLog("food--->${cartModel}")
    }

    private fun manageFunction() {
        Constant.viewModel = authViewModel
        if (Constant.BOOK_TYPE == "FOOD"){
            authViewModel.foodDetails(preferences.getUserId(), BOOKING_ID)
        }else {
            authViewModel.summery(
                TRANSACTION_ID, CINEMA_ID, preferences.getUserId(), BOOKING_ID, DONATION
            )
        }
        authViewModel.initJusPay(preferences.getUserId(), BOOKING_ID)

        movedNext()
        foodCart()
        summeryDetails()
        saveFood()
        setDonation()
        getShimmerData()
        extendTime()
        paytmHMAC()
        credCheck()
        upiStatus()
        phonePeHmac()
        phonePeStatus()

        if (BOOK_TYPE == "BOOKING")
        timerManage()

    }


    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    //CartFood
    private fun foodCart() {
        println("cartModel--->"+cartModel.size)
        if (cartModel.isNotEmpty()) {
            binding?.constraintLayout171?.show()
            binding?.recyclerView32?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            addFoodCartAdapter = AddFoodCartAdapter(cartModel, this, this)
            binding?.recyclerView32?.layoutManager = layoutManagerCrew
            binding?.recyclerView32?.adapter = addFoodCartAdapter
            binding?.recyclerView32?.setHasFixedSize(true)
        } else {
            binding?.constraintLayout171?.hide()
            binding?.recyclerView32?.hide()
        }
    }

    private fun movedNext() {
        binding?.quickPayBtn?.setOnClickListener {
            when (pp?.id?.uppercase(Locale.getDefault())) {
                Constant.UPI -> {
                    authViewModel.paytmHMAC(
                        preferences.getUserId(),
                        BOOKING_ID,
                        TRANSACTION_ID,
                        false,
                        "",
                        Constant.BOOK_TYPE,
                        pp?.name.toString(),
                        "no",
                        "NO"
                    )
                }
                Constant.CREDIT_CARD -> {
                    val intent = Intent(this@SummeryActivity, CardDetailsActivity::class.java)
                    intent.putExtra("pTypeId", pp?.id)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("tc", pp?.tc)
                    startActivity(intent)
                }
                Constant.CRED -> {
                    authViewModel.credCheck(
                        preferences.getUserId(),
                        BOOKING_ID,
                        Constant.BOOK_TYPE,
                        TRANSACTION_ID,
                        "false",
                        Constant.isPackageInstalled(packageManager).toString(),
                        "false"
                    )

                }
                Constant.AIRTEL -> {
                    val intent = Intent(this@SummeryActivity, PaytmWebActivity::class.java)
                    intent.putExtra("pTypeId", pp?.id)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("tc", pp?.tc)
                    startActivity(intent)
                }
                Constant.DEBIT_CARD -> {
                    val intent = Intent(this@SummeryActivity, CardDetailsActivity::class.java)
                    intent.putExtra("pTypeId", pp?.id)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("tc", pp?.tc)
                    startActivity(intent)
                }
                Constant.NET_BANKING -> {
                    val intent = Intent(this@SummeryActivity, CardDetailsActivity::class.java)
                    intent.putExtra("pTypeId", pp?.id)
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
                Constant.PHONE_PE -> {
                    authViewModel.phonepeHMAC(
                        preferences.getUserId(), BOOKING_ID, Constant.BOOK_TYPE, TRANSACTION_ID
                    )
                }
                Constant.PAYTMPOSTPAID -> {
                    val intent = Intent(this, PaytmPostPaidActivity::class.java)
                    intent.putExtra("type", "PP")
                    intent.putExtra("ca_a", "")
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
                Constant.PAYTM_WALLET -> {
                    val intent = Intent(this, PaytmPostPaidActivity::class.java)
                    intent.putExtra("type", "P")
                    intent.putExtra("ca_a", "")
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
                Constant.GYFTR -> {
                    val intent = Intent(this, PromoCodeActivity::class.java)
                    intent.putExtra("type", "GYFTR")
                    intent.putExtra("pid", pp?.id)
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("ca_a", "")
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
                Constant.GEIFT_CARD -> {
                    val intent = Intent(this, GiftCardRedeemActivity::class.java)
                    intent.putExtra("type", "GEIFT_CARD")
                    intent.putExtra("pid", pp?.id)
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("c", pp?.c)
                    intent.putExtra("ca_a", "")
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
                Constant.ZAGGLE -> {
                    val intent = Intent(this, GiftCardRedeemActivity::class.java)
                    intent.putExtra("type", "ZAGGLE")
                    intent.putExtra("pid", pp?.id)
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("c", pp?.c)
                    intent.putExtra("ca_a", "")
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
                Constant.ACCENTIVE -> {
                    val intent = Intent(this, PromoCodeActivity::class.java)
                    intent.putExtra("type", "ACCENTIVE")
                    intent.putExtra("pid", pp?.id)
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("ca_a", "")
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount.toString())
                    startActivity(intent)
                }
            }

        }
        binding?.include7?.textView108?.text = getString(R.string.checkout)
        binding?.include7?.imageView58?.setOnClickListener {
            onBackPressed()
        }
    }

    //saveFood
    private fun saveFood() {
        authViewModel.seatWithFoodDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        authViewModel.setDonation(
                            BOOKING_ID,
                            TRANSACTION_ID,
                            isDonate = true,
                            istDonate = false,
                            isSpi = "NO"
                        )

                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {

                            },
                            negativeClick = {

                            })
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

    //Set Donation
    private fun setDonation() {
        authViewModel.setDonationDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
//                        println("paidAmount--->$paidAmount")
                        // Hit Event
                        try {
                            val bundle = Bundle()
                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Book ticket")
//                bundle.putString("var_login_city", cityNameMAin)
                            GoogleAnalytics.hitEvent(this, "FnB_add_checkout", bundle)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }


                        val intent = Intent(this@SummeryActivity, PaymentActivity::class.java)
                        intent.putExtra("paidAmount", paidAmount.toString())
                        startActivity(intent)

                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {

                            },
                            negativeClick = {

                            })
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

    //SummeryDetails
    private fun summeryDetails() {
        authViewModel.liveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: SummeryResponse.Output) {
        summeryResponse = output

        if (BOOK_TYPE == "FOOD"){
            binding?.cardView7?.hide()
            binding?.constraintLayout40?.hide()
            binding?.view211?.hide()
            binding?.textView117?.hide()
            binding?.textView119?.hide()
            binding?.textView120?.hide()
            binding?.constraintLayout156?.hide()
            binding?.constraintLayout170?.hide()
            binding?.textView164?.hide()
            binding?.taxes?.hide()
            binding?.line?.hide()
            binding?.imageView150?.hide()
            binding?.imageView77?.hide()
            binding?.textView111?.hide()
            binding?.constraintLayout115?.hide()
            ticketPrice = output.a.toDouble()
            //shows
            binding?.textView112?.text = output.t

        }else{
            binding?.textView111?.show()
            binding?.cardView7?.show()
            binding?.imageView77?.show()
            binding?.constraintLayout40?.show()
            binding?.view211?.show()
            binding?.textView117?.show()
            binding?.textView119?.show()
            binding?.textView120?.show()
            binding?.constraintLayout156?.show()
            binding?.constraintLayout170?.show()
            binding?.textView164?.show()
            binding?.taxes?.show()
            binding?.line?.show()
            binding?.imageView150?.show()
            binding?.constraintLayout115?.show()
            if (!TextUtils.isEmpty(output.bnd) && output.bnd.toDouble() > 0) {
                if (output.cv){
                    binding?.fnbVocherView?.show()
                    binding?.cutPrice?.show()
                    binding?.cutPriceSub?.show()
                    binding?.cutPriceSub?.paintFlags = binding?.cutPriceSub?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                    binding?.cutPrice?.paintFlags = binding?.cutPrice?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
                    binding?.cutFoodPrice?.paintFlags = binding?.cutFoodPrice?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG

                }else{
                    binding?.fnbVocherView?.hide()
                    binding?.cutPrice?.hide()
                    binding?.cutPriceSub?.hide()
                }
            }
            //ticket
            binding?.textView119?.text = output.f[0].it[0].n
            //price
            binding?.textView120?.text = getString(R.string.currency) + output.f[0].v
            binding?.cutPrice?.text = getString(R.string.currency) + output.f[0].cp
            ticketPrice = output.f[0].v.toDouble()
            //subtotal
//        binding?.textView168?.text =  getString(R.string.currency)+output.a

            //movie Details
            binding?.textView111?.text =
                output.cen + getString(R.string.dots) + output.lg + getString(R.string.dots) + output.fmt

            //audi
            binding?.textView115?.text = output.audi + "-" + output.st
            //shows
            binding?.textView112?.text = output.md + ", " + output.t

            //manageFood
            if (FOODENABLE == 1) {
                if (!output.cv) {
                    binding?.constraintLayout157?.hide()
                    binding?.constraintLayout156?.hide()
                }else{
                    binding?.constraintLayout157?.show()
                    binding?.constraintLayout156?.hide()
                }
            } else {
                binding?.constraintLayout157?.show()
            }
        }


// quickPay
        if (output.pp != null) {
            pp = output.pp
            binding?.quickPayBtn?.show()
        }

        //shimmer
        binding?.constraintLayout145?.hide()
        //design
        binding?.constraintLayout155?.show()

        //privilege point
        if (output.pe!=null && output.pe != "") {
            binding?.textView313?.text = "You will earn  " + output.pe + " Privilege Points."
        }else{
            binding?.textView313?.text = "You will earn  " + 0 + " Privilege Points."
        }







        //coupon
        binding?.textView169?.text = ""


        for (data in output.f) {
            if (data.n.contains("Taxes & Fees")) {

                //taxes fee
                binding?.textView170?.text = getString(R.string.currency) + data.v

                //Convenience fee
                binding?.textView272?.text = data.it[0].v

                //Convenience Heading
                binding?.textView270?.text = data.it[0].n

                //gst
                binding?.textView273?.text = data.it[1].v

                //gst Heading
                binding?.textView271?.text = data.it[1].n
            }else if (data.n.contains("Food vouchers")){
                println("data.n--->"+data.n)
                binding?.constraintLayout171?.show()
                binding?.fnbVocherView?.show()
                binding?.recyclerView32?.hide()
                binding?.cutFoodPrice?.text = getString(R.string.currency) + data.cp
                binding?.foodPrice?.text = getString(R.string.currency) + data.v
            }
            else if (data.n.contains("Total (Tickets + Food voucher)")){
                println("data.n--->"+data.n)
                binding?.constraintLayout171?.show()
                binding?.fnbVocherView?.show()
                binding?.recyclerView32?.hide()
                binding?.textView162?.text = data.n
                binding?.textView168?.text = getString(R.string.currency) + data.v
                binding?.cutPriceSub?.text = getString(R.string.currency) + data.cp
            }

        }

        //caver calling
        binding?.textView171?.text = ""


        //payedAmount
        payableAmount = output.a.toDouble()

        //Image
        Glide.with(this).load(output.im).error(R.drawable.placeholder_vertical)
            .into(binding?.imageView59!!)

        //title
        binding?.textView110?.text = output.m

        //location
        binding?.textView113?.text = output.c
        binding?.imageView60?.setOnClickListener {
            Constant().openMap(this, preferences.getLatitudeData(), preferences.getLongitudeData())
        }

        //audi
        binding?.textView115?.text = output.audi + "-" + output.st

        //seat
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.alignItems = AlignItems.STRETCH
        val adapter = SeatListAdapter(output.seat)
        binding?.recyclerView31?.setHasFixedSize(true)
        binding?.recyclerView31?.layoutManager = layoutManager
        binding?.recyclerView31?.adapter = adapter

        //taxes UnderLine
        binding?.textView164?.paintFlags =
            binding?.textView164!!.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //Show taxes
        binding?.textView164?.setOnClickListener {
            binding?.taxes?.show()
        }

        //manage taxes Shows
        binding?.imageView77?.setOnClickListener {
            showTaxes = if (!showTaxes) {
                binding?.imageView77?.setImageResource(R.drawable.arrow_down)
                binding?.taxes?.hide()
                true
            } else {
                binding?.imageView77?.setImageResource(R.drawable.arrow_up)
                binding?.taxes?.show()
                false
            }
        }


        //seatWithFood
        binding?.textView175?.setOnClickListener {
            cartModel.forEachIndexed { index, food ->
                itemDescription = if (index == 0) {
                    food.title + "|" + food.id + "|" + food.quantity + "|" + food.price + "|" + food.ho + "|" + food.mid
                } else {
                    itemDescription + "#" + food.title + "|" + food.id + "|" + food.quantity + "|" + food.price + "|" + food.ho + "|" + food.mid
                }
            }

            if (cartModel.isNotEmpty() && BOOK_TYPE != "FOOD") {
                authViewModel.seatWithFood(
                    itemDescription,
                    TRANSACTION_ID,
                    CINEMA_ID,
                    preferences.getUserId(),
                    "",
                    "",
                    "no",
                    output.seats,
                    output.audi
                )
            } else {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Book ticket")
//                bundle.putString("var_login_city", cityNameMAin)
                    GoogleAnalytics.hitEvent(this, "FnB_add_checkout", bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }

                val intent = Intent(this@SummeryActivity, PaymentActivity::class.java)
                intent.putExtra("paidAmount", output.a.toString())
                startActivity(intent)
            }
        }



        //Add More Food
        binding?.textView312?.setOnClickListener {

// Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Book ticket")
//                bundle.putString("var_login_city", cityNameMAin)
                GoogleAnalytics.hitEvent(this, "FndB_add_snacks", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            Constant.SUMMERYBACK = 1
            foodCartModel = cartModel
            finish()
        }

        //Donation manage
        if (output.donation == "True") {
            binding?.constraintLayout170?.show()
            setDonationData(output)

            //add
            binding?.textView400?.setOnClickListener {
                setDonationData(output)
            }

            //remove
            binding?.textView172?.setOnClickListener {
                removeDonationData(output)
            }

            //Carvery Dialog
            binding?.imageView147?.setOnClickListener {
                carveryDialog(output.don_stext)
            }

        } else {
            setPrice(output)
            binding?.constraintLayout170?.hide()
        }
    }
    private fun setDonationData(output: SummeryResponse.Output) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Summery")
            bundle.putString("var_add_donation", "")
            GoogleAnalytics.hitEvent(this, "book_add_donation", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

        //add
        binding?.textView400?.hide()

        //remove
        binding?.textView172?.show()

        binding?.textView165?.text = output.don_text


        //total seat
        val donationAmount = output.don / 100
        val ticketCount = donationAmount * output.seat.size

        binding?.textView171?.text = getString(R.string.currency) + " " + ticketCount.toString()
//        binding?.textView166?.text = donationAmount.toString() + "/" + getString(R.string.ticket)


//    payableAmount
        if (cartModel.isNotEmpty()) {
            val foodPrice = calculateTotalPrice()
            val foodTotPrice = Constant.DECIFORMAT.format(foodPrice / 100.0)
            binding?.textView168?.text = getString(R.string.currency)+Constant.DECIFORMAT.format(ticketPrice+(foodPrice/100.0))

            paidAmount =
                (payableAmount + ticketCount.toDouble() + foodTotPrice.toDouble())
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount) + " |"

            //grand total
            binding?.textView173?.text = getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount)
        } else {
            if (!output.cv) {
                binding?.textView168?.text =
                    getString(R.string.currency) + Constant.DECIFORMAT.format(ticketPrice)
                binding?.cutPriceSub?.text = getString(R.string.currency) + output.f[0].cp
            }

            paidAmount = (payableAmount + ticketCount)
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount) + " |"

//grand total
            binding?.textView173?.text = getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount)
        }
    }

    private fun removeDonationData(output: SummeryResponse.Output) {

// Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Summery")
            bundle.putString("var_book_remove_donation", "")
            GoogleAnalytics.hitEvent(this, "book_remove_donation", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

        //add
        binding?.textView400?.show()
        //remove
        binding?.textView172?.hide()

        binding?.textView165?.text = output.don_text

        //total seat
        val donationAmount = 0
        val ticketCount = output.seat.size

        binding?.textView171?.text = getString(R.string.currency) + " 0"
//        binding?.textView166?.text = ticketCount.toString() + "/" + getString(R.string.ticket)

//    payableAmount
        if (cartModel.isNotEmpty()) {
            val foodPrice = calculateTotalPrice()
            val foodTotPrice = Constant.DECIFORMAT.format(foodPrice / 100.0)
            binding?.textView168?.text = getString(R.string.currency)+Constant.DECIFORMAT.format(ticketPrice+(foodPrice/100.0))

            paidAmount =
                (payableAmount + donationAmount.toDouble() + foodTotPrice.toDouble())
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount) + " |"

            //grand total
            binding?.textView173?.text = getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount)

        } else {
            if (!output.cv) {
                binding?.textView168?.text =
                    getString(R.string.currency) + Constant.DECIFORMAT.format(ticketPrice)
                binding?.cutPriceSub?.text = getString(R.string.currency) + output.f[0].cp
            }


            paidAmount = (payableAmount + donationAmount)
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount) + " |"

            //grand total
            binding?.textView173?.text = getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setPrice(output: SummeryResponse.Output) {
        //total seat
//        val donationAmount = output.don / 100

        val donationAmount = 0 / 100
        val ticketCount = donationAmount * output.seat.size

        //    payableAmount
        if (cartModel.isNotEmpty()) {
            val foodPrice = calculateTotalPrice()
            val foodTotPrice = Constant.DECIFORMAT.format(foodPrice / 100.0)

            binding?.textView168?.text = getString(R.string.currency)+Constant.DECIFORMAT.format(ticketPrice+(foodPrice/100.0))

            paidAmount =
                (payableAmount + ticketCount.toDouble() + foodTotPrice.toDouble())
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount) + " |"

            //grand total
            binding?.textView173?.text = getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount)
        } else {
            if (!output.cv) {
                binding?.textView168?.text =
                    getString(R.string.currency) + Constant.DECIFORMAT.format(ticketPrice)
                binding?.cutPriceSub?.text = getString(R.string.currency) + output.f[0].cp
            }


            paidAmount = (payableAmount + ticketCount).toDouble()
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount) + " |"

//grand total
            binding?.textView173?.text = getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount)
        }
    }

    private fun calculateTotalPrice(): Int {
        var totalPrice = 0
        for (data in cartModel) {
            totalPrice += data.price * data.quantity
        }
        return totalPrice
    }

    private fun carveryDialog(donText: String) {
        dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val bindingCarvery = CarveryDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout>? = dialog?.behavior

        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.setContentView(bindingCarvery.root)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.show()

        bindingCarvery.textView154.text = Html.fromHtml(donText, Html.FROM_HTML_MODE_LEGACY)
        bindingCarvery.textView154.movementMethod = LinkMovementMethod.getInstance();
        bindingCarvery.textView154.isClickable = true
        bindingCarvery.textView310.paintFlags =
            bindingCarvery.textView310.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //click privacy
        bindingCarvery.textView310.setOnClickListener {
            dialog?.dismiss()
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("from", "CheckOut")
            intent.putExtra("title", "Donation Terms & Condition")
            intent.putExtra("getUrl", Constant.donation)
            startActivity(intent)
        }

        bindingCarvery.include10.textView5.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun increaseFoodClick(comingSoonItem: CartModel) {
        var num = comingSoonItem.quantity
        if (FoodActivity.itemCount >= FoodActivity.limitCount) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
               FoodActivity.seatMessage,
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num += 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
        }
    }

    override fun decreaseFoodClick(comingSoonItem: CartModel) {
        var num = comingSoonItem.quantity
        num -= 1
        comingSoonItem.quantity = num
        updateCartFoodCartList(comingSoonItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCartFoodCartList(recyclerData: CartModel) {
        addFoodCartAdapter?.notifyDataSetChanged()
        if (summeryResponse?.donation == "True") {
            binding?.constraintLayout170?.show()
            setDonationData(summeryResponse!!)
        } else {
            summeryResponse?.let { setPrice(it) }
        }

        try {
            for (item in cartModel) {
                if (item.id == recyclerData.id) {
                    if (recyclerData.quantity == 0) {
                        removeCartItem(item)
                    } else {
                        item.quantity = recyclerData.quantity
                        break
                    }
                }
            }
//            foodCartModel = cartModel
        } catch (e: Exception) {
            e.printStackTrace()
        }

        FoodActivity.itemCount = FoodActivity.getItemCount(cartModel)
    }

    private fun removeCartItem(item: CartModel) {
        for (data in cartModel) {
            if (data.id == item.id) {
                cartModel.remove(data)
            }
        }
    }

    override fun onBackPressed() {
        SeatBack= 1
        if (FOODENABLE == 0) {
            showDialog("1")
        } else {
            showDialog("0")
        }
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

    override fun onResume() {
        super.onResume()
        if (BOOK_TYPE == "BOOKING")
        registerReceiver(br, IntentFilter(BroadcastService.COUNTDOWN_BR))
    }

    override fun onPause() {
        super.onPause()
        if (BOOK_TYPE == "BOOKING")
        unregisterReceiver(br)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onStop() {
        try {
            if (BOOK_TYPE == "BOOKING")
            unregisterReceiver(br)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (BOOK_TYPE == "BOOKING")
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

            binding?.include48?.textView394?.text = display + " " + getString(R.string.minRemaining)

            if (millisUntilFinished.toInt() <= Constant.AVAILABETIME) {
                binding?.constrainLayout169?.show()
            } else {
                binding?.constrainLayout169?.hide()
            }

            binding?.include48?.textView395?.setOnClickListener {
                binding?.constrainLayout169?.hide()
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
        if (BOOK_TYPE == "BOOKING")
        timerManage()
    }

    private fun paytmHMAC() {
        authViewModel.livePDataScope.observe(this) {
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
            Constant().printTicket(this@SummeryActivity)
        } else if (output.p.equals("PENDING", ignoreCase = true)) {
            if (upiCount <= 6) {
                val handler = Handler()
                upiCount += 1
                handler.postDelayed({ // close your dialog
                    authViewModel.upiStatus(
                        BOOKING_ID, Constant.BOOK_TYPE
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

    private fun credCheck() {
        authViewModel.credCheckLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (it.data.output.state == "true") {
                            val intent = Intent(this@SummeryActivity, CredActivity::class.java)
                            intent.putExtra("bannertext", it.data.output.banner_txt)
                            intent.putExtra("icon", it.data.output.icon)
                            intent.putExtra("paidAmount", paidAmount.toString())
                            intent.putExtra("msg", it.data.output.msg)
                            intent.putExtra("mode", it.data.output.mode)
                            intent.putExtra("pid", pp?.id)
                            intent.putExtra("tc", pp?.tc)
                            intent.putExtra("ca_a", "")
                            intent.putExtra("ca_t", "")
                            intent.putExtra("title", pp?.name)
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
                            Constant().printTicket(this@SummeryActivity)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            if (data != null && data.extras != null) {
                if (Constant.BOOK_TYPE == "LOYALTYUNLIMITED") {
                    toast("LOYALTYUNLIMITED")
                } else {
                    authViewModel.upiStatus(
                        BOOKING_ID, Constant.BOOK_TYPE
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
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    authViewModel.phonepeStatus(
                        preferences.getUserId(), BOOKING_ID, Constant.BOOK_TYPE, TRANSACTION_ID
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

    private fun showDialog(type:String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.cancel_summery_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val crossBtn = dialog.findViewById<View>(R.id.crossBtn) as ImageView
        val endSession = dialog.findViewById<View>(R.id.delete) as TextView
        val exploreMenu = dialog.findViewById<View>(R.id.cancel) as TextView
        val or = dialog.findViewById<View>(R.id.or) as TextView

        if (type == "0"){
            exploreMenu.hide()
            or.hide()
        }else{
            exploreMenu.show()
            or.show()
        }

        //close button
        crossBtn.setOnClickListener {
            dialog.dismiss()
        }

        exploreMenu.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                bundle.putString("var_FnB_food_type","veg")
                GoogleAnalytics.hitEvent(this, "FnB_explore_menu", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }
            dialog.dismiss()
            Constant.SUMMERYBACK = 1
            foodCartModel = cartModel
            finish()
        }

        endSession.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                bundle.putString("var_FnB_food_type","veg")
                GoogleAnalytics.hitEvent(this, "FnB_end_the_session", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            authViewModel.cancelTrans(
                CINEMA_ID, TRANSACTION_ID, BOOKING_ID
            )

            dialog.dismiss()
            val intent = Intent(this@SummeryActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

}