package com.net.pvr1.ui.payment.giftcardredeem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityGiftcardRedeemBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.payment.PaymentActivity
import com.net.pvr1.ui.payment.giftcardredeem.viewModel.GiftcardRedeemViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GiftCardRedeemActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityGiftcardRedeemBinding? = null
    private var title: String = ""
    private var loader: LoaderDialog? = null
    private var stringtex = ""
    private var type = "GIFT_CARD"
    private var paymentOptionMode = ""
    var c = ""
    private val giftcardRedeemViewModel: GiftcardRedeemViewModel by viewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftcardRedeemBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include29?.textView108?.text = intent.getStringExtra("title")
        title = intent.getStringExtra("title").toString()
        paymentOptionMode = intent.getStringExtra("pid").toString()
        //PaidAmount
        binding?.textView178?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")
        binding?.include29?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        type = intent.extras?.getString("type").toString()
        if (intent.getStringExtra("c") != null) {
            c = intent.getStringExtra("c")?.split("-")?.get(0) ?: ""
        }

        if (intent.extras?.getBoolean("ca_a") === false)
            binding?.textView371?.text = intent.extras?.getString("ca_t")
          binding?.textView373?.text = intent.extras?.getString("tc")


        binding?.include30?.constraintLayout10?.setOnClickListener(View.OnClickListener {
            if (validateInputFields())
                setDataToApi()
            Constant().hideKeyboard(this)
        })

        callZagglePay()
        callGiftCardRedeem()
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

    private fun usePromoCode() {
        if (paymentOptionMode == "126") {
            giftcardRedeemViewModel.zagglePay(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,
                binding?.ccEditText?.text.toString(),
                binding?.pinEditText?.text.toString(),
                paymentOptionMode
            )
        } else {
            giftcardRedeemViewModel.giftCardRedeem(
                preferences.getUserId(),
                Constant.BOOKING_ID,
                Constant.TRANSACTION_ID,
                Constant.BOOK_TYPE,
                binding?.ccEditText?.text.toString(),
                binding?.pinEditText?.text.toString(),
                paymentOptionMode
            )
        }
    }


    private fun setDataToApi() {
        usePromoCode()
    }

    private fun validateInputFields(): Boolean {
        if (!InputTextValidator.hasText(binding?.ccEditText!!)) {
            binding?.ccInputLayout?.error == (getString(R.string.card_number_msg_required))
        } else {
            binding?.ccInputLayout?.error == (getString(R.string.card_number_msg))
        }
        if (!InputTextValidator.hasText(binding?.pinEditText!!)) {
            binding?.pinInputLayout?.error == (getString(R.string.card_pin_msg_required))
        } else {
            binding?.pinInputLayout?.error == (getString(R.string.card_pin_msg))
        }
        if (!paymentOptionMode.equals("126", ignoreCase = true)) {
            if (!InputTextValidator.validatePin(binding?.pinEditText!!)) {
                if (binding?.pinEditText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                    binding?.pinInputLayout?.error == (getString(R.string.card_pin_msg_required))
                } else binding?.pinInputLayout?.error == (getString(R.string.card_pin_msg_invalid))
            } else {
                binding?.pinInputLayout?.error == (getString(R.string.card_pin_msg))
            }
        }
        if (!InputTextValidator.validateCard(binding?.ccEditText!!)) {
            if (binding?.ccEditText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                binding?.ccInputLayout?.error == (getString(R.string.card_number_msg_required))
            } else binding?.ccInputLayout?.error == (getString(R.string.card_number_msg_invalid))
        } else {
            binding?.ccInputLayout?.error == (getString(R.string.card_number_msg))
        }
        return if (paymentOptionMode.equals("126", ignoreCase = true)) {
            (InputTextValidator.hasText(binding?.ccEditText!!) &&
                    InputTextValidator.hasText(binding?.pinEditText!!) &&
                    InputTextValidator.validateCard(binding?.ccEditText!!)
                    )
        } else {
            (InputTextValidator.hasText(binding?.ccEditText!!) &&
                    InputTextValidator.hasText(binding?.pinEditText!!) &&
                    InputTextValidator.validateCard(binding?.ccEditText!!) &&
                    InputTextValidator.validatePin(binding?.pinEditText!!)
                    )
        }
    }


    private fun callZagglePay() {
        giftcardRedeemViewModel.livezagglePayScope.observe(this) {
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
                                preferences.saveBoolean(
                                    Constant.SharedPreference.Has_Bin_Series,
                                    true
                                )
                            } else
                                preferences.saveBoolean(
                                    Constant.SharedPreference.Has_Bin_Series,
                                    false
                                )
                            PaymentActivity.isPromocodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals(
                                        "LOYALTYUNLIMITED",
                                        ignoreCase = true
                                    )
                                ) {
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
                                    launchActivity(
                                        PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    )

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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun callGiftCardRedeem() {
        giftcardRedeemViewModel.liveDataGiftCardRedeemScope.observe(this) {
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
                                preferences.saveBoolean(
                                    Constant.SharedPreference.Has_Bin_Series,
                                    true
                                )
                            } else
                                preferences.saveBoolean(
                                    Constant.SharedPreference.Has_Bin_Series,
                                    false
                                )
                            PaymentActivity.isPromocodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals(
                                        "LOYALTYUNLIMITED",
                                        ignoreCase = true
                                    )
                                ) {
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
                                    launchActivity(
                                        PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    )

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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }


}