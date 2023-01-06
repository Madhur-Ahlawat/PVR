package com.net.pvr1.ui.movieDetails.nowShowing.adapter

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


class CastAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Cast>,
    private var context: Context,
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
        if (cinemaItem.character==""){
            holder.description.hide()
        }else{
            holder.description.show()
            holder.description.text =cinemaItem.character
        }

        //Image
            Glide.with(context)
            .load("https://"+cinemaItem.poster)
            .error(R.drawable.placeholder_vertical)
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



}