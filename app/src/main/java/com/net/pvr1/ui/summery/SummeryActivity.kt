package com.net.pvr1.ui.summery

import android.annotation.SuppressLint
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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
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
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySummeryBinding
import com.net.pvr1.databinding.CarveryDialogBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.ui.payment.PaymentActivity
import com.net.pvr1.ui.payment.cardDetails.CardDetailsActivity
import com.net.pvr1.ui.payment.cred.CredActivity
import com.net.pvr1.ui.payment.giftcardredeem.GiftCardRedeemActivity
import com.net.pvr1.ui.payment.paytmpostpaid.PaytmPostPaidActivity
import com.net.pvr1.ui.payment.promoCode.PromoCodeActivity
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.payment.response.UPIStatusResponse
import com.net.pvr1.ui.payment.webView.PaytmWebActivity
import com.net.pvr1.ui.seatLayout.adapter.AddFoodCartAdapter
import com.net.pvr1.ui.summery.adapter.SeatListAdapter
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.ui.summery.response.SummeryResponse
import com.net.pvr1.ui.summery.viewModel.SummeryViewModel
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.DONATION
import com.net.pvr1.utils.Constant.Companion.FOODENABLE
import com.net.pvr1.utils.Constant.Companion.TRANSACTION_ID
import com.net.pvr1.utils.Constant.Companion.foodCartModel
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.TransactionRequestBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

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
    private var paidAmount = 0
    private var upiLoader = false
    private var upiCount = 0
    private var payableAmount = 0.0


    //Bottom Dialog
    private var dialog: BottomSheetDialog? = null

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
                    cartModel = intent.getSerializableExtra("food") as ArrayList<CartModel>
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                manageFunction()
            }
        } else {
            if (intent.hasExtra("food")) foodCartModel =
                intent.getSerializableExtra("food") as ArrayList<CartModel>
            val intent = Intent(this@SummeryActivity, LoginActivity::class.java)
            intent.putExtra("from", "summery")
            startActivity(intent)
            finish()
        }
    }

    private fun manageFunction() {
        Constant.viewModel = authViewModel
        authViewModel.summery(
            TRANSACTION_ID, CINEMA_ID, preferences.getUserId(), BOOKING_ID, DONATION
        )

        movedNext()
        foodCart()
        summeryDetails()
        saveFood()
        setDonation()
        getShimmerData()
        timerManage()
        extendTime()
        paytmHMAC()
        credCheck()
        upiStatus()
        phonePeHmac()
        phonePeStatus()
    }


    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    //CartFood
    private fun foodCart() {
        if (cartModel.isNotEmpty()) {
            binding?.recyclerView32?.show()
            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = AddFoodCartAdapter(cartModel, this, this)
            binding?.recyclerView32?.layoutManager = layoutManagerCrew
            binding?.recyclerView32?.adapter = foodBestSellerAdapter
            binding?.recyclerView32?.setHasFixedSize(true)
        } else {
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
                    intent.putExtra("paidAmount", paidAmount)
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
                    intent.putExtra("paidAmount", paidAmount)
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("tc", pp?.tc)
                    startActivity(intent)
                }
                Constant.DEBIT_CARD -> {
                    val intent = Intent(this@SummeryActivity, CardDetailsActivity::class.java)
                    intent.putExtra("pTypeId", pp?.id)
                    intent.putExtra("paidAmount", paidAmount)
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("tc", pp?.tc)
                    startActivity(intent)
                }
                Constant.NET_BANKING -> {
                    val intent = Intent(this@SummeryActivity, CardDetailsActivity::class.java)
                    intent.putExtra("pTypeId", pp?.id)
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount)
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
                    intent.putExtra("paidAmount", paidAmount)
                    startActivity(intent)
                }
                Constant.PAYTM_WALLET -> {
                    val intent = Intent(this, PaytmPostPaidActivity::class.java)
                    intent.putExtra("type", "P")
                    intent.putExtra("ca_a", "")
                    intent.putExtra("tc", pp?.tc)
                    intent.putExtra("ca_t", "")
                    intent.putExtra("title", pp?.name)
                    intent.putExtra("paidAmount", paidAmount)
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
                    intent.putExtra("paidAmount", paidAmount)
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
                    intent.putExtra("paidAmount", paidAmount)
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
                    intent.putExtra("paidAmount", paidAmount)
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
                    intent.putExtra("paidAmount", paidAmount)
                    startActivity(intent)
                }
            }

        }
        binding?.include7?.textView108?.text = getString(R.string.checkout)
        binding?.include7?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
                        val intent = Intent(this@SummeryActivity, PaymentActivity::class.java)
                        intent.putExtra("paidAmount", paidAmount)
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
        binding?.textView313?.text = "You will earn  " + output.pe + " Privilege Points."

        //subtotal
        binding?.textView168?.text = output.ft

        //movie Details
        binding?.textView111?.text =
            output.cen + getString(R.string.dots) + output.lg + getString(R.string.dots) + output.fmt

        //audi
        binding?.textView115?.text = output.audi + "-" + output.st

        //ticket
        binding?.textView119?.text = output.f[0].it[0].n

        //price
        binding?.textView120?.text = getString(R.string.currency) + output.f[0].it[0].v

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
            }

        }

        //caver calling
        binding?.textView171?.text = ""


        //payedAmount
        payableAmount = output.a.toDouble()

        //grand total
        binding?.textView173?.text = getString(R.string.currency) + output.a

        //Image
        Glide.with(this)
            .load(output.imh)
            .error(R.drawable.placeholder_vertical)
            .into(binding?.imageView59!!)

        //title
        binding?.textView110?.text = output.m

        //shows
        binding?.textView112?.text = output.md + ", " + output.t

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

            if (cartModel.isNotEmpty()) {
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
                val intent = Intent(this@SummeryActivity, PaymentActivity::class.java)
                intent.putExtra("paidAmount", output.a)
                startActivity(intent)
            }
        }

        //manageFood
        if (FOODENABLE == 1) {
            binding?.constraintLayout157?.hide()
        } else {
            binding?.constraintLayout157?.show()
        }

        //Add More Food
        binding?.textView312?.setOnClickListener {
            onBackPressed()
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
            binding?.constraintLayout170?.hide()
        }
    }

    private fun setDonationData(output: SummeryResponse.Output) {
        //add
        binding?.textView400?.hide()

        //remove
        binding?.textView172?.show()

        binding?.textView165?.text = output.don_text


        //total seat
        val donationAmount = output.don / 100
        val ticketCount = donationAmount * output.seat.size

        binding?.textView171?.text = getString(R.string.currency) + " " + ticketCount.toString()
        binding?.textView166?.text = donationAmount.toString() + "/" + getString(R.string.ticket)


//    payableAmount
        if (cartModel.isNotEmpty()) {
            val foodPrice = calculateTotalPrice()
            val foodTotPrice = Constant.DECIFORMAT.format(foodPrice / 100.0)

            paidAmount = (payableAmount + donationAmount.toDouble() + foodTotPrice.toInt()).roundToInt()
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + paidAmount + " |"

        } else {
            paidAmount = (payableAmount + donationAmount).roundToInt()
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + paidAmount + " |"

        }
    }

    private fun removeDonationData(output: SummeryResponse.Output) {
        //add
        binding?.textView400?.show()
        //remove
        binding?.textView172?.hide()

        binding?.textView165?.text = output.don_text

        //total seat
        val donationAmount = 0
        val ticketCount =  output.seat.size

        binding?.textView171?.text = getString(R.string.currency) + " 0"
        binding?.textView166?.text = ticketCount.toString() + "/" + getString(R.string.ticket)

//    payableAmount
        if (cartModel.isNotEmpty()) {
            val foodPrice = calculateTotalPrice()
            val foodTotPrice = Constant.DECIFORMAT.format(foodPrice / 100.0)

            paidAmount = (payableAmount + donationAmount.toDouble() + foodTotPrice.toInt()).roundToInt()
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + paidAmount + " |"

        } else {
            paidAmount = (payableAmount + donationAmount).roundToInt()
            binding?.textView174?.text =
                getString(R.string.pay) + " " + getString(R.string.currency) + paidAmount + " |"

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

        bindingCarvery.textView310.paintFlags =
            bindingCarvery.textView310.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //click privacy
        bindingCarvery.textView310.setOnClickListener {
            dialog?.dismiss()
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("from", "CheckOut")
            intent.putExtra("title", this.getString(R.string.terms_condition_text))
            intent.putExtra("getUrl", Constant.donation)
            startActivity(intent)
        }
//        dismiss dialog
        bindingCarvery.include10.textView5.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun increaseFoodClick(comingSoonItem: CartModel) {
        var num = comingSoonItem.quantity
        if (num > 10 || num == 10) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.max_item_msz),
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
        if (num < 0 || num == 0) {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                getString(R.string.min_item_msz),
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        } else {
            num -= 1
            comingSoonItem.quantity = num
            updateCartFoodCartList(comingSoonItem)
        }

    }

    private fun updateCartFoodCartList(recyclerData: CartModel) {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun removeCartItem(item: CartModel) {
        for (data in cartModel) {
            if (data.id == item.id) {
                cartModel.remove(data)
            }
        }
    }

    override fun onBackPressed() {
        if (FOODENABLE == 0) {
            finish()
        } else {
            authViewModel.cancelTrans(
                CINEMA_ID, TRANSACTION_ID, BOOKING_ID
            )
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
        registerReceiver(br, IntentFilter(BroadcastService.COUNTDOWN_BR))
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
//        resultLauncher.launch(intent,120)
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
                            intent.putExtra("paidAmount", paidAmount)
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
            var bundle: Bundle
            val txnResult: String? = null
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


}