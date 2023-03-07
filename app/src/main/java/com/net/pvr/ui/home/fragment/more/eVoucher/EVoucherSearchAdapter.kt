package com.net.pvr.ui.home.fragment.more.eVoucher

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.utils.printLog


class EVoucherSearchAdapter(
    private var selectCityList: ArrayList<VoucherListResponse.Output.Ev>,
    private var context: Context,
    private var listner: RecycleViewItemClickListener
) : RecyclerView.Adapter<EVoucherSearchAdapter.MyViewHolderSearchCity>() {

    var rowIndex=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderSearchCity {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_dialog_city_item_layout, parent, false)
        return MyViewHolderSearchCity(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolderSearchCity, position: Int) {
        val selectCityItemList = selectCityList[position]

        holder.otherCityName.text = selectCityItemList.voucherCategory

        if (rowIndex==position){
            val bold: Typeface = context.resources.getFont(R.font.sf_pro_text_bold)
            holder.otherCityName.typeface = bold
        }else{
            val medium: Typeface = context.resources.getFont(R.font.sf_pro_text_medium)
            holder.otherCityName.typeface = medium
        }

        holder.otherCityName.setOnClickListener {
            rowIndex= position
            listner.onItemClick(selectCityItemList)
        }

    }

    override fun getItemCount(): Int {
        return  selectCityList.size
    }

    class MyViewHolderSearchCity(view: View) : RecyclerView.ViewHolder(view) {
        var otherCityName: TextView = view.findViewById(R.id.otherCityName)
    }

    interface RecycleViewItemClickListener {
        fun onItemClick(city: VoucherListResponse.Output.Ev)
    }

}