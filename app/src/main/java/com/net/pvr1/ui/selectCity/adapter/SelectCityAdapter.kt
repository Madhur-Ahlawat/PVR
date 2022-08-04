package com.net.pvr1.ui.selectCity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.utils.printLog

class SelectCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Pc>,
    private var context: Context) :
    RecyclerView.Adapter<SelectCityAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_city_item_layout, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {

        val selectCityItemList = selectCityList[position]
        holder.cityName.text = selectCityItemList.name
        Glide.with(context).load(selectCityItemList.image).error(R.drawable.app_icon).into(holder.imageSelectCity)

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var cityName: TextView = view.findViewById(R.id.cityName)
        var imageSelectCity: ImageView = view.findViewById(R.id.imageSelectCity)
    }

}