package com.net.pvr.ui.giftCard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftcardSummaryBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
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

    private fun initData() {
        if (intent != null) {
            if (intent.getStringExtra("imageValueUri") != null) {
                imageValue = intent.getStringExtra("imageValueUri")!!
                imageValueUri = Uri.parse(imageValue)
                binding?.imageLandingScreen?.setImageURI(imageValueUri)
            }
        }

        binding?.checkboxTnc?.setOnClickListener{ v ->
            val checked = (v as CheckBox).isChecked
            // Check which checkbox was clicked
            if (checked) {
                binding?.llPayGift?.show()
                binding?.llProceedGiftUnselect?.hide()
                binding?.checkboxTnc?.isChecked = true
            } else {
                binding?.llPayGift?.hide()
                binding?.llProceedGiftUnselect?.show()
            }
        }

        binding?.tvTermsGift?.setOnClickListener{

            val termsDialog = Dialog(this)
            termsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            termsDialog.setCancelable(true)
            termsDialog.setCanceledOnTouchOutside(true)
            termsDialog.setContentView(R.layout.gc_terms_dialoge)
            termsDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            termsDialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            termsDialog.window!!.setGravity(Gravity.BOTTOM)
            termsDialog.findViewById<TextView>(R.id.tv_t_n_c)
            val tvAcceptTerms = termsDialog.findViewById<TextView>(R.id.tv_accept_terms)
            tvAcceptTerms.setOnClickListener {
                termsDialog.dismiss()
                binding?.llPayGift?.show()
                binding?.llProceedGiftUnselect?.hide()
                binding?.checkboxTnc?.isChecked = true
            }
            termsDialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
            termsDialog.show()

        }


        binding?.llCancelGift?.setOnClickListener{
            onBackPressed()
        }

        binding?.otherPg?.rlMainLayout?.setOnClickListener{
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("paidAmount",paidAmount)
            startActivity(intent)
        }

        binding?.llPayGift?.setOnClickListener{
            //NEW GOOGLE IMPLEMENTED
            if (binding?.checkboxTnc?.isChecked == true) {
//PP Work to do

                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("paidAmount",paidAmount)

                startActivity(intent)

            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please accept the T&C to proceed",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }
        }

        binding?.llProceedGiftUnselect?.setOnClickListener{
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "Please accept the T&C to proceed",
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {},
                negativeClick = {})
            dialog.show()
        }

        binding?.btnPay?.setOnClickListener{

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