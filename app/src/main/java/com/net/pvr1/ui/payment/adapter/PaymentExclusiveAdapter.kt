package com.net.pvr1.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemPaymentListBinding
import com.net.pvr1.ui.payment.response.PaymentResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

//category

class PaymentExclusiveAdapter(
    private var nowShowingList: ArrayList<PaymentResponse.Output.Offer>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<PaymentExclusiveAdapter.ViewHolder>() {
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
                binding.imageView81.setImageResource(R.drawable.ic_star)
                //title
                binding.textView124.text = this.name
                itemView.setOnClickListener{
                    listener.paymentExclusiveClick(this)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun paymentExclusiveClick(comingSoonItem: PaymentResponse.Output.Offer)

    }

}