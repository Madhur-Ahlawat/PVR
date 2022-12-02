package com.net.pvr1.ui.selectCity.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.selectCity.response.SelectCityResponse

class OtherCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Ot>,
    private var selectCityListCC: SelectCityResponse.Output.Cc,
    private var mContext: Context,
    private var listner: RecycleViewItemClickListenerCity
) : RecyclerView.Adapter<OtherCityAdapter.MyViewHolderNowShowing>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_dialog_city_item_layout, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList.name

        if (selectCityListCC.name == selectCityItemList.name) {
            holder.otherCityName.paintFlags =
                holder.otherCityName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        } else {
            holder.otherCityName.paintFlags =
                holder.otherCityName.paintFlags
        }

        holder.itemView.setOnClickListener {
            listner.onItemClickCityOtherCity(selectCityList, position)
            holder.otherCityName.paintFlags =
                holder.otherCityName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
    }

    interface RecycleViewItemClickListenerCity {
        fun onItemClickCityOtherCity(city: ArrayList<SelectCityResponse.Output.Ot>, position: Int)
    }

}
