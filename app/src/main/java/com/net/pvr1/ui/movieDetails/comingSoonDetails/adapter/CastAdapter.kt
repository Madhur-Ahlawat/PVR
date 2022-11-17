package com.net.pvr1.ui.movieDetails.comingSoonDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse


class CastAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Cast>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<CastAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_details_cast, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        //title
        holder.title.isSelected = true
        holder.title.text =cinemaItem.name
        //subTitle
        holder.description.text =cinemaItem.character
        //Click
        holder.itemView.setOnClickListener {
            listener.castClick(cinemaItem)
        }
        //Image
            Glide.with(context)
            .load("https://"+cinemaItem.poster)
            .error(R.drawable.app_icon)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView54)
        var description: TextView = view.findViewById(R.id.textView80)
        var image: ImageView = view.findViewById(R.id.imageView30)
    }

    interface RecycleViewItemClickListener {
        fun castClick(comingSoonItem: MovieDetailsResponse.Mb.Cast)
    }

}