package com.net.pvr1.ui.payment.cardDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCardDetailsBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
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
class CardDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCardDetailsBinding? = null
    private val authViewModel: CardDetailsViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var saveCardId = ""
    private  var subscriptionId = ""
    private  var paymentType = ""
    private  var isSavedCard = false
    private var isFromNet = false
    private var isKotak = false
    private var lastInput = ""
    private var selectedBankCode = ""
    private var expiryMonth = ""
    private var expiryYear = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        paymentType=intent.getStringExtra("ptype").toString()
        movedNext()
        paytmHMAC()
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
            authViewModel.paytmHMAC(
                preferences.getUserId(),  Constant.BOOKING_ID, Constant.TRANSACTION_ID, false, binding?.cardNumber?.text.toString(), "BOOKING",paymentType, "no", "NO"
            )
        }

        //mm//year
        binding?.monthYear?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(
                p0: CharSequence?,
                start: Int,
                removed: Int,
                added: Int
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
                                binding?.monthYear?.text.toString()
                                    .substring(0, 1)
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
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: PaytmHmacResponse.Output) {
        expiryMonth=binding?.monthYear?.text.toString().split("/")[0]
        expiryYear=binding?.monthYear?.text.toString().split("/")[1]

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