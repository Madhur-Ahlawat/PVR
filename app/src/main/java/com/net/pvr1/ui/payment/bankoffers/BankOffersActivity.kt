package com.net.pvr1.ui.payment.bankoffers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityStarPassBinding
import com.net.pvr1.ui.payment.PaymentActivity
import com.net.pvr1.ui.payment.bankoffers.adapter.BankOfferAdapter
import com.net.pvr1.ui.payment.promoCode.viewModel.PromoCodeViewModel
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BankOffersActivity : AppCompatActivity(),BankOfferAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityStarPassBinding? = null
    private var couponList = ArrayList<StarPasModel>()
    private var itemSize: Int = 3
    private val promoCodeViewModel: PromoCodeViewModel by viewModels()
    private var paidAmount = "0"
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarPassBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include29?.imageView58?.setOnClickListener {
            onBackPressed()
        }
        binding?.include29?.textView108?.text = getString(R.string.bank_offer)

        //PaidAmount
        paidAmount = intent.getStringExtra("paidAmount").toString()
        binding?.textView178?.text =
            getString(R.string.pay) + " " + getString(R.string.currency) + Constant.DECIFORMAT.format(paidAmount.toDouble())

        val layoutManagerCrew = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val foodBestSellerAdapter = BankOfferAdapter(PaymentActivity.offerList, this, this)
        binding?.recyclerView50?.layoutManager = layoutManagerCrew
        binding?.recyclerView50?.adapter = foodBestSellerAdapter
        binding?.recyclerView50?.setHasFixedSize(true)
    }

    override fun onPromoClick(binoffer: PaymentResponse.Output.Binoffer) {
        val intent = Intent(this@BankOffersActivity, BankOfferDetailsActivity::class.java)
        intent.putExtra("paidAmount", Constant.DECIFORMAT.format(paidAmount.toDouble()))
        intent.putExtra("scheem", binoffer.scheme)
        intent.putExtra("title", binoffer.name)
        intent.putExtra("pTypeId", binoffer.pty)
        startActivity(intent)
    }

    override fun onTNCClick(binoffer: PaymentResponse.Output.Binoffer) {
        showTncDialog(this,binoffer.tc)
    }

    private fun showTncDialog(mContext: Context?, tnc: String?) {
        val dialog = BottomSheetDialog(mContext!!, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.tnc_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
        val cross = dialog.findViewById<View>(R.id.cross) as TextView?
        val tncText = dialog.findViewById<View>(R.id.tncText) as TextView?
        tncText!!.text = tnc
        cross!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}