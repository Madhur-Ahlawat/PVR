package com.net.pvr.ui.giftCard.activateGiftCard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.giftCard.activateGiftCard.AddGiftCardActivity
import com.net.pvr.ui.giftCard.response.ActiveGCResponse
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import java.lang.String
import kotlin.Int


class GiftCardAddAmtAdapter(
    private var nowShowingList: ArrayList<GiftCardListResponse.Output.GiftCard>,
    private var context: Context,
    private var imageUri: Uri
) :
    RecyclerView.Adapter<GiftCardAddAmtAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_amt_list, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        holder.tv_gift_value.text =
            context.resources.getString(R.string.currency) + (cinemaItem.giftValue/100).toString()
        holder.tv_number_amount.text = String.valueOf(cinemaItem.count)
        println("imageUri----$imageUri----${cinemaItem.newImageUrl}")
        if (imageUri != null && imageUri.toString()!="") {
            Glide.with(context)
                .load(imageUri)
                .error(R.drawable.gift_card_default)
                .placeholder(R.drawable.gift_card_default)
                .into(holder.iv_add_gift_image)
        } else {
            Glide.with(context)
                .load(cinemaItem.newImageUrl)
                .error(R.drawable.gift_card_default)
                .placeholder(R.drawable.gift_card_default)
                .into(holder.iv_add_gift_image)
        }
        if (cinemaItem.count == 0) {
            holder.tv_add_amount.show()
            holder.ll_plus_minus.hide()
        } else {
            holder.tv_add_amount.hide()
            holder.ll_plus_minus.show()
        }
        holder.tv_add_amount.setOnClickListener(View.OnClickListener {
            (context as AddGiftCardActivity).plusClick(
                position,
                (cinemaItem.giftValue/100)
            )
        })
        holder.tv_plus_amount.setOnClickListener(View.OnClickListener {
            (context as AddGiftCardActivity).plusClick(
                position,
                (cinemaItem.giftValue/100)
            )
        })
        holder.view_minus.setOnClickListener(View.OnClickListener {
            (context as AddGiftCardActivity).minusClick(
                position,
                (cinemaItem.giftValue/100)
            )
        })

        holder.view_plus.setOnClickListener(View.OnClickListener {
            (context as AddGiftCardActivity).plusClick(
                position,
                (cinemaItem.giftValue/100)
            )
        })

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {

        var tv_gift_value = itemView.findViewById<TextView?>(R.id.tv_gift_value)
        var iv_add_gift_image = itemView.findViewById<ImageView?>(R.id.iv_add_gift_image)
        var item_add_amount = itemView.findViewById<LinearLayout?>(R.id.item_add_amount)
        var ll_plus_minus = itemView.findViewById<LinearLayout?>(R.id.ll_plus_minus)
        var tv_number_amount = itemView.findViewById<TextView?>(R.id.tv_number_amount)
        var tv_plus_amount = itemView.findViewById<TextView?>(R.id.tv_plus_amount)
        var tv_add_amount = itemView.findViewById<TextView?>(R.id.tv_add_amount)

        var view_minus = itemView.findViewById<LinearLayout?>(R.id.view_minus)
        var view_plus = itemView.findViewById<LinearLayout?>(R.id.view_plus)

    }

    interface RecycleViewItemClickListener {
        fun activateGiftCard(comingSoonItem: ActiveGCResponse.Gca)
    }


}