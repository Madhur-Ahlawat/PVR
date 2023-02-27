package com.net.pvr.ui.payment.bankoffers.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.BinListItemBinding
import com.net.pvr.ui.payment.response.PaymentResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

//category

class BankOfferAdapter(
    private var nowShowingList: ArrayList<PaymentResponse.Output.Binoffer>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<BankOfferAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: BinListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BinListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                Glide.with(context)
                    .load(nowShowingList[position].imageUrl)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView5)
                binding.titleText.text = nowShowingList[position].name
                if (!nowShowingList[position].ca_a) {
                    binding.textView6.show()
                    binding.textView6.text = nowShowingList[position].name + " will not be refuded for cancellation"
                } else {
                    binding.textView6.hide()
                }

                binding.mainView.setOnClickListener {
                    listener.onPromoClick(this)
                }
                binding.textView5.setOnClickListener {
                    listener.onTNCClick(this)

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun onPromoClick(binOffer: PaymentResponse.Output.Binoffer)
        fun onTNCClick(binOffer: PaymentResponse.Output.Binoffer)
    }

}