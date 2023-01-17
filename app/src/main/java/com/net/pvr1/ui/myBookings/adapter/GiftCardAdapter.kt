package com.net.pvr1.ui.myBookings.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.GiftCardItemBinding
import com.net.pvr1.ui.myBookings.response.GiftCardResponse


class GiftCardAdapter(
    private var nowShowingList: List<GiftCardResponse.Output.Gc>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<GiftCardAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: GiftCardItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GiftCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cinemaItem = nowShowingList[position]
        with(holder) {
            with(nowShowingList[position]) {

                //giftedTo
                binding.tvGift.text = cinemaItem.r
                //Price
                binding.tvValue.text = cinemaItem.ta
                //DateTime
                binding.tvDate.text = cinemaItem.d
                //OrderId
                binding.orderId.text =context.getString(R.string.order)+ cinemaItem.id
                //resend
                binding.tvresend.setOnClickListener {
                    listener.resend(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun resend(comingSoonItem: GiftCardResponse.Output.Gc)
    }


}