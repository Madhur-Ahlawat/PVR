package com.net.pvr.ui.giftCard.activateGiftCard

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityActivateGiftCardBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.activateGiftCard.adapter.ActivateGiftCardAdapter
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.ui.giftCard.response.ActiveGCResponse
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ActivateGiftCardActivity : AppCompatActivity(),
    ActivateGiftCardAdapter.RecycleViewItemClickListener {
    private var binding: ActivityActivateGiftCardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferenceManager
    private var activeGiftList = ArrayList<ActiveGCResponse.Gca>()
    private var inActiveGiftList = ArrayList<ActiveGCResponse.Gca>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivateGiftCardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include12?.titleCommonToolbar?.text = "Active Gift Cards"
        binding?.include12?.btnBack?.setOnClickListener {
            onBackPressed()
        }

        //Screen Width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        authViewModel.activeGiftCard(preferences.getUserId())
        activateGiftCard()

        binding?.tvExpiredGift?.setOnClickListener {
            val intent = Intent(this, ExpiredGiftCardActivity::class.java)
            if (inActiveGiftList.size > 0) {
                intent.putExtra("expiredList", inActiveGiftList)
            }
            startActivity(intent)
        }

    }

    private fun activateGiftCard() {
        authViewModel.activeGCResponseLiveData.observe(this) {
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun getDetails(data: ActiveGCResponse.Gca) {
        authViewModel.redeemGCResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val detailIntent = Intent(
                            this@ActivateGiftCardActivity,
                            GiftCardDetailsActivity::class.java
                        )
                        detailIntent.putExtra("cardDetails", it.data.output)
                        detailIntent.putExtra("cardId", data.id)
                        detailIntent.putExtra("cardNo", data.gcn)
                        detailIntent.putExtra("giftedTo", data.r)
                        detailIntent.putExtra("giftedOn", data.dn)
                        if (data.ci != null && data.ci != "") detailIntent.putExtra(
                            "image",
                            data.ci
                        )
                        else detailIntent.putExtra("image", data.gi)
                        startActivity(detailIntent)
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
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: ActiveGCResponse.Output) {
        inActiveGiftList = output.gci

        if (inActiveGiftList.size > 0) {
            binding?.llBottom?.show()
            binding?.noData?.show()
        } else {
            binding?.llBottom?.hide()
        }

        binding?.noTitle?.text = "No Gift Cards Available"
        binding?.buttonProceed?.text = "Buy Gift Cards"
        binding?.noSubTitle?.text = "Your Gift Cards will appear here"
        binding?.noDataImg?.setImageResource(R.drawable.gift_card_no_data)

        if (output.gca.isEmpty()) {
            binding?.noData?.show()
        } else {
            binding?.noData?.hide()
        }

        if (output.gci.isEmpty()) {
//            binding?.buttonProceed?.hide()
            binding?.noData?.show()
        } else {
            binding?.noData?.hide()
        }

        //back Button
        binding?.buttonProceed?.setOnClickListener {
            finish()
        }

        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardMainAdapter2 = ActivateGiftCardAdapter(output.gca, this, this)
        binding?.recyclerView30?.layoutManager = gridLayout2
        binding?.recyclerView30?.adapter = giftCardMainAdapter2
    }

    override fun activateGiftCard(comingSoonItem: ActiveGCResponse.Gca) {
        if (comingSoonItem.customImageApprove != null && comingSoonItem.customImageApprove && comingSoonItem.ci != null && comingSoonItem.ci != "") {
            //Approved
            showDialog(comingSoonItem, comingSoonItem.gcn.replace("ID:", ""))
        } else if (comingSoonItem.up) {
            // Waiting for Approval
            showApprovalDialog(comingSoonItem, comingSoonItem.gcn.replace("ID:", ""))
        } else if (comingSoonItem.ci == null || comingSoonItem.ci == "") {
            // Details
            showDialog(comingSoonItem, comingSoonItem.gcn.replace("ID:", ""))
        } else {
            //Rejected
            val createIntent = Intent(
                this@ActivateGiftCardActivity, RejectedGiftCardActivity::class.java
            )
            createIntent.putExtra("cim", comingSoonItem.cim)
            createIntent.putExtra("msg", comingSoonItem.msg)
            createIntent.putExtra("pkGiftId", comingSoonItem.id.replace("ID: ", ""))
            startActivity(createIntent)
        }
    }

    private fun showDialog(data: ActiveGCResponse.Gca, giftId: String) {
        val pinDialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        pinDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pinDialog.setCancelable(true)
        pinDialog.setCanceledOnTouchOutside(true)
        pinDialog.setContentView(R.layout.gift_pin_dialog)
        pinDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pinDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pinDialog.window!!.setGravity(Gravity.BOTTOM)
        val et_card_pin = pinDialog.findViewById<EditText>(R.id.et_card_pin)
        val tv_proceed_detail = pinDialog.findViewById<TextView>(R.id.tv_proceed_detail)
        val iv_gift_image = pinDialog.findViewById<ImageView>(R.id.iv_gift_image)
        if (data.ci != null && data.ci != "") Glide.with(this).load(data.ci)
            .error(R.drawable.gift_card_placeholder).placeholder(R.drawable.gift_card_placeholder)
            .into(iv_gift_image!!)
        else Glide.with(this).load(data.ci).error(R.drawable.gift_card_placeholder)
            .placeholder(R.drawable.gift_card_placeholder).into(iv_gift_image!!)
        tv_proceed_detail?.setOnClickListener {
            if (et_card_pin?.text.toString().isNotEmpty()) {
                authViewModel.redeemGC(
                    preferences.getUserId(),
                    giftId.replace("ID:", "").trim(),
                    et_card_pin?.text.toString()
                )
                getDetails(data)
                et_card_pin?.setText("")
                pinDialog.dismiss()
            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please enter Card Pin",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }
        }
        pinDialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        pinDialog.show()
    }

    private fun showApprovalDialog(data: ActiveGCResponse.Gca, giftId: String?) {
        val pinDialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        pinDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        pinDialog.setCancelable(true)
        pinDialog.setCanceledOnTouchOutside(true)
        pinDialog.setContentView(R.layout.gift_approval_dialog)
        pinDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pinDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pinDialog.window!!.setGravity(Gravity.BOTTOM)
        val upText: TextView = pinDialog.findViewById<TextView>(R.id.upText)!!
        upText.text = data.cim.toString()
        val iv_gift_image = pinDialog.findViewById<ImageView>(R.id.iv_gift_image)
        if (data.ci != null && data.ci != "") Glide.with(this).load(data.ci)
            .error(R.drawable.gift_card_placeholder).placeholder(R.drawable.gift_card_placeholder)
            .into(iv_gift_image!!)
        else Glide.with(this).load(data.ci).error(R.drawable.gift_card_placeholder)
            .placeholder(R.drawable.gift_card_placeholder).into(iv_gift_image!!)
        pinDialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        pinDialog.show()
    }


}