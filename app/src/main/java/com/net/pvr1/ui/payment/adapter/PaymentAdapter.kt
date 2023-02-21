package com.net.pvr1.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemPaymentListBinding
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
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
                if (position ==( nowShowingList.size-1)){
                    binding.view220.hide()
                }else{
                    binding.view220.show()
                }
                if (this.c != null && !TextUtils.isEmpty(this.c)) {
                    binding.paymentModeSubTitle.show()
                    if (this.c.split("|").size == 1){
                        binding.paymentModeSubTitle.text = "1 Offer"
                    }else{
                        binding.paymentModeSubTitle.text = "${this.c.split("|").size}  Offer"
                    }
                }else{
                    binding.paymentModeSubTitle.hide()
                }

                if (this.id==Constant.PHONE_PE){
                    try {
                        if (isAppInstalled()) {
                            binding.textView124.text = this.name
                            holder.itemView.setOnClickListener { listener.paymentClick(this) }
                            holder.itemView.visibility = View.VISIBLE
                            binding.imageView81.setImageResource(R.drawable.p_phonepe)
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
                        binding.imageView81.setImageResource(R.drawable.payment_card)
                    } else if (Constant.PAYTM == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_paytm)
                    } else if (Constant.PAYTMPOSTPAID == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_postpiad)
                    } else if (Constant.MOBIKWIK == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_mobi)
                    } else if (Constant.OXYGEN == this.id) {
                        binding.imageView81.setImageResource(R.drawable.oxigen)
                    } else if (Constant.GEIFT_CARD == this.id) {
                        binding.imageView81.setImageResource(R.drawable.gift_card_default)
                    } else if (Constant.DC_CARD == this.id) {
                        binding.imageView81.setImageResource(R.drawable.payment_director)
                    } else if (Constant.DEBIT_CARD == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_card)
                    } else if (Constant.CREDIT_CARD == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_card)
                    } else if (Constant.NETBANKING == this.id) {
                        binding.imageView81.setImageResource(R.drawable.payment_net)
                    } else if (Constant.ZAGGLE == this.id) {
                        binding.imageView81.setImageResource(R.drawable.zaggle_icon)
                    } else if (Constant.OFFER_OPTION == this.id) {
                        binding.imageView81.setImageResource(R.drawable.ic_star)
                    } else if (Constant.BIN_OPTION == this.id) {
                        binding.imageView81.setImageResource(0)
                    } else if (Constant.AIRTEL == this.id) {
                        binding.imageView81.setImageResource(R.drawable.airtel_icon)
                    } else if (Constant.TEJ == this.id) {
                        binding.imageView81.setImageResource(R.drawable.tej)
                    } else if (Constant.PHONE_PE == this.id) {
                        println("Phonepe--->${this.id}")
                        binding.imageView81.setImageResource(R.drawable.p_phonepe)
                    } else if (Constant.EPAY_LATTER == this.id) {
                        binding.imageView81.setImageResource(R.drawable.epay)
                    } else if (Constant.TEZUPI == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_upi)
                    } else if (Constant.PAYTMUPI == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_upi)
                    } else if (Constant.PAYPAL == this.id) {
                        binding.imageView81.setImageResource(R.drawable.paypal)
                    } else if (Constant.PAYTM_CREDIT_CARD == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_card)
                    } else if (Constant.PAYTM_DEBIT_CARD == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_card)
                    } else if (Constant.PAYTM_NETBANKING == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_nb)
                    }else if (Constant.GYFTR == this.id) {
                        binding.imageView81.setImageResource(R.drawable.p_gyter)
                    }else if (Constant.ACCENTIVE == this.id) {
                        println("Phonepexpe--->${this.id}")
                        binding.imageView81.setImageResource(R.drawable.ticket_xpres)
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