package com.net.pvr1.ui.location.selectCity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.location.selectCity.response.SelectCityResponse

@Suppress("SENSELESS_COMPARISON")
class SearchCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Ot>,
    private var selectCityListCC: SelectCityResponse.Output,
    var context: Context,
    var listner: RecycleViewItemClickListener
) : RecyclerView.Adapter<SearchCityAdapter.MyViewHolderSearchCity>() {

    var mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSearchCity {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_other_city_item_layout, parent, false)
        return MyViewHolderSearchCity(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderSearchCity, position: Int) {
        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList.name

//        if (selectCityListCC.cc != null){
//            if (selectCityListCC.cc.name == selectCityItemList.name){
//                holder.otherCityName.paintFlags =
//                    holder.otherCityName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
//            }else{
//                holder.otherCityName.paintFlags
//            }
//        }

        holder.otherCityName.setOnClickListener {
            listner.onItemClickCitySearch(selectCityList, position)

//            holder.otherCityName.paintFlags =
//                holder.otherCityName.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderSearchCity(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
    }

    interface RecycleViewItemClickListener {
        fun onItemClickCitySearch(city: ArrayList<SelectCityResponse.Output.Ot>, position: Int)

    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<SelectCityResponse.Output.Ot>) {
        // below line is to add our filtered
        selectCityList = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }


}