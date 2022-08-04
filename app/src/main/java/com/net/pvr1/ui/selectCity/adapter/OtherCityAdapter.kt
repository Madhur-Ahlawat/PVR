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

class OtherCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Ot>,
    private var context: Context) :
    RecyclerView.Adapter<OtherCityAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_other_city_item_layout, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {

        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList.name

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
    }

}