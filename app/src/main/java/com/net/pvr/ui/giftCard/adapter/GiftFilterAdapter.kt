package com.net.pvr.ui.giftCard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.GiftCardCateogryItemBinding
import com.net.pvr.ui.giftCard.response.GiftCardsFilter
import com.net.pvr.utils.hide
import com.net.pvr.utils.show


class GiftFilterAdapter(
    private var nowShowingList: ArrayList<GiftCardsFilter>,
    private var context: Context, private var listner : RecycleViewItemClickListener
) :
    RecyclerView.Adapter<GiftFilterAdapter.ViewHolder>() {
    private var rowIndex = 0
    inner class ViewHolder(val binding: GiftCardCateogryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GiftCardCateogryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //Image

                if (this.imageValue==""){
                    binding.imageView8.setImageResource(R.drawable.ic_voucher_fill)
                }else {
                    Glide.with(context)
                        .load(this.imageValue)
                        .error(R.drawable.gift_card_placeholder)
                        .into(binding.imageView8)
                }



                binding.textView30.text = this.filterText

                if (rowIndex==position){
                    binding.textView30.setTextAppearance(R.style.text_black_bold)
                    binding.view24.show()
                }else{
                    binding.textView30.setTextAppearance(R.style.text_gray_gift)
                    binding.view24.hide()
                }

                holder.itemView.setOnClickListener {
                    rowIndex = position
                    listner.gcFilterClick(this,position)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun gcFilterClick(comingSoonItem: GiftCardsFilter, position: Int)
    }

}