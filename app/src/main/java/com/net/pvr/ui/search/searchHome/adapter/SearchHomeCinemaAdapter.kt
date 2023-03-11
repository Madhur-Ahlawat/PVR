package com.net.pvr.ui.search.searchHome.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

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
        holder.title.isSelected = true

        if (selectCityItemList.desc!= null && selectCityItemList.desc!= ""){
            holder.cinemaLocation.show()
            holder.cinemaLocation.text = selectCityItemList.desc
        } else {
            holder.cinemaLocation.hide()
        }

        holder.itemView.setOnClickListener {
            listner.onSearchCinema(selectCityItemList)
        }
        holder.direction.setOnClickListener {
            listner.onSearchCinemaDirection(selectCityItemList)

        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView50)
        var address: TextView = view.findViewById(R.id.textView51)
        var distance: TextView = view.findViewById(R.id.textView53)
        var cinemaLocation: TextView = view.findViewById(R.id.cinemaLocation)
        var direction: ImageView = view.findViewById(R.id.direction)
    }

    interface RecycleViewItemClickListenerCity {
        fun onSearchCinema(selectCityItemList: HomeSearchResponse.Output.T)
        fun onSearchCinemaDirection(selectCityItemList: HomeSearchResponse.Output.T)
    }

    fun filterCinemaList(filterList: ArrayList<HomeSearchResponse.Output.T>) {
        // below line is to add our filtered
        selectCityList = filterList
        notifyDataSetChanged()
    }

}
