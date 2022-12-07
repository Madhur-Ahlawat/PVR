package com.net.pvr1.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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