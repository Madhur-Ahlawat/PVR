package com.net.pvr.ui.payment.bankoffers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivityBankofferDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.payment.cardDetails.CardDetailsActivity
import com.net.pvr.ui.payment.cardDetails.viewModel.CardDetailsViewModel
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.BOOK_TYPE
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BankOfferDetailsActivity : AppCompatActivity(){
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityBankofferDetailsBinding? = null
    private val authViewModel: CardDetailsViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var paymentType = ""
    private var scheem = ""
    private var paidAmount = "0"
    private var  cardNumber = "0"

    //netBanking Case
    private var title = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankofferDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        paymentType = intent.getStringExtra("pTypeId").toString()
        scheem = intent.getStringExtra("scheem").toString()
        paidAmount = intent.getStringExtra("paidAmount").toString()
        //PaidAmount
        binding?.textView287?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + paidAmount
        movedNext()
        bankOffer()
        binding?.cardNumber?.addTextChangedListener(FourDigitCardFormatWatcher())
    }

    private fun movedNext() {
        //Back
        binding?.include27?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding?.include27?.textView108?.isSelected = true

        //Title
        binding?.include27?.textView108?.text = getString(R.string.pay_via_card)+" "+intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()

        //Proceed Bt
        binding?.include28?.textView5?.text = "Verify & Pay"
        binding?.include28?.textView5?.setOnClickListener {

            cardNumber = binding?.cardNumber?.text.toString().trim()
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
            } else{
                    authViewModel.bankOffer(
                        preferences.getUserId(),
                        Constant.BOOKING_ID,
                        Constant.TRANSACTION_ID,
                        BOOK_TYPE,
                        scheem,
                        cardNumber,
                        "NO",
                        paymentType
                    )
                }

        }

    }

    private fun bankOffer() {
        authViewModel.bankOfferDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val intent = Intent(this@BankOfferDetailsActivity, CardDetailsActivity::class.java)
                        intent.putExtra("pTypeId", "BIN")
                        intent.putExtra("ptype", paymentType)
                        val actualAmt = (paidAmount.toDouble()-(it.data.output.TICKET_DISCOUNT+it.data.output.FOOD_DISCOUNT))
                        intent.putExtra("paidAmount", actualAmt.toString())
                        intent.putExtra("title", title)
                        intent.putExtra("scheem", scheem)
                        intent.putExtra("tc", "")
                        intent.putExtra("ccnumber",cardNumber)
                        intent.putExtra("dis",it.data.output.disc)
                        startActivity(intent)
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


}