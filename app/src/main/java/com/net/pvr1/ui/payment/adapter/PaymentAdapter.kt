package com.net.pvr1.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemPaymentListBinding
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.utils.Constant
import com.phonepe.intent.sdk.api.PhonePe.isAppInstalled
import com.phonepe.intent.sdk.api.PhonePeInitException

//category

class PaymentAdapter(
    private var nowShowingList: ArrayList<PaymentResponse.Output.Gateway>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPaymentListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (this.id==Constant.PHONE_PE){
                    try {
                        if (isAppInstalled()) {
                            binding.textView124.text = this.name
                            holder.itemView.setOnClickListener { listener.paymentClick(this) }
                            holder.itemView.visibility = View.VISIBLE
                        } else {
                            holder.itemView.visibility = View.GONE
                        }
                    } catch (e: PhonePeInitException) {
                        e.printStackTrace()
                        holder.itemView.visibility = View.GONE
                    }

                }else {
                    //title
                    binding.textView124.text = this.name
                    if (Constant.PRICE_DESK == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.payment_card)
                    } else if (Constant.PAYTM == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.paytm_icon)
                    } else if (Constant.PAYTMPOSTPAID == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.paytm_postpaid_icon)
                    } else if (Constant.MOBIKWIK == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.moviequick)
                    } else if (Constant.OXYGEN == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.oxigen)
                    } else if (Constant.GEIFT_CARD == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.giftp)
                    } else if (Constant.DC_CARD == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.payment_director)
                    } else if (Constant.DEBIT_CARD == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.debit_card_new)
                    } else if (Constant.CREDIT_CARD == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.debit_card_new)
                    } else if (Constant.NETBANKING == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.netbanking_new)
                    } else if (Constant.ZAGGLE == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.zaggle_icon)
                    } else if (Constant.OFFER_OPTION == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.ic_star)
                    } else if (Constant.BIN_OPTION == this.id) {
                        // holder.paymentImageView.setVisibility(View.GONE);
                    } else if (Constant.AIRTEL == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.airtel_icon)
                    } else if (Constant.TEJ == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.tej)
                    } else if (Constant.PHONE_PE == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.phonepe_icon)
                    } else if (Constant.EPAY_LATTER == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.epay)
                    } else if (Constant.TEZUPI == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.upi_new)
                    } else if (Constant.PAYTMUPI == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.upi_new)
                    } else if (Constant.PAYPAL == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.paypal)
                    } else if (Constant.PAYTM_CREDIT_CARD == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.debit_card_new)
                    } else if (Constant.PAYTM_DEBIT_CARD == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.debit_card_new)
                    } else if (Constant.PAYTM_NETBANKING == this.id) {
                        binding.imageView81.setBackgroundResource(R.drawable.netbanking_new)
                    }
                    holder.itemView.setOnClickListener { listener.paymentClick(this) }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun paymentClick(comingSoonItem: PaymentResponse.Output.Gateway)

    }

}