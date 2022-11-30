package com.net.pvr1.ui.selectCity.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


@Suppress("DEPRECATION")
class SelectCityAdapter(
    private var selectCityList: ArrayList<SelectCityResponse.Output.Pc>,
    private var context: Activity,
    var listner: RecycleViewItemClickListenerSelectCity ) :
    RecyclerView.Adapter<SelectCityAdapter.MyViewHolderNowShowing>() {
    private var rowIndex = 0
    private var width = 0
    private var height = 0
    private var setWidth = 0
    private var setHeight = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_city_item_layout, parent, false)
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: MyViewHolderNowShowing,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val selectCityItemList = selectCityList[position]
        setWidth = width / 2 - 5
        setHeight = height / 2 - 60
//        holder.constraintLayout.layoutParams =
//            LinearLayout.LayoutParams(setWidth, setHeight)
        holder.cityName.text = selectCityItemList.name
        //Image
        Glide.with(context)
            .load(selectCityItemList.image)
            .error(R.drawable.app_icon)
            .into(holder.imageSelectCity)

        if (rowIndex == position) {
            holder.clickOhk.show()
        } else {
            holder.clickOhk.hide()
        }
        holder.itemView.setOnClickListener {
            rowIndex = position
            listner.onItemClickCityImgCity(selectCityList, position)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var cityName: TextView = view.findViewById(R.id.cityName)
        var imageSelectCity: ImageView = view.findViewById(R.id.imageView36)
        var clickOhk: ImageView = view.findViewById(R.id.imageView37)
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintLayout13)
    }

    interface RecycleViewItemClickListenerSelectCity {
        fun onItemClickCityImgCity(city: ArrayList<SelectCityResponse.Output.Pc>, position: Int)
    }

}