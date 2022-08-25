package com.net.pvr1.ui.movieDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.movieDetails.response.MovieDetailsResponse


class MusicVideoAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Crew.Role>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<MusicVideoAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_details_music, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        //title
        holder.title.text =cinemaItem.name
        //subTitle
        holder.subTitle.text =cinemaItem.role
        //moreDetails
        holder.play.setOnClickListener {
            listener.musicVideo(cinemaItem)
        }
        //Image
            Glide.with(context)
            .load(cinemaItem.poster)
            .error(R.drawable.app_icon)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView82)
        var subTitle: TextView = view.findViewById(R.id.textView83)
        var image: ImageView = view.findViewById(R.id.imageView33)
        var play: ImageView = view.findViewById(R.id.imageView34)
    }

    interface RecycleViewItemClickListener {
        fun musicVideo(comingSoonItem: MovieDetailsResponse.Mb.Crew.Role)
    }

}