package com.net.pvr.ui.home.fragment.home.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.more.offer.response.OfferResponse
import com.net.pvr.utils.Constant

class HomeOfferAdapter(
    private var nowShowingList: List<OfferResponse.Offer>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListenerCity
) :
    RecyclerView.Adapter<HomeOfferAdapter.MyViewHolderNowShowing>() {
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_offer, parent, false)
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]

        //Image
            Glide.with(context)
            .load(cinemaItem.imageUrl)
            .error(R.drawable.placeholder_horizental)
            .into(holder.image)

        if (nowShowingList.size > 1) {
            if (nowShowingList.size == 2) {
                val itemWidth = ((screenWidth - 0) / 1.18f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                if (position == 0) {
                    layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                    layoutParams.rightMargin = Constant().convertDpToPixel(1f, context)
                } else {
                    layoutParams.leftMargin = Constant().convertDpToPixel(8f, context)
                    layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                }
                holder.itemView.layoutParams = layoutParams
            } else if (position == 0) {
                val itemWidth = ((screenWidth - 0) / 1.18f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            } else if (position == nowShowingList.size - 1) {
                val itemWidth = ((screenWidth - 0) / 1.18f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            } else {
                val itemWidth = ((screenWidth - 0) / 1.18f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            }
        } else {
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.leftMargin = Constant().convertDpToPixel(13F, context)
            layoutParams.rightMargin = Constant().convertDpToPixel(13F, context)
            holder.itemView.layoutParams = layoutParams
        }

        //moreDetails
        holder.itemView.setOnClickListener {
            listener.offerClick(cinemaItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView11)
    }

    interface RecycleViewItemClickListenerCity {
        fun offerClick(comingSoonItem: OfferResponse.Offer)
    }

}