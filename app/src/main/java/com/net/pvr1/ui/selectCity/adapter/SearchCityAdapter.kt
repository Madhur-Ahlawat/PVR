package com.net.pvr1.ui.selectCity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import java.util.*
import kotlin.collections.ArrayList

class SearchCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Ot>,
    var cityList: ArrayList<Any>,
    val filteredList: ArrayList<Any>?,
    private var context: Context,
    var listner : RecycleViewItemClickListener) :
    RecyclerView.Adapter<SearchCityAdapter.MyViewHolderSearchCity>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSearchCity {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_other_city_item_layout, parent, false)
        return MyViewHolderSearchCity(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderSearchCity, position: Int) {

        val selectCityItemList = selectCityList[position]
        holder.otherCityName.text = selectCityItemList.name

        holder.otherCityName.setOnClickListener {
            listner.onItemClickCitySearch(selectCityList, position)
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

    override fun getFilter(): Filter {
        return UserFilter(this, cityList, selectCityList)
    }

    private class UserFilter(
        private val adapter: SearchCityAdapter,
        var originalList: ArrayList<Any>,
        var otList: ArrayList<SelectCityResponse.Output.Ot>
    ) : Filter() {

        val filteredList = ArrayList<Any>()
        override fun performFiltering(constraint: CharSequence): FilterResults {
            filteredList.clear()
            val results = FilterResults()
            if (constraint.isEmpty()) {
                filteredList.addAll(originalList)
            } else {
                for (obj in otList) {
                    if (obj is SelectCityResponse.Output.Ot) {
                        val city = obj
                        if (city.name.lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ) filteredList.add(city)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            adapter.filteredList!!.clear()
            if (constraint==""){
                adapter.filteredList.addAll(originalList)
            }else{
                adapter.filteredList.addAll(results?.values as (java.util.ArrayList<*>))
            }
            println("constraint${filteredList+originalList.size}")
            adapter.notifyDataSetChanged()
        }

    }


}