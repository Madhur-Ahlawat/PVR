package com.net.pvr1.ui.payment.cardDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCardDetailsBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.payment.cardDetails.adapter.NetBankingAdapter
import com.net.pvr1.ui.payment.cardDetails.viewModel.CardDetailsViewModel
import com.net.pvr1.ui.payment.response.PaytmHmacResponse
import com.net.pvr1.ui.payment.webView.PaymentWebActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.BOOK_TYPE
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CardDetailsActivity : AppCompatActivity(), NetBankingAdapter.RecycleViewItemClickListener {
    private var isNetBaking: Boolean = false

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCardDetailsBinding? = null
    private val authViewModel: CardDetailsViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var saveCardId = ""
    private var subscriptionId = ""
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
    private var amountNet = ""
    private var hmackeyNet = ""


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
            isNetBaking = true
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

    private fun movedNext() {
        //Back
        binding?.include27?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //Title
        binding?.include27?.textView108?.text = getString(R.string.pay_via_card)

        //Proceed Bt
        binding?.include28?.textView5?.setOnClickListener {

            val cardNumber = binding?.cardNumber?.text.toString()
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
            } else if (monthYear == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardMonth),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (cvv == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardCvv),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else if (nameOnCard == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.cardNumber),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else {
                authViewModel.paytmHMAC(
                    preferences.getUserId(),
                    Constant.BOOKING_ID,
                    Constant.TRANSACTION_ID,
                    false,
                    cardNumber,
                    "BOOKING",
                    paymentType,
                    "no",
                    "NO"
                )
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
                binding?.cvv?.setText("")
                isSavedCard = false
            }
        }
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

    private fun retrieveData(output: PaytmHmacResponse.Output) {
        val bankList: ArrayList<Map.Entry<String, String>> = ArrayList()
        if (output.nblist != null) {
            output.nblist.forEach {
                bankList.add(it)
            }
        }

        printLog("nbList--->${bankList}")
        if (bankList.isNotEmpty()) {
            hmackeyNet = output.hmackey
            amountNet = output.amount
            midNet = output.mid
            currencyNet = output.currency
            callingUrlNet = output.callingurl

            val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val foodBestSellerAdapter = NetBankingAdapter(bankList, this, this)
            binding?.recyclerView52?.layoutManager = layoutManagerCrew
            binding?.recyclerView52?.adapter = foodBestSellerAdapter
            binding?.recyclerView52?.setHasFixedSize(true)
        } else {
            expiryMonth = binding?.monthYear?.text.toString().split("/")[0]
            expiryYear = binding?.monthYear?.text.toString().split("/")[1]

            val intent = Intent(this@CardDetailsActivity, PaymentWebActivity::class.java)
            if (subscriptionId != "") intent.putExtra("subscriptionId", subscriptionId)
            intent.putExtra("token", output.hmackey)
            intent.putExtra("amount", output.amount)
            intent.putExtra("mid", output.mid)
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
                intent.putExtra("ccnumber", binding?.cardNumber?.text.toString())
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

    }


    override fun netBankingClick(comingSoonItem: Map.Entry<String, String>) {
        val intent = Intent(this@CardDetailsActivity, PaymentWebActivity::class.java)
        if (subscriptionId != "") intent.putExtra("subscriptionId", subscriptionId)
        intent.putExtra("token", hmackeyNet)
        intent.putExtra("amount", amountNet)
        intent.putExtra("mid", midNet)
        intent.putExtra("currency", currencyNet)
        intent.putExtra("saveCardId", saveCardId)
        intent.putExtra("paymentType", paymentType)

        if (!isFromNet) {
            if (binding?.checkBox?.isChecked == true && !isKotak) {
                intent.putExtra("saveCard", "1")
            } else {
                intent.putExtra("saveCard", "0")
            }
            intent.putExtra("cvv", binding?.cvv?.text.toString())
            intent.putExtra("ccnumber", binding?.cardNumber?.text.toString())
            intent.putExtra("expmonth", expiryMonth)
            intent.putExtra("expyear", expiryYear)
        }

        intent.putExtra("channelCode", comingSoonItem.key)
        intent.putExtra("paymenttype", paymentType)
        intent.putExtra("checksum", callingUrlNet)
        intent.putExtra("BookType", BOOK_TYPE)
        intent.putExtra("TICKET_BOOKING_DETAILS", "paymentIntentData")
        startActivity(intent)

    }


}