package com.net.pvr.ui.home.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.home.response.HomeResponse


class HomePromotionAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Mfi>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<HomePromotionAdapter.MyViewHolderNowShowing>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_promotion, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context)
            .load(comingSoonItem.url)
            .error(R.drawable.placeholder_horizental)
            .into(holder.image)

        holder.image.setOnClickListener {
            listener.onPromotionClick(comingSoonItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.sliderImg)
    }

    interface RecycleViewItemClickListener {
        fun onPromotionClick(comingSoonItem: HomeResponse.Mfi)
    }

}