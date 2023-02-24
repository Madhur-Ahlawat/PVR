package com.net.pvr1.ui.home.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class HomeCinemaCategoryAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Mfi>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<HomeCinemaCategoryAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_image, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        if (nowShowingList.size == 1){
            holder.cardTag.hide()
            holder.imageCatNew.show()
            Glide.with(context)
                .load(comingSoonItem.nurl)
                .placeholder(R.drawable.format_placeholder)
                .error(R.drawable.format_placeholder)
                .into(holder.imageCatNew)
        }else{
            holder.cardTag.show()
            holder.imageCatNew.hide()
            Glide.with(context)
                .load(comingSoonItem.nurl)
                .placeholder(R.drawable.format_placeholder)
                .error(R.drawable.format_placeholder)
                .into(holder.imageCat)
        }

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(comingSoonItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var cardTag: CardView = view.findViewById(R.id.cardTag)
        var imageCat: ImageView = view.findViewById(R.id.ivTag)
        var imageCatNew: ImageView = view.findViewById(R.id.ivTagNew)

    }

    interface RecycleViewItemClickListener {
        fun onCategoryClick(comingSoonItem: HomeResponse.Mfi)
    }

}