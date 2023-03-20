package com.net.pvr.ui.payment.cardDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityCardDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.privilege.EnrollInPassportActivity
import com.net.pvr.ui.home.fragment.privilege.NonMemberFragment
import com.net.pvr.ui.home.fragment.privilege.NonMemberFragment.Companion.maxtrycount
import com.net.pvr.ui.payment.PaymentActivity
import com.net.pvr.ui.payment.PaymentActivity.Companion.subsId
import com.net.pvr.ui.payment.cardDetails.adapter.NetBankingAdapter
import com.net.pvr.ui.payment.cardDetails.viewModel.CardDetailsViewModel
import com.net.pvr.ui.payment.response.PaytmHmacResponse
import com.net.pvr.ui.payment.webView.PaymentWebActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.BOOKING_ID
import com.net.pvr.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr.utils.Constant.Companion.CALL_PAYMODE
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CardDetailsActivity : AppCompatActivity(), NetBankingAdapter.RecycleViewItemClickListener {
    private var isNetBaking: Boolean = false
    var hmacSubs = false

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCardDetailsBinding? = null
    private val authViewModel: CardDetailsViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var saveCardId = ""
    private var paymentType = ""
    private var isSavedCard = false
    private var isFromNet = false
    private var isKotak = false
    private var lastInput = ""
    private var selectedBankCode = ""
    private var expiryMonth = ""
    private var expiryYear = ""

    //netBanking Case
    private var callingUrlNet = ""
    private var currencyNet = ""
    private var midNet = ""
    private var title = ""
    private var amountNet = ""
    private var hmackeyNet = ""
    var callCount = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        paymentType = intent.getStringExtra("pTypeId").toString()
        //PaidAmount
        binding?.textView287?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")
        movedNext()
        paytmHMAC()
        bankList()
        paytmRHMAC()
        bankOffer()
        if (intent.hasExtra("ccnumber")){
            binding?.cardNumber?.setText(intent.getStringExtra("ccnumber"))
            binding?.cardNumber?.isEnabled =false
            binding?.cardNumber?.isFocusable =false
            binding?.cardNumber?.isFocusableInTouchMode =false
        }
        if (BOOK_TYPE == "RECURRING") {
            checkBinForRecurring()
            binding?.reccurMsg?.show()
        }else{
            binding?.reccurMsg?.hide()
        }
        if (paymentType.equals("116", ignoreCase = true)) {
            paymentType = getString(R.string.mobikwik_addmoney_payment_type_credit_card)
            binding?.constraintLayout130?.show()
            isNetBaking = false
        } else if (paymentType.equals("117", ignoreCase = true)) {
            paymentType = getString(R.string.mobikwik_addmoney_payment_type_dedit_card)
            binding?.constraintLayout130?.show()
            isNetBaking = false
        } else if (paymentType.equals("118", ignoreCase = true)) {
            paymentType = getString(R.string.mobikwik_addmoney_payment_type_net_banking)
            binding?.constraintLayout130?.hide()
            binding?.recyclerView52?.show()
            isNetBaking = true
            authViewModel.bankList()
        }

        binding?.cardNumber?.addTextChangedListener(FourDigitCardFormatWatcher())



    }

    private fun movedNext() {
        //Back
        binding?.include27?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //Title
        binding?.include27?.textView108?.text = getString(R.string.pay_via_card)+" "+intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()

        //Proceed Bt
        binding?.include28?.textView5?.text = "Verify & Pay"
        binding?.include28?.textView5?.setOnClickListener {

            val cardNumber = binding?.cardNumber?.text.toString().trim()
            val monthYear = binding?.monthYear?.text.toString()
            val cvv = binding?.cvv?.text.toString()
            val nameOnCard = binding?.nameOnCard?.text.toString()

            if (cardNumber == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardNumber),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (cardNumber.length<16) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardNumberValid),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }else if (monthYear == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardMonth),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }else if (monthYear.length<5 && !monthYear.contains("/")) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardMonthValid),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (cvv == "" ) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardCvv),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }else if ((cvv.length<3)) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardCvvValid),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }  else {
                if (BOOK_TYPE == "RECURRING") {
                    authViewModel.paytmRHMAC(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        false,
                        cardNumber,
                        BOOK_TYPE,
                        paymentType,
                        "no",
                        "NO"
                    )
                }else{
                    if (paymentType == "BIN"){
                        authViewModel.bankOffer(
                            preferences.getUserId(),
                            Constant.BOOKING_ID,
                            Constant.TRANSACTION_ID,
                            BOOK_TYPE,
                            intent.getStringExtra("scheem").toString(),
                            cardNumber,
                            "YES",
                            intent.getStringExtra("ptype").toString()
                        )
                    }else {
                        authViewModel.paytmHMAC(
                            preferences.getUserId(),
                            Constant.BOOKING_ID,
                            Constant.TRANSACTION_ID,
                            false,
                            cardNumber,
                            BOOK_TYPE,
                            paymentType,
                            "no",
                            "NO"
                        )
                    }
                }
            }

        }

        //mm//year
        binding?.monthYear?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(
                p0: CharSequence?, p1: Int, p2: Int, p3: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(
                p0: CharSequence?, start: Int, removed: Int, added: Int
            ) {
                val input = p0.toString()
                val formatter = SimpleDateFormat("MM/YY", Locale.GERMANY)
                val expiryDateDate = Calendar.getInstance()
                try {
                    expiryDateDate.time = formatter.parse(input) as Date
                } catch (e: ParseException) {
                    if (p0?.length == 2 && !lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            binding?.monthYear?.setText(
                                binding?.monthYear?.text.toString() + "/"
                            )
                            binding?.monthYear?.setSelection(
                                3
                            )
                        }
                    } else if (p0?.length == 2 && lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            binding?.monthYear?.setText(
                                binding?.monthYear?.text.toString().substring(0, 1)
                            )
                        }
                    }
                    lastInput = binding?.monthYear?.text.toString()
                    return
                }
            }
        })

        //cvv
        binding?.cvv?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
//                lCvv = ""
                saveCardId = ""
               // binding?.cvv?.setText("")
                isSavedCard = false
            }
        }

        binding?.cardNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {

                if (s.isNotEmpty() && s.replace(" ".toRegex(),"").length == 6) {
                    if (BOOK_TYPE == "RECURRING") {
                        if (maxtrycount>=callCount) {
                            if (maxtrycount != (callCount)) {
                                authViewModel.recurringBinCheck(
                                    preferences.getUserId(),
                                    Constant.BOOKING_ID,
                                    PaymentActivity.subsToken,
                                    s.toString().trim(),
                                    ""
                                )
                            } else {
                                show("Your card details are not valid for subscription, if you want to purchase passport one month then press on ok button! ")
                            }
                        } else {
                            authViewModel.recurringBinCheck(
                                preferences.getUserId(),
                                Constant.BOOKING_ID,
                                PaymentActivity.subsToken,
                                s.toString().trim(),
                                ""
                            )
                        }
                    }
                }
            }
        })
    }

    private fun paytmHMAC() {
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
    private fun bankList() {
        authViewModel.liveBankDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val bankList: ArrayList<Map.Entry<String, String>> = ArrayList()
                        if (it.data.output != null) {
                            it.data.output.forEach { it1 ->
                                bankList.add(it1)
                            }
                            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                            val foodBestSellerAdapter = NetBankingAdapter(bankList, this, this)
                            binding?.recyclerView52?.layoutManager = layoutManagerCrew
                            binding?.recyclerView52?.adapter = foodBestSellerAdapter
                            binding?.recyclerView52?.setHasFixedSize(true)
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
    private fun paytmRHMAC() {
        authViewModel.liveDataRScope.observe(this) {
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

    private fun checkBinForRecurring() {
        authViewModel.recurringBinLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        hmacSubs = true
                        binding?.subsView?.show()
                        binding?.msg1?.text = it.data.output.tf
                        binding?.msg2?.text = it.data.output.ts
                        binding?.msg1?.setTextColor(Color.parseColor("#007D23"))
                        binding?.subsView?.setBackgroundResource(R.drawable.subs_valid_rect)
                    } else {
                        hmacSubs = false
                        binding?.msg1?.setTextColor(Color.parseColor("#ED1B2E"))
                        binding?.msg1?.text = it.data?.output?.tf
                        binding?.msg2?.text = it.data?.output?.ts
                        binding?.subsView?.show()
                        binding?.subsView?.setBackgroundResource(R.drawable.subs_invalid_rect)
                        callCount += 1
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
    private fun generateNewOrder() {
        authViewModel.recurringNewLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        subsId = ""
                        BOOK_TYPE = it.data.output.booktype
                        BOOKING_ID = it.data.output.id
                        CALL_PAYMODE = 1
                        onBackPressed()
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

    private fun retrieveData(output: PaytmHmacResponse.Output) {


            hmackeyNet = output.hmackey
            amountNet = output.amount
            midNet = output.mid
            currencyNet = output.currency
            callingUrlNet = output.callingurl

        if (binding?.monthYear?.text.toString()!="") {
            expiryMonth = binding?.monthYear?.text.toString().split("/")[0]
            expiryYear = binding?.monthYear?.text.toString().split("/")[1]
        }

            val intent = Intent(this@CardDetailsActivity, PaymentWebActivity::class.java)
            if (subsId != "")
                intent.putExtra("subscriptionId", subsId)
            intent.putExtra("token", output.hmackey)
            intent.putExtra("amount", output.amount)
            intent.putExtra("mid", output.mid)
            intent.putExtra("title", title)
            intent.putExtra("currency", output.currency)
            intent.putExtra("saveCardId", saveCardId)
            intent.putExtra("paymentType", paymentType)

            if (!isFromNet) {
                if (binding?.checkBox?.isChecked == true && !isKotak) {
                    intent.putExtra("saveCard", "1")
                } else {
                    intent.putExtra("saveCard", "0")
                }

                intent.putExtra("cvv", binding?.cvv?.text.toString())
                intent.putExtra("ccnumber", binding?.cardNumber?.text.toString().trim())
                intent.putExtra("expmonth", expiryMonth)
                intent.putExtra("expyear", expiryYear)
            }

            intent.putExtra("channelCode", selectedBankCode)
            intent.putExtra("paymenttype", paymentType)
            intent.putExtra("checksum", output.callingurl)
            intent.putExtra("BookType", BOOK_TYPE)
            intent.putExtra("TICKET_BOOKING_DETAILS", "paymentIntentData")
            startActivity(intent)

    }


    override fun netBankingClick(comingSoonItem: Map.Entry<String, String>) {

        selectedBankCode = comingSoonItem.key
        if (BOOK_TYPE == "RECURRING") {
            authViewModel.paytmRHMAC(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                false,
                "",
                BOOK_TYPE,
                paymentType,
                "no",
                "NO"
            )
        }else{
            authViewModel.paytmHMAC(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                false,
                "",
                BOOK_TYPE,
                paymentType,
                "no",
                "NO"
            )
        }

    }

    fun show(msg: String?) {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.retry_popup)
        dialog.setCancelable(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val cross_text = dialog.findViewById<TextView>(R.id.cross_text)
        val cross_text_new = dialog.findViewById<TextView>(R.id.cross_text_new)
        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val ok_btn = dialog.findViewById<TextView>(R.id.ok_btn)
        cross_text_new?.text = NonMemberFragment.retrymsg1
        cross_text?.text = NonMemberFragment.retrymsg2
        dialog.show()
        ok_btn!!.setOnClickListener {
            authViewModel.passportGenerate(
                preferences.getUserId(),
                preferences.getCityName(),
                NonMemberFragment.scheme_id,
                BOOKING_ID,
                maxtrycount.toString(),""
            )
            generateNewOrder()
            dialog.dismiss()
        }
        cancel!!.setOnClickListener {
            val intent = Intent(this@CardDetailsActivity, EnrollInPassportActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }

    class FourDigitCardFormatWatcher : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {
            // Remove spacing char
            if (s.isNotEmpty() && s.length % 5 == 0) {
                val c = s[s.length - 1]
                if (space == c) {
                    s.delete(s.length - 1, s.length)
                }
            }
            // Insert char where needed.
            if (s.isNotEmpty() && s.length % 5 == 0) {
                val c = s[s.length - 1]
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(
                        s.toString(),
                        space.toString()
                    ).size <= 3
                ) {
                    s.insert(s.length - 1, space.toString())
                }
            }
        }

        companion object {
            // Change this to what you want... ' ', '-' etc..
            private const val space = ' '
        }
    }


    private fun bankOffer() {
        authViewModel.bankOfferDataScope.observe(this) {
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


}