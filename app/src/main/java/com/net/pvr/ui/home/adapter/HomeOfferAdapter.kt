package com.net.pvr.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr.utils.printLog

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
        printLog("OfferSize--->${nowShowingList.size}")
        //Image
            Glide.with(context)
            .load(cinemaItem.i)
            .error(R.drawable.placeholder_horizental)
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