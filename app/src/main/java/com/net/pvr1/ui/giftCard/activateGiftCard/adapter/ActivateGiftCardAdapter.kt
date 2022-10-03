package com.net.pvr1.ui.giftCard.activateGiftCard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.giftCard.adapter.GiftCardMainAdapter
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.utils.hide


class ActivateGiftCardAdapter(
    private var nowShowingList: List<com.net.pvr1.ui.giftCard.response.GiftCardResponse.Output.GiftCard>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<ActivateGiftCardAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gift_card_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]

        holder.value.hide()
        holder.price.hide()

        //Image
        Glide.with(context)
            .load(cinemaItem.imageUrl)
            .error(R.drawable.app_icon)
            .into(holder.image)


        //giftedTo
        holder.giftedTo.text = cinemaItem.channel
        //Price
//        holder.price.text = cinemaItem.ta
        //DateTime
        holder.date.text = cinemaItem.allowedCount.toString()
        //OrderId
        holder.orderId.text =context.getString(R.string.order)+ cinemaItem.active

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var giftedTo: TextView = view.findViewById(R.id.tvGift)
        var price: TextView = view.findViewById(R.id.tvValue)
        var date: TextView = view.findViewById(R.id.tvDate)
        var value: TextView = view.findViewById(R.id.tvValueTxt)
        var orderId: TextView = view.findViewById(R.id.orderId)
        var image: ImageView = view.findViewById(R.id.giftCardImage)

    }

    interface RecycleViewItemClickListener {
        fun activateGiftCard(comingSoonItem: CinemaResponse.Output.C)
    }


}