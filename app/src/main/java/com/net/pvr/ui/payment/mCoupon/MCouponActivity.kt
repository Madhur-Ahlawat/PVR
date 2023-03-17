package com.net.pvr.ui.payment.mCoupon

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.net.pvr.R
import com.net.pvr.databinding.ActivityMcouponBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.payment.PaymentActivity
import com.net.pvr.ui.payment.bankoffers.StarPasModel
import com.net.pvr.ui.payment.promoCode.viewModel.PromoCodeViewModel
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.M_COUPON
import com.net.pvr.utils.Constant.Companion.SELECTED_SEAT
import com.net.pvr.utils.view.CouponEditText
import com.net.pvr.utils.view.CouponEditText.DrawableClickListener
import com.net.pvr.utils.view.CouponEditText.DrawableClickListener.DrawablePosition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MCouponActivity : AppCompatActivity() { @Inject
lateinit var preferences: PreferenceManager
    private var binding: ActivityMcouponBinding? = null
    private val promoCodeViewModel: PromoCodeViewModel by viewModels()
    private var maxSeatSelected = 0
    private var paymentOptionMode = ""
    private var loader: LoaderDialog? = null
    private var isFristTimeCalled = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMcouponBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include29?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        binding?.include29?.textView108?.text = intent.getStringExtra("title")
        paymentOptionMode = intent.getStringExtra("pid").toString()
        maxSeatSelected = SELECTED_SEAT
        if (intent.extras?.getBoolean("ca_a") === false)
            binding?.textView371?.text = intent.extras?.getString("ca_t")

        if (intent?.hasExtra("tc") == true)
            binding?.textView373?.text = intent.extras?.getString("tc")

        //PaidAmount
        binding?.textView178?.text = getString(R.string.pay) + " " + getString(R.string.currency) + intent.getStringExtra("paidAmount")

        if (paymentOptionMode == "O102"){
            binding?.ccLayout?.show()
            binding?.mobileNoLayout?.show()
        }else{
            binding?.ccLayout?.hide()
            binding?.mobileNoLayout?.hide()
        }

        callMCoupon()
        callStarPass()


        binding?.minus?.setOnClickListener {
            if (getCount() > 1) deleteCoupon()
        }

        binding?.include30?.textView5?.setOnClickListener {
            val couponCodeList = getCouponCode()
            if (paymentOptionMode.equals(M_COUPON, ignoreCase = true)) {
                if (validateInputFields() && couponCodeList != null && couponCodeList.size == getCount()) {
                    redeemCoupon(couponCodeList)
                }
            } else {
                if (couponCodeList != null && couponCodeList.size == getCount()) {
                    redeemCoupon(couponCodeList)
                } else {
                    toast("Coupon code required")
                }
            }
        }

        binding?.plus?.setOnClickListener {
            println("ll.child1---"+getCount()+"----"+maxSeatSelected)

            if (getCount() < maxSeatSelected) {
                addCoupon()
            }
        }


    }

    private fun redeemCoupon(couponCodeList: java.util.ArrayList<String>) {
        var json = ""
        if (paymentOptionMode.equals(M_COUPON, ignoreCase = true)) {
            val mCoupon = McouponRequestModel(couponCodeList,Constant.BOOKING_ID,
                binding?.mobileEditText?.text.toString(),
                binding?.ccEditText?.text.toString()
                )
            val gson = Gson()
            json = gson.toJson(mCoupon)
            println("jsonm--->$json")
            val string: String = java.lang.String.join(",", couponCodeList)

            promoCodeViewModel.mcoupon(preferences.getUserId(),Constant.BOOKING_ID,Constant.TRANSACTION_ID,Constant.BOOK_TYPE,Constant.CINEMA_ID,binding?.ccEditText?.text.toString()
            ,binding?.mobileEditText?.text.toString(),string)
            return
        } else {
            val starPass = StarPasModel(
                couponCodeList,
                Constant.BOOKING_ID
            )
            val gson = Gson()
            json = gson.toJson(starPass)
            println("json--->$json")
            val string: String = java.lang.String.join(",", couponCodeList)

            promoCodeViewModel.starpass(preferences.getUserId(),Constant.BOOKING_ID,Constant.TRANSACTION_ID,Constant.BOOK_TYPE,Constant.CINEMA_ID,string)
            return
        }
    }


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun addCoupon() {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (binding?.mobileLayout?.childCount!!>0) {
            layoutParams.setMargins(8, 55, 25, 0)
        }else{
            layoutParams.setMargins(8, 0, 25, 0)
        }
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = layoutParams
        addChild(linearLayout, getCount() + 1, "")
        binding?.mobileLayout?.addView(linearLayout)

        binding?.foodCount?.text = "" + getCount()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isFristTimeCalled) {
            addCoupon()
            isFristTimeCalled = !isFristTimeCalled
        }
    }

    private fun addChild(linearLayout: LinearLayout, position: Int, text: String) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            Constant().convertDpToPixel(58f,this), 1f
        )
        val couponEditText = CouponEditText(this)
        couponEditText.layoutParams = params
        couponEditText.setBackgroundResource(R.drawable.text_curve)
        couponEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.close, 0)
        couponEditText.isSingleLine = true
        couponEditText.isAllCaps = true
        couponEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = LengthFilter(
            if (paymentOptionMode.equals(
                    M_COUPON,
                    ignoreCase = true
                )
            ) 8 else 10
        )
        couponEditText.filters = filterArray
        couponEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
        couponEditText.setTextColor(resources.getColor(R.color.black))
        couponEditText.setPadding(
           28,
            0,
            18,
            0
        )
        if (TextUtils.isEmpty(text)) couponEditText.hint = "  Coupon $position" else {
            couponEditText.setText(text)
        }
        couponEditText.tag = couponEditText
        couponEditText.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawablePosition?, v: View?) {
                when (target) {
                    DrawablePosition.RIGHT -> {
                        couponEditText.setText("")
                    }
                    else -> {
                        couponEditText.setText("")
                    }
                }
            }
        })
//        couponEditText.setDrawableClickListener(object : DrawableClickListener {
//            override fun onClick(target: DrawableClickListener.DrawablePosition?, v: View?) {
//                val editText: CouponEditText = v?.tag as CouponEditText
//                editText.setText("")
//
//                when (target) {
//                    DrawableClickListener.DrawablePosition.RIGHT -> {
//                        editText.setText("")
//                        editText.background = ContextCompat.getDrawable(
//                            this@MCouponActivity,
//                            R.drawable.text_curve
//                        )
//                    }
//                    else -> {}
//                }
//            }
//        }, couponEditText)
        linearLayout.addView(couponEditText)
    }


    private fun getErrorMessage(): String? {
        var message = ""
        if (!InputTextValidator.hasText(binding?.ccEditText!!) && !InputTextValidator.hasText(binding?.mobileEditText!!)) {
            message =
                if (TextUtils.isEmpty(message)) message + "Credit card and mobile number required" else "$message\nCredit card and mobile number required"
        } else {
            if (!InputTextValidator.hasText(binding?.ccEditText!!)) {
                message =
                    if (TextUtils.isEmpty(message)) message + "Credit card required" else "$message\nCredit card required"
            } else {
                if (binding?.ccEditText?.text.toString().length != 4) {
                    message =
                        if (TextUtils.isEmpty(message)) message + "Credit card invalid" else "$message\nCredit card invalid"
                }
            }
            if (!InputTextValidator.hasText(binding?.mobileEditText!!)) {
                message =
                    if (TextUtils.isEmpty(message)) message + "Mobile number required" else "$message\nMobile number required"
            } else {
                if (binding?.mobileEditText?.text.toString().length != 5) {
                    message =
                        if (TextUtils.isEmpty(message)) message + "Mobile number invalid" else "$message\nMobile number invalid"
                }
            }
        }
        if (getCouponCode() != null && getCouponCode()!!.size != getCount()) {
            message =
                if (TextUtils.isEmpty(message)) message + "Coupon code required" else "$message\nCoupon code required"
        }
        return message
    }

    private fun validateInputFields(): Boolean {
        if (!InputTextValidator.hasText(binding?.mobileEditText!!) || binding?.mobileEditText?.text.toString().length != 5) {
            binding?.mobileError?.text = getString(R.string.mobile_msg_invalid)
        } else {
            binding?.mobileError?.text = ""
        }
        if (!InputTextValidator.hasText(binding?.ccEditText!!) || binding?.ccEditText?.text.toString().length != 4) {
            binding?.creditError?.text = getString(R.string.card_number_msg_invalid)
        } else {
            binding?.creditError?.text = ""
        }
        return (InputTextValidator.hasText(binding?.ccEditText!!)
                && InputTextValidator.hasText(binding?.mobileEditText!!)
                && binding?.mobileEditText?.text.toString().length == 5 && binding?.ccEditText?.text
            .toString().length == 4)
    }

    @SuppressLint("SetTextI18n")
    private fun deleteCoupon() {
        try {
            if ((binding?.mobileLayout?.childCount)!! > 1) {
                val ll =
                    binding?.mobileLayout?.getChildAt(binding?.mobileLayout?.childCount!! - 1) as LinearLayout
                println("ll.child---" + ll.childCount)
                if (ll.childCount == 1) {
                    binding?.mobileLayout?.removeView(ll)
                }
            }
            binding?.mobileLayout?.invalidate()
            binding?.foodCount?.text = "" + getCount()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }

    private fun getCount(): Int {
        var count = 0
        try {
             count = binding?.mobileLayout?.childCount!!
        }catch (e:Exception){
            e.printStackTrace()
        }
        return count
    }


    private fun getCouponCode(): ArrayList<String>? {
        val couponCode = ArrayList<String>()
        if (binding?.mobileLayout?.childCount!! > 0) {
            for (i in 0 until binding?.mobileLayout?.childCount!!) {
                val ll = binding?.mobileLayout?.getChildAt(i) as LinearLayout
                if (ll != null) {
                    for (j in 0 until ll.childCount) {
                        val couponEditText: CouponEditText = ll.getChildAt(j) as CouponEditText
                        if (!TextUtils.isEmpty(couponEditText.text.toString())) {
                            couponCode.add(couponEditText.text.toString().toUpperCase())
                            binding?.errorText?.text = ""
                        } else {
                            binding?.errorText?.text = "coupon code required"
                            couponEditText.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.text_curve
                            )
                        }
                    }
                }
            }
        }
        return couponCode
    }


    private fun callMCoupon() {
        promoCodeViewModel.mcouponScope.observe(this) {
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
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, true)
                            } else
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, false)
                            PaymentActivity.isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals("LOYALTYUNLIMITED", ignoreCase = true)) {
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
                                    launchPaymentActivity(PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK, intent.getStringExtra("paidAmount").toString()
                                    ,"MCoupon")
//                                    PaymentActivity.showTncDialog(this, it.data.output.di, "MCoupon")

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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }
    private fun callStarPass() {
        promoCodeViewModel.starpassOldScope.observe(this) {
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
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, true)
                            } else
                                preferences.saveBoolean(Constant.SharedPreference.Has_Bin_Series, false)
                            PaymentActivity.isPromoCodeApplied = it.data.output.creditCardOnly
                            if (it.data.output.p) {
                                Constant().printTicket(this)
                            } else {
                                if (Constant.BOOK_TYPE.equals("LOYALTYUNLIMITED", ignoreCase = true)) {
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
                                    launchPaymentActivity(PaymentActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK, intent.getStringExtra("paidAmount").toString()
                                    ,"Star Pass")
//                                    PaymentActivity.showTncDialog(this, it.data.output.di, )

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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }


}