package com.net.pvr1.ui.offer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.offer.response.OfferResponse


class OfferAdapter(
    private var nowShowingList: List<OfferResponse.Output>,
    private var context: Context,
    private var listener: Direction,
) :
    RecyclerView.Adapter<OfferAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_offer_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        //title
        holder.title.text =cinemaItem.t
        //subTitle
        holder.subTitle.text =cinemaItem.c
        //valid
        holder.validTill.text =context.getString(R.string.validTill)+cinemaItem.vt
        //moreDetails
        holder.moreDetails.setOnClickListener {
            listener.offerClick(cinemaItem)
        }
        //Image
            Glide.with(context)
            .load(cinemaItem.i)
            .error(R.drawable.app_icon)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tvOfferTitle)
        var subTitle: TextView = view.findViewById(R.id.tvOfferSubTitle)
        var validTill: TextView = view.findViewById(R.id.valid)
        var moreDetails: TextView = view.findViewById(R.id.moreDetails)
        var image: ImageView = view.findViewById(R.id.offerImg)
    }

    interface Direction {
        fun offerClick(comingSoonItem: OfferResponse.Output)
    }

}