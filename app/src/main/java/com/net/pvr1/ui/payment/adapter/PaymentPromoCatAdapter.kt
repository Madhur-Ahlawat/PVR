package com.net.pvr1.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemPaymentListBinding
import com.net.pvr1.databinding.OfferOptionItemBinding
import com.net.pvr1.ui.payment.response.PaymentResponse

//category

class PaymentPromoCatAdapter(
    private var nowShowingList: ArrayList<String>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<PaymentPromoCatAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: OfferOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OfferOptionItemBinding.inflate(
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
//                binding.imageView81.setImageResource(R.drawable.shows)
                //title
                binding.options.text = this
                itemView.setOnClickListener{
                    listener.onItemCatClick(this)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun onItemCatClick(cat:String)

    }

}