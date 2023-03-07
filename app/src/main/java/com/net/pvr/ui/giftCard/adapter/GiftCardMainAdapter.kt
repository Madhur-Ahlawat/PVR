package com.net.pvr.ui.giftCard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.GiftCardMainBinding
import com.net.pvr.ui.giftCard.response.GiftCardListResponse


class GiftCardMainAdapter(
    private var nowShowingList: List<GiftCardListResponse.Output.GiftCard>,
    private var context: Context,
    private var listner: RecycleViewItemClickListener,
    private val filterText: String
) :
    RecyclerView.Adapter<GiftCardMainAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: GiftCardMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GiftCardMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            if (filterText != "ALL OCCASIONS") {
                with(nowShowingList[nowShowingList.size - 1]) {
                    //Image
                    Glide.with(context)
                        .load(this.newImageUrl)
                        .error(R.drawable.gift_card_default)
                        .placeholder(R.drawable.gift_card_default)
                        .into(binding.ivImageGeneric)

                    holder.itemView.setOnClickListener {
                        listner.giftCardClick(this)
                    }
                }
            }else{
                with(nowShowingList[position]) {
                    //Image
                    Glide.with(context)
                        .load(this.newImageUrl)
                        .error(R.drawable.gift_card_default)
                        .placeholder(R.drawable.gift_card_default)
                        .into(binding.ivImageGeneric)

                    holder.itemView.setOnClickListener {
                        listner.giftCardClick(this)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        if (filterText == "ALL OCCASIONS") {
            return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
        }else{
            return 1
        }
    }


    interface RecycleViewItemClickListener {
        fun giftCardClick(comingSoonItem: GiftCardListResponse.Output.GiftCard)
    }

}