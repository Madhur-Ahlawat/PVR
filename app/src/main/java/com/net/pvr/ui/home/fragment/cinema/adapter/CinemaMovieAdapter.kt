package com.net.pvr.ui.home.fragment.cinema.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr.ui.movieDetails.nowShowing.NowShowingMovieDetailsActivity


class CinemaMovieAdapter(
    private var nowShowingList: List<CinemaResponse.Output.C.M>,
    private var context: Context,
    private var listener: CinemaResponse.Output.C,
) :
    RecyclerView.Adapter<CinemaMovieAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cinema_child_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context)
            .load(comingSoonItem.i)
            .error(R.drawable.placeholder_vertical)
            .into(holder.image)

        //click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NowShowingMovieDetailsActivity::class.java)
            intent.putExtra("mid", comingSoonItem.m)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageLandingScreen)

    }
}