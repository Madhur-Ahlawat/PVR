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
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide


class TrailerTrsAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Trs>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<TrailerTrsAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_trailer, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        //title
        holder.title.text =cinemaItem.t
        //subTitle
        holder.subTitle.hide()
        holder.subTitle.text =cinemaItem.d

        //moreDetails
        holder.itemView.setOnClickListener {
            listener.trailerTrsClick(cinemaItem)
        }
        val videoId = Constant().extractYoutubeId(cinemaItem.u)
        val imageUrl = "http://img.youtube.com/vi/" + videoId.toString() + "/mqdefault.jpg" //

        //Image
            Glide.with(context)
            .load(imageUrl)
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
        fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs)
    }

}