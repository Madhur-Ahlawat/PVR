package com.net.pvr1.ui.home.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse


class HomeSliderAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Mv>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<HomeSliderAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_slider, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context)
            .load(comingSoonItem.i)
            .error(R.drawable.app_icon)
            .into(holder.image)
        val pos = position + 1
        holder.position.text = pos.toString() + "/" + nowShowingList.size

        holder.time.text = comingSoonItem.lc
        holder.book.setOnClickListener {
            listener.onSliderBookClick(comingSoonItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView13)
        var position: TextView = view.findViewById(R.id.textView39)
        var time: TextView = view.findViewById(R.id.textView40)
        var book: TextView = view.findViewById(R.id.textView41)

    }

    interface RecycleViewItemClickListener {
        fun onSliderBookClick(comingSoonItem: HomeResponse.Mv)
    }

}