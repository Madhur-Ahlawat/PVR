package com.net.pvr1.ui.home.adapter

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
import com.net.pvr1.utils.printLog

class HomeOfferAdapter(
    private var nowShowingList: List<OfferResponse.Output>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity
) :
    RecyclerView.Adapter<HomeOfferAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_offer_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        context.printLog("OfferSize--->${nowShowingList.size}")
        //Image
            Glide.with(context)
            .load(cinemaItem.i)
            .error(R.drawable.app_icon)
            .into(holder.image)

        //moreDetails
        holder.itemView.setOnClickListener {
            listener.offerClick(cinemaItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView94)
    }

    interface RecycleViewItemClickListenerCity {
        fun offerClick(comingSoonItem: OfferResponse.Output)
    }

}