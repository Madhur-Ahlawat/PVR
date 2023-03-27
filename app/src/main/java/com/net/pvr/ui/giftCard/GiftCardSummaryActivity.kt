package com.net.pvr.ui.giftCard

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityGiftcardSummaryBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.ui.giftCard.response.GiftCardDetailResponse
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.payment.PaymentActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class GiftCardSummaryActivity : AppCompatActivity() {
    private var binding: ActivityGiftcardSummaryBinding? = null
    private var loader: LoaderDialog? = null
    private var imageValue = ""
    private var paidAmount = "0.0"
    private var dis = "0.0"
    private var imageValueUri: Uri? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()
    private var df = DecimalFormat("#,###.00")

    private  var tncUrl =""
    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftcardSummaryBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Gift Card Summery")
            GoogleAnalytics.hitEvent(this, "gift_card_place_order", bundle)
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding?.llTop?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.llTop?.titleCommonToolbar?.text = "Your Order"
        //Screen Width

        initData()

        if (!preferences.getIsLogin()) {
            val intent4 = Intent(this, LoginActivity::class.java)
            intent4.putExtra("from", "GC")
            startActivity(intent4)
            finish()
        } else {
            authViewModel.detailGiftCard(Constant.BOOKING_ID,preferences.getUserId())
        }
        Constant.BOOK_TYPE = "GIFTCARD"

        getGiftCardDetails()
        binding?.llClickExpand?.setOnClickListener {
            if (binding?.llTotal?.visibility == View.VISIBLE){
                binding?.llTotal?.hide()
                binding?.tvDetail?.show()
                binding?.IVDown?.rotation = 360f
                binding?.rlDelivery?.hide()
                binding?.rlDiscount?.hide()
            }else{
                binding?.llTotal?.show()
                binding?.tvDetail?.hide()
                binding?.IVDown?.rotation = 180f
                if (!TextUtils.isEmpty(binding?.tvTextVal?.text.toString()))
                    binding?.rlDelivery?.show()
                if (!TextUtils.isEmpty(dis))
                    binding?.rlDiscount?.show()

            }

        }
    }

    @SuppressLint("SetTextI18n")
   private fun addData(f: List<GiftCardDetailResponse.Fn>, dc: String) {
        binding?.llTotal?.removeAllViews()
        for (i in f.indices) {
            val view: View =
                LayoutInflater.from(this).inflate(R.layout.layout_gift_total_row, binding?.llTotal, false)
            val tvText: TextView = view.findViewById<View>(R.id.tvText) as TextView
            val tvTextVal: TextView = view.findViewById<View>(R.id.tvTextVal) as TextView
            tvText.text = resources.getString(R.string.currency) + f[i].n
            val amount: Double = java.lang.Double.valueOf(f[i].v)
            tvTextVal.text = resources.getString(R.string.currency) + df.format(amount)
            binding?.llTotal?.addView(view)
        }
        if (!TextUtils.isEmpty(dc)) {
            binding?.tvText?.text = getString(R.string.Delivery_data)
            binding?.tvTextVal?.text = resources.getString(R.string.currency) + dc.replace("Rs. ".toRegex(), "")
            //  rlDelivery.setVisibility(View.VISIBLE);
        } else {
            binding?.rlDelivery?.visibility = View.GONE
            binding?.rlDiscount?.visibility = View.GONE
        }
    }

    private var textTermsAndCondition: TextView? = null

    private fun initData() {
        if (intent != null) {
            if (intent.getStringExtra("imageValueUri") != null) {
                imageValue = intent.getStringExtra("imageValueUri")!!
                imageValueUri = Uri.parse(imageValue)
                binding?.imageLandingScreen?.setImageURI(imageValueUri)
            }
        }

//        binding?.checkboxTnc?.setOnClickListener{ v ->
//            val checked = (v as CheckBox).isChecked
//            // Check which checkbox was clicked
//            if (checked) {
//                binding?.llPayGift?.show()
//                binding?.llProceedGiftUnselect?.hide()
//                binding?.checkboxTnc?.isChecked = true
//            } else {
//                binding?.llPayGift?.hide()
//                binding?.llProceedGiftUnselect?.show()
//            }
//        }

        binding?.tvTermsGift?.setOnClickListener{
            val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.seat_t_c_dialog_layout)
            dialog.setCancelable(false)
            val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.show()
            dialog.setCanceledOnTouchOutside(false)

            val btnName = dialog.findViewById<TextView>(R.id.textView5)

            textTermsAndCondition = dialog.findViewById(R.id.textTermsAndCondition)
            val value =Constant().setSubtitle(tncUrl)
            val secretedData=value.replace("|", "")
//            val regex = "-?\\d+(\\.\\d+)?"
//            val sourceString = "<b>$regex</b> $secretedData"

            textTermsAndCondition?.text =secretedData

//            textTermsAndCondition?.text = value.replace("|", "")
            btnName?.text = getString(R.string.accept)
            btnName?.setOnClickListener {

                dialog.dismiss()
            }


        }


        binding?.llCancelGift?.setOnClickListener{
            onBackPressed()
        }

        binding?.otherPg?.rlMainLayout?.setOnClickListener{
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("paidAmount",paidAmount)
            startActivity(intent)
        }

//        binding?.llPayGift?.setOnClickListener{
//            //NEW GOOGLE IMPLEMENTED
//            if (binding?.checkboxTnc?.isChecked == true) {
//
//
//            } else {
//                val dialog = OptionDialog(this,
//                    R.mipmap.ic_launcher,
//                    R.string.app_name,
//                    "Please accept the T&C to proceed",
//                    positiveBtnText = R.string.ok,
//                    negativeBtnText = R.string.no,
//                    positiveClick = {},
//                    negativeClick = {})
//                dialog.show()
//            }
//        }


        binding?.checkboxTnc?.setOnCheckedChangeListener { _, isChecked ->
            // checkbox status is changed from uncheck to checked.
            if (isChecked) {
                binding?.checkboxTnc?.buttonTintList =
                    ColorStateList.valueOf(this.getColor(R.color.black))
            } else {
                binding?.checkboxTnc?.buttonTintList =
                    ColorStateList.valueOf(this.getColor(R.color.black))
            }
        }

        binding?.llProceedGiftUnselect?.setOnClickListener{
            if (binding?.checkboxTnc?.isChecked== false){
                binding?.checkboxTnc?.buttonTintList =
                    ColorStateList.valueOf(this.getColor(R.color.red))
                val shake: Animation =
                    AnimationUtils.loadAnimation(this@GiftCardSummaryActivity, R.anim.shake)
                binding?.tvTermsGift?.startAnimation(shake)
                Constant().vibrateDevice(this)

//                binding?.llPayGift?.show()
//                binding?.llProceedGiftUnselect?.hide()

            }else{
//                binding?.llPayGift?.show()
//                binding?.llProceedGiftUnselect?.hide()

                //PP Work to do
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("paidAmount",paidAmount)
                startActivity(intent)

            }
        }

        binding?.btnPay?.setOnClickListener{
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Gift Card Summery")
                GoogleAnalytics.hitEvent(this, "gift_card_your_order_pay", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            //PP Work to do
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("paidAmount",paidAmount)
            startActivity(intent)
        }

    }

    private fun getGiftCardDetails() {
        authViewModel.detailsGCResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        dis = it.data.output.dis
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: GiftCardDetailResponse.Output) {
        addData(output.fn,output.dc)
        tncUrl= output.tnC
        paidAmount = output.a
        binding?.otherPg?.rlMainLayout?.show()

        if (output.ch == ("PCARD") || output.ch == ("SCARD")) {
            binding?.tvdeliveryaddress?.text = output.da + "," + output.p
            binding?.rldeliveryAdd?.show()
        } else {
            binding?.rldeliveryAdd?.hide()
        }

        binding?.tvTitle?.text = output.title

        if (!TextUtils.isEmpty(output.pm)) {
            binding?.tvMessage?.text = output.pm
            binding?.llGiftMsg?.show()
        } else {
            binding?.tvMessage?.hide()
            binding?.llGiftMsg?.hide()
        }

        binding?.tvNamevalue?.text = output.rn
        binding?.tvMobilevalue?.text = output.m
        binding?.tvemailvalue?.text = output.em
        val amount: Double = java.lang.Double.valueOf(output.a)
        binding?.tvTOtalAmount?.text = resources.getString(R.string.currency) + df.format(amount)

        try {
            if (!TextUtils.isEmpty(output.dis)) {
                binding?.tvTOtalAmountCUt?.hide()
                val amount1: Double =
                    java.lang.Double.valueOf(output.dis)
                val aData = amount1 + amount
                binding?.tvTOtalAmountCUt?.text = "₹" + df.format(aData)
                binding?.tvTOtalAmountCUt?.paintFlags = (binding?.tvTOtalAmountCUt?.paintFlags!! or Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                val discount = amount1.toInt()
                val amount = amount.toInt()
                (discount * 100) / (discount + amount)

                // tvText_Discount.setText("Flat "+percentage+"% off on Gift Card");
                if (!TextUtils.isEmpty(output.dm))
                    binding?.tvTextDiscount?.text = output.dm
                binding?.tvTextValDiscount?.text = "-₹" + df.format(amount1)
                binding?.tvTextDiscount?.setTextColor(resources.getColor(R.color.orange_))
                binding?.tvTextValDiscount?.setTextColor(resources.getColor(R.color.orange_))
                if ((!TextUtils.isEmpty(output.kd) && !output.pv)) {
                    var percentage1 = ((discount * 100) / (discount + amount)).toDouble()
                    percentage1 += output.kd.toDouble()
                    val data = (percentage1 * aData) / 100
                    aData - data
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        try {
//            paymentIntentData.setCdm(giftCardPurchaseResponse.getData().getCdm())
//            paymentIntentData.setKd(giftCardPurchaseResponse.getData().getKd())
//            paymentIntentData.setKpd(giftCardPurchaseResponse.getData().getKpd())
//            paymentIntentData.setSd(giftCardPurchaseResponse.getData().isSd())
//            paymentIntentData.setPv(giftCardPurchaseResponse.getData().isPv())
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        if (imageValueUri != null) {
            binding?.imageLandingScreen?.setImageURI(imageValueUri)
        } else {

            Glide.with(this)
                .load(output.img)
                .error(R.drawable.gift_card_placeholder)
                .placeholder(R.drawable.gift_card_placeholder)
                .into(binding?.imageLandingScreen!!)
        }



    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            getString(R.string.confirm_msz),
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.cancel,
            positiveClick = {
                launchActivity(
                    GiftCardActivity::class.java,
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            },
            negativeClick = {
            })
        dialog.show()
    }


}