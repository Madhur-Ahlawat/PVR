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
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class TrailerAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Video>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<TrailerAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_trailer, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        //title
        holder.title.text =cinemaItem.caption
        //subTitle
        holder.subTitle.hide()
        holder.subTitle.text =cinemaItem.type

        //moreDetails
        holder.itemView.setOnClickListener {
            listener.trailerClick(cinemaItem)
        }
        if (cinemaItem.url!=""){
            holder.play.show()
        }else{
            holder.play.hide()
        }
        //Image
            Glide.with(context)
            .load(cinemaItem.thumbnail)
            .error(R.drawable.app_icon)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView37)
        var subTitle: TextView = view.findViewById(R.id.textView38)
        var image: ImageView = view.findViewById(R.id.imageView11)
        var play: ImageView = view.findViewById(R.id.imageView12)
    }

    interface RecycleViewItemClickListener {
        fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video)
    }

}