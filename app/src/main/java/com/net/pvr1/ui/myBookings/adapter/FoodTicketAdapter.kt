package com.net.pvr1.ui.myBookings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.utils.printLog


class FoodTicketAdapter(
    private var nowShowingList: List<FoodTicketResponse.Output.C>,
    private var context: Context
) :
    RecyclerView.Adapter<FoodTicketAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_food_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {

        //giftedTo
        holder.title.text = context.getString(R.string.upcomingBooking) + nowShowingList.size

//RecyclerView
        val gridLayout2 = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        val giftCardAdapter = FoodTicketChildAdapter(nowShowingList, context)
        holder.recyclerView.layoutManager = gridLayout2
        holder.recyclerView.adapter = giftCardAdapter
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView3)
        var recyclerView: RecyclerView = view.findViewById(R.id.recycler)
    }

}