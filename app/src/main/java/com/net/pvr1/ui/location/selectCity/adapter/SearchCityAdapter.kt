package com.net.pvr1.ui.location.selectCity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

@Suppress("SENSELESS_COMPARISON")
class SearchCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Ot>,
    private var listner: RecycleViewItemClickListener,
    private var cityName: String
) : RecyclerView.Adapter<SearchCityAdapter.MyViewHolderSearchCity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSearchCity {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_other_city_item_layout, parent, false)
        return MyViewHolderSearchCity(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderSearchCity, position: Int) {
        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList.name

        println("subcities--->${selectCityItemList.subcities}")
        if (selectCityItemList.subcities!=""){
            holder.imageView.show()
        }else{
            holder.imageView.hide()
        }

        if (selectCityItemList.name == cityName) {
            holder.layout.setBackgroundResource(R.drawable.city_selectd_bg)
        } else {
            holder.layout.setBackgroundResource(R.drawable.city_un_selectd_bg)
        }

        holder.itemView.setOnClickListener {
            listner.onItemClickCitySearch(selectCityList, position)
        }
    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderSearchCity(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
        var imageView: ImageView = view.findViewById(R.id.imageView85)
        var layout: ConstraintLayout = view.findViewById(R.id.constraintLayout159)
    }

    interface RecycleViewItemClickListener {
        fun onItemClickCitySearch(city: ArrayList<SelectCityResponse.Output.Ot>, position: Int)

    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<SelectCityResponse.Output.Ot>) {
        // below line is to add our filtered
        selectCityList = filterList
        notifyDataSetChanged()
    }


}