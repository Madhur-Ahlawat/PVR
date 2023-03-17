package com.net.pvr.ui.myBookings.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.GiftCardItemBinding
import com.net.pvr.ui.myBookings.response.GiftCardResponse
import com.net.pvr.utils.show


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
                if (cinemaItem.r!=null && cinemaItem.r != "") {
                    binding.tvGift.text = cinemaItem.r
                    binding.tvGift.show()
                    binding.tvGiTxt.show()
                }
                //Price
                binding.tvValue.text = cinemaItem.ta
                //DateTime
                binding.tvDate.text = cinemaItem.d.replace("Date:","")
                //OrderId
                binding.orderId.text =context.getString(R.string.orderId)+ cinemaItem.id.replace("ID: ","")
                //resend
                binding.tvresend.setOnClickListener {
                    listener.resend(this)
                }
                if (cinemaItem.ci != null && cinemaItem.ci != ""){
                    Glide.with(context)
                        .load(cinemaItem.ci)
                        .error(R.drawable.gift_card_placeholder)
                        .placeholder(R.drawable.gift_card_placeholder)
                        .into(binding.giftCardImage)
                }else {
                    Glide.with(context)
                        .load(cinemaItem.gi)
                        .error(R.drawable.gift_card_placeholder)
                        .placeholder(R.drawable.gift_card_placeholder)
                        .into(binding.giftCardImage)
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