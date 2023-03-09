package com.net.pvr.ui.giftCard.activateGiftCard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ActivityGiftcardDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.giftCard.activateGiftCard.adapter.ActivateGiftCardAdapter
import com.net.pvr.ui.giftCard.activateGiftCard.adapter.GCTicketAdapter
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.ui.giftCard.response.GiftCardDetailResponse
import com.net.pvr.ui.giftCard.response.GiftcardDetailsResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class GiftCardDetailsActivity : AppCompatActivity(){
    private var binding: ActivityGiftcardDetailsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferenceManager
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGiftcardDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.include12?.titleCommonToolbar?.text = "Expired Gift Card"
        binding?.include12?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        //Screen Width

        Glide.with(this)
            .load(intent.getStringExtra("image"))
            .error(R.drawable.gift_card_placeholder)
            .placeholder(R.drawable.gift_card_placeholder)
            .into(binding?.ivImageGeneric!!)

        if (intent.getSerializableExtra("cardDetails") != null) {
            val giftCardDetailResponse = intent.getSerializableExtra("cardDetails") as GiftcardDetailsResponse.Output
            binding?.availBal?.text = getString(R.string.currency) + giftCardDetailResponse.b
            if (giftCardDetailResponse.tcklist.isNotEmpty()) {
                val tcklist = giftCardDetailResponse.tcklist
                val ticketAdapter = GCTicketAdapter(tcklist, this@GiftCardDetailsActivity)
                binding?.rvGiftHistory?.layoutManager = LinearLayoutManager(this)
                binding?.rvGiftHistory?.adapter = ticketAdapter
            }
        }

        binding?.cardId?.text = intent.getStringExtra("cardId")!!.replace("ID:".toRegex(), "")
        binding?.cardNo?.text = intent.getStringExtra("cardNo")
        binding?.giftedTo?.text = intent.getStringExtra("giftedTo")
        binding?.giftedOn?.text = intent.getStringExtra("giftedOn")

    }

}