package com.net.pvr1.ui.myBookings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse


class GiftCardAdapter(
    private var nowShowingList: List<GiftCardResponse.Output.Gc>,
    private var context: Context,
    private var listener: Direction
) :
    RecyclerView.Adapter<GiftCardAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gift_card_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]

        //giftedTo
        holder.giftedTo.text = cinemaItem.r
        //Price
        holder.price.text = cinemaItem.ta
        //DateTime
        holder.date.text = cinemaItem.d
        //DateTime
        holder.date.text = cinemaItem.d
        //OrderId
        holder.orderId.text =context.getString(R.string.order)+ cinemaItem.id

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var giftedTo: TextView = view.findViewById(R.id.tvGift)
        var price: TextView = view.findViewById(R.id.tvValue)
        var date: TextView = view.findViewById(R.id.tvDate)
        var orderId: TextView = view.findViewById(R.id.orderId)

    }

    interface Direction {
        fun resend(comingSoonItem: CinemaResponse.Output.C)
    }


}