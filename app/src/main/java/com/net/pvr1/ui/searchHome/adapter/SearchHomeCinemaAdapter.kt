package com.net.pvr1.ui.searchHome.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.searchHome.response.HomeSearchResponse
import kotlin.collections.ArrayList

class SearchHomeCinemaAdapter(
    private var selectCityList: ArrayList<HomeSearchResponse.Output.T>,
    private var context: Context,
    private var listner: RecycleViewItemClickListenerCity
) :
    RecyclerView.Adapter<SearchHomeCinemaAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_search_cinema, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {

        val selectCityItemList = selectCityList[position]
        holder.title.text = selectCityItemList.n
        holder.address.text = selectCityItemList.ad
        holder.distance.text = selectCityItemList.length

        holder.itemView.setOnClickListener {
            listner.onSearchCinema(selectCityList)

        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView50)
        var address: TextView = view.findViewById(R.id.textView51)
        var distance: TextView = view.findViewById(R.id.textView49)
    }

    interface RecycleViewItemClickListenerCity {
        fun onSearchCinema(city: ArrayList<HomeSearchResponse.Output.T>)
    }
}
