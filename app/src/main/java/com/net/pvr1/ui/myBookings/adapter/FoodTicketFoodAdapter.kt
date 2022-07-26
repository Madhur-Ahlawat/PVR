package com.net.pvr1.ui.myBookings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class FoodTicketFoodAdapter(
    private var nowShowingList: List<FoodTicketResponse.Output.P.F>,
    private val type: String
) :
    RecyclerView.Adapter<FoodTicketFoodAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_food_food_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        if (type == "food") {
            holder.view.hide()
        } else {
            holder.view.show()
        }
        holder.title.text = cinemaItem.n
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView22)
        var view: View = view.findViewById(R.id.view19)
    }

}