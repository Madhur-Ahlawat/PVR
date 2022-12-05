package com.net.pvr1.ui.selectCity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R

class PopUpCityAdapter(
    private var selectCityList: ArrayList<String>,
    private var context: Context,
    private var listner: RecycleViewItemClickListener1,
) : RecyclerView.Adapter<PopUpCityAdapter.MyViewHolderSearchCity>() {

    var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSearchCity {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_dialog_city_item_layout, parent, false)
        return MyViewHolderSearchCity(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderSearchCity, position: Int) {
        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList

//        subCityName = holder.otherCityName

//        if (selectCityItemList==cityNameCC){
//            holder.otherCityName.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_1))
//        }else{
//
//        }

        holder.otherCityName.setOnClickListener {
            listner.onItemClickCityDialog(selectCityList, position)
        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderSearchCity(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
    }

    interface RecycleViewItemClickListener1 {
        fun onItemClickCityDialog(city: ArrayList<String>, position: Int)

    }


}