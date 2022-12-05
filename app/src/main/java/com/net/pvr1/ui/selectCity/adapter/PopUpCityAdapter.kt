package com.net.pvr1.ui.selectCity.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.utils.printLog

class PopUpCityAdapter(
    private var selectCityList: ArrayList<String>,
    private var context: Context,
    private var listner: RecycleViewItemClickListener,
    private var cityName: String,
) : RecyclerView.Adapter<PopUpCityAdapter.MyViewHolderSearchCity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSearchCity {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_dialog_city_item_layout, parent, false)
        return MyViewHolderSearchCity(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolderSearchCity, position: Int) {
        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList

        if (cityName!="All"&& selectCityItemList==cityName){
            val bold: Typeface = context.resources.getFont(R.font.sf_pro_text_bold)
            holder.otherCityName.typeface = bold
        }else{
            val medium: Typeface = context.resources.getFont(R.font.sf_pro_text_medium)
            holder.otherCityName.typeface = medium
        }

        holder.otherCityName.setOnClickListener {
            listner.onItemClickCityDialog(selectCityItemList)
        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderSearchCity(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
    }

    interface RecycleViewItemClickListener {
        fun onItemClickCityDialog(city: String)
    }

}