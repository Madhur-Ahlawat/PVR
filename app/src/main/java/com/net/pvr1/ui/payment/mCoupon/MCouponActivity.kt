package com.net.pvr1.ui.payment.mCoupon

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
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityMcouponBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.payment.PaymentActivity
import com.net.pvr1.ui.payment.promoCode.viewModel.PromoCodeViewModel
import com.net.pvr1.ui.payment.starPass.StarPasModel
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.M_COUPON
import com.net.pvr1.utils.Constant.Companion.SELECTED_SEAT
import com.net.pvr1.utils.view.CouponEditText
import com.net.pvr1.utils.view.CouponEditText.DrawableClickListener
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

    private var couponContainer:LinearLayout? = null
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
        couponContainer = binding?.mobileLayout
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

        addCoupon()


        binding?.minus?.setOnClickListener {
            if (getCount() > 1) deleteCoupon()
        }

        binding?.include30?.textView5?.setOnClickListener {
            val couponCodeList = getCouponCode()
            if (paymentOptionMode.equals(M_COUPON, ignoreCase = true)) {
                if (validateInputFields() && couponCodeList != null && couponCodeList.size == getCount())
                    redeemCoupon(couponCodeList)
                else {
                    toast(getErrorMessage())
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
            if (getCount() < maxSeatSelected) {
                addCoupon()
                binding?.foodCount?.text = "" + getCount()
            }
        }

        binding?.foodCount?.text = "" + getCount()

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
            promoCodeViewModel.mcoupon(preferences.getUserId(),Constant.BOOKING_ID,Constant.TRANSACTION_ID,Constant.BOOK_TYPE,Constant.CINEMA_ID,binding?.ccEditText?.text.toString()
            ,binding?.mobileEditText?.text.toString(),json)
            return
        } else {
            val starPass = StarPasModel(
                couponCodeList,
                Constant.BOOKING_ID
            )
            val gson = Gson()
            json = gson.toJson(starPass)
            promoCodeViewModel.starpass(preferences.getUserId(),Constant.BOOKING_ID,Constant.TRANSACTION_ID,Constant.BOOK_TYPE,Constant.CINEMA_ID,json)
            return
        }
    }


    private fun addCoupon() {
        if (couponContainer?.childCount!! > 0) {
            val ll = couponContainer?.getChildAt(couponContainer?.childCount!! - 1) as LinearLayout
            if (ll != null && ll.childCount == 1) {
                val childView: CouponEditText = ll.getChildAt(0) as CouponEditText
                couponContainer?.removeView(couponContainer?.getChildAt(couponContainer?.childCount!! - 1))
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 2.0f
                )
                layoutParams.setMargins(0, 18, 0, 0)
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.layoutParams = layoutParams
                for (i in 0..1) {
                    addChild(
                        linearLayout,
                        getCount() + i + 1,
                        if (i == 0) childView.text.toString() else ""
                    )
                }
                couponContainer?.addView(linearLayout)
            } else {
                val layoutParams = LinearLayout.LayoutParams(
                    couponContainer?.width?:0 / 2,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                layoutParams.setMargins(0, 18, 0, 0)
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.layoutParams = layoutParams
                addChild(linearLayout, getCount() + 1, "")
                couponContainer?.addView(linearLayout)
            }
        } else {
            val layoutParams = LinearLayout.LayoutParams(
                couponContainer?.width!! / 2,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.setMargins(0, 18, 0, 0)
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams = layoutParams
            addChild(linearLayout, getCount() + 1, "")
            couponContainer?.addView(linearLayout)
        }
    }

    private fun addChild(linearLayout: LinearLayout, position: Int, text: String) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            70, 1.0f
        )
        params.setMargins(18, 0, 0, 0)
        val couponEditText = CouponEditText(this)
        couponEditText.layoutParams = params
        couponEditText.setBackgroundResource(R.drawable.text_curve)
        couponEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close, 0)
        couponEditText.isSingleLine = true
        couponEditText.isAllCaps = true
        couponEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        val FilterArray = arrayOfNulls<InputFilter>(1)
        FilterArray[0] = LengthFilter(
            if (paymentOptionMode.equals(
                    M_COUPON,
                    ignoreCase = true
                )
            ) 8 else 10
        )
        couponEditText.filters = FilterArray
        couponEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
        couponEditText.setTextColor(resources.getColor(R.color.black))
        couponEditText.setPadding(
           18,
            0,
            18,
            0
        )
        if (TextUtils.isEmpty(text)) couponEditText.hint = "Coupon $position" else {
            couponEditText.setText(text)
        }
        couponEditText.tag = couponEditText
        couponEditText.setDrawableClickListener(object : DrawableClickListener {
            override fun onClick(target: DrawableClickListener.DrawablePosition?, v: View?) {
                val editText: CouponEditText = v?.tag as CouponEditText
                when (target) {
                    DrawableClickListener.DrawablePosition.RIGHT -> {
                        editText.setText("")
                        editText.background = ContextCompat.getDrawable(
                            this@MCouponActivity,
                            R.drawable.text_curve
                        )
                    }
                    else -> {}
                }
            }
        }, couponEditText)
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
            binding?.mobileInputLayout?.error = getString(R.string.mobile_msg_invalid)
        } else {
            binding?.mobileInputLayout?.error = null
        }
        if (!InputTextValidator.hasText(binding?.ccEditText!!) || binding?.ccEditText?.text.toString().length != 4) {
            binding?.ccInputLayout?.error = getString(R.string.card_number_msg_invalid)
        } else {
            binding?.ccInputLayout?.error = null
        }
        return (InputTextValidator.hasText(binding?.ccEditText!!)
                && InputTextValidator.hasText(binding?.mobileEditText!!)
                && binding?.mobileEditText?.text.toString().length == 5 && binding?.ccEditText?.text
            .toString().length == 4)
    }

    private fun deleteCoupon() {
        if (couponContainer?.childCount?:0 > 0) {
            val ll = couponContainer?.getChildAt(couponContainer?.childCount?:0 - 1) as LinearLayout
            if (ll.childCount == 1) {
                couponContainer?.removeView(ll)
            } else {
                val childView: CouponEditText = ll.getChildAt(0) as CouponEditText
                couponContainer?.removeView(ll)
                val layoutParams = LinearLayout.LayoutParams(
                    couponContainer?.width?:0 / 2,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                layoutParams.setMargins(0,18, 0, 0)
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.layoutParams = layoutParams
                addChild(linearLayout, getCount() + 1, childView.getText().toString())
                couponContainer?.addView(linearLayout)
            }
        }
        couponContainer?.invalidate()
        binding?.foodCount?.text = "" + getCount()
    }

    private fun getCount(): Int {
        var count = 0
        if (couponContainer?.childCount == 1) {
            val ll = couponContainer?.getChildAt(couponContainer?.childCount?:0 - 1) as LinearLayout
            count = ll.childCount
        } else if (couponContainer?.childCount?:0 > 1) {
            val ll = couponContainer?.getChildAt(couponContainer?.childCount?:0 - 1) as LinearLayout
            count = ll.childCount
            count += 2 * (couponContainer?.childCount?:0 - 1)
        }
        return count
    }


    private fun getCouponCode(): ArrayList<String>? {
        val couponCode = ArrayList<String>()
        if (couponContainer?.childCount!! > 0) {
            for (i in 0 until couponContainer?.childCount!!) {
                val ll = couponContainer?.getChildAt(i) as LinearLayout
                if (ll != null) {
                    for (j in 0 until ll.childCount) {
                        val couponEditText: CouponEditText = ll.getChildAt(j) as CouponEditText
                        if (!TextUtils.isEmpty(couponEditText.text.toString())) couponCode.add(
                            couponEditText.text.toString().toUpperCase()
                        ) else couponEditText.background = ContextCompat.getDrawable(
                            this,
                            R.drawable.text_curve
                        )
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
                            PaymentActivity.isPromocodeApplied = it.data.output.creditCardOnly
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
                            PaymentActivity.isPromocodeApplied = it.data.output.creditCardOnly
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
                                    launchActivity(PaymentActivity::class.java,
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