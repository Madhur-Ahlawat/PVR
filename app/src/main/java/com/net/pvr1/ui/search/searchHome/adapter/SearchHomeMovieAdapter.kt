package com.net.pvr1.ui.search.searchHome.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse

class SearchHomeMovieAdapter(
    private var selectCityList: List<HomeSearchResponse.Output.M>,
    private var context: Context,
    private var listner: RecycleViewItemClickListenerCity
) :
    RecyclerView.Adapter<SearchHomeMovieAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_search_movie, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val selectCityItemList = selectCityList[position]
        holder.title.text = selectCityItemList.n
        holder.timeCategory.text = selectCityItemList.length + " | " + selectCityItemList.genre
        holder.language.text = selectCityItemList.lng

        //Image
        Glide.with(context)
            .load(selectCityItemList.im)
            .error(R.drawable.app_icon)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            listner.onSearchMovie(selectCityItemList)
        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView25)
        var title: TextView = view.findViewById(R.id.textView47)
        var timeCategory: TextView = view.findViewById(R.id.textView48)
        var language: TextView = view.findViewById(R.id.textView49)
    }

    interface RecycleViewItemClickListenerCity {
        fun onSearchMovie(selectCityList: HomeSearchResponse.Output.M)
    }

}
