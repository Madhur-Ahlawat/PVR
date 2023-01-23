package com.net.pvr1.ui.giftCard.activateGiftCard.adapter

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
import com.net.pvr1.R
import com.net.pvr1.ui.giftCard.activateGiftCard.AddGiftCardActivity
import com.net.pvr1.ui.giftCard.response.ActiveGCResponse
import com.net.pvr1.ui.giftCard.response.GiftCards
import java.lang.String
import kotlin.Int


class CustomGiftCardAdapter(
    private var nowShowingList: ArrayList<GiftCards>,
    private var context: Context,
    private var imageUri: Uri
) :
    RecyclerView.Adapter<CustomGiftCardAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_gift_list, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        holder.tv_gift_value.text =
            context.resources.getString(R.string.currency) + cinemaItem.d.replace("xCUSTOMISED", "")
        holder.tv_count.text = "x " + String.valueOf(cinemaItem.c)
        if (imageUri != null) {
            holder.iv_add_gift_image.setImageURI(imageUri)
        }
        holder.iv_close.setOnClickListener {
            (context as AddGiftCardActivity).customCloseClick(
                position,
                cinemaItem.d.replace("xCUSTOMISED", "")
            )
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {

        var tv_gift_value = itemView.findViewById<TextView>(R.id.tv_gift_value)!!
        var iv_add_gift_image = itemView.findViewById<ImageView>(R.id.iv_add_gift_image)!!
        var item_custom_gift_card = itemView.findViewById<LinearLayout>(R.id.item_custom_gift_card)!!
        var iv_close = itemView.findViewById<ImageView>(R.id.iv_close)!!
        var tv_count = itemView.findViewById<TextView>(R.id.tv_count)!!

    }

    interface RecycleViewItemClickListener {
        fun activateGiftCard(comingSoonItem: ActiveGCResponse.Gca)
    }


}