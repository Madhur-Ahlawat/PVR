package com.net.pvr1.ui.home.fragment.cinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse


class CinemaAdapter(
    private var nowShowingList: List<CinemaResponse.Output.C>,
    private var context: Context,
    private var listener: Direction,
    private var location: Location,
) :
    RecyclerView.Adapter<CinemaAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cinema_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]

        //title
        holder.title.text = cinemaItem.n
        //type
        holder.multipleCinema.text = cinemaItem.mc
        //address
        holder.cinemaLocation.text = cinemaItem.ad
        //Shows
        holder.show.text = cinemaItem.sc.toString()+" "+context.getString(R.string.shows)
        //Distance
        holder.distance.text = cinemaItem.d
        //Image
        Glide.with(context)
            .load(cinemaItem.imob)
            .error(R.drawable.app_icon)
            .into(holder.image)

        //Call Child Adapter
        val gridLayout2 = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        val comingSoonMovieAdapter = CinemaChildAdapter(cinemaItem.m,context, this)
        holder.movieList.layoutManager = gridLayout2
        holder.movieList.adapter = comingSoonMovieAdapter

        //Direction
        holder.direction.setOnClickListener {
            listener.onDirectionClick(cinemaItem)
        }
        //Kilometer
        holder.kiloMeter.setOnClickListener {
            location.onLocationClick(cinemaItem)
        }


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.cinemaName)
        var image: ImageView = view.findViewById(R.id.cinemaImg)
        var checkFev: CheckBox = view.findViewById(R.id.cbFav)
        var multipleCinema: TextView = view.findViewById(R.id.multipleCinema)
        var cinemaLocation: TextView = view.findViewById(R.id.cinemaLocation)
        var imageLocation: ImageView = view.findViewById(R.id.ivLocation)
        var parking: LinearLayout = view.findViewById(R.id.parking)
        var food: LinearLayout = view.findViewById(R.id.food)
        var metro: LinearLayout = view.findViewById(R.id.metro)
        var kiloMeter: LinearLayout = view.findViewById(R.id.kiloMeter)
        var direction: LinearLayout = view.findViewById(R.id.llLocation)
        var show: TextView = view.findViewById(R.id.tvShows)
        var distance: TextView = view.findViewById(R.id.tvDistance)
        var movieList: RecyclerView = view.findViewById(R.id.llMovieList)
    }

    interface Direction {
        fun onDirectionClick(comingSoonItem: CinemaResponse.Output.C)
    }
    interface Location {
        fun onLocationClick(comingSoonItem: CinemaResponse.Output.C)
    }

}