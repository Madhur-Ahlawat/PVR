package com.net.pvr.ui.giftCard.activateGiftCard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.giftCard.activateGiftCard.ExpiredGiftCardActivity
import com.net.pvr.ui.giftCard.response.ActiveGCResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show


class ActivateGiftCardAdapter(
    private var nowShowingList: ArrayList<ActiveGCResponse.Gca>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<ActivateGiftCardAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gift_card_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]

        if (cinemaItem.ci != null && cinemaItem.ci != ""){
            Glide.with(context)
                .load(cinemaItem.ci)
                .error(R.drawable.gift_card_placeholder)
                .placeholder(R.drawable.gift_card_placeholder)
                .into(holder.image)
        }else {
            Glide.with(context)
                .load(cinemaItem.gi)
                .error(R.drawable.gift_card_placeholder)
                .placeholder(R.drawable.gift_card_placeholder)
                .into(holder.image)
        }

//        holder.orderId.text =context.getString(R.string.order)+ cinemaItem.active

        if (context is ExpiredGiftCardActivity) {
            holder.iv_expired.show()
            holder.price.hide()
            holder.value.hide()
        }
        holder.tvresend.text = "View"
        if (cinemaItem.r != null)
            holder.giftedTo.text = Html.fromHtml(cinemaItem.r)
        if (cinemaItem.ta != null)
            holder.price.text = context.resources.getString(R.string.currency) + cinemaItem.ta.replace("Rs. ".toRegex(), "")
        if (cinemaItem.id != null)
            holder.orderId.text = "Card No: " + cinemaItem.gcn
        if (cinemaItem.d != null)
            holder.date.text = cinemaItem.dn.replace("Date:","") + " • " + cinemaItem.tn


    holder.itemView.setOnClickListener {
        listener.activateGiftCard(cinemaItem)
    }

//    holder.item_active_gift_card.setOnClickListener(
//    object : View.OnClickListener {
//        override fun onClick(v: View) {
//            if (context is ActiveGiftCardsActivity) {
//                (context as ActiveGiftCardsActivity).showDialog(data.getId().replace("ID:", ""))
//            }
//            if (context is ExpiredGiftCardActivity) {
//                (context as ExpiredGiftCardActivity).showDialog(data.getId().replace("ID:", ""))
//            }
//        }
//    })

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var giftedTo: TextView = view.findViewById(R.id.tvGift)
        var tvresend: TextView = view.findViewById(R.id.tvresend)
        var price: TextView = view.findViewById(R.id.tvValue)
        var date: TextView = view.findViewById(R.id.tvDate)
        var value: TextView = view.findViewById(R.id.tvValueTxt)
        var orderId: TextView = view.findViewById(R.id.orderId)
        var image: ImageView = view.findViewById(R.id.giftCardImage)
        var iv_expired: ImageView = view.findViewById(R.id.iv_expired)

    }

    interface RecycleViewItemClickListener {
        fun activateGiftCard(comingSoonItem: ActiveGCResponse.Gca)
    }


}