package com.net.pvr.ui.myBookings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.ui.myBookings.response.FoodTicketResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show


class FoodTicketFoodAdapter(
    private var nowShowingList: List<FoodTicketResponse.Output.P.F>,
    private val type: String,private val count:Int
) :
    RecyclerView.Adapter<FoodTicketFoodAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_food_food_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        println("list.size--->"+nowShowingList.size)

        if (type == "food") {
            holder.view.hide()
        } else {
            holder.view.show()
        }
        holder.title.text = cinemaItem.n
        holder.quant.text = "x "+cinemaItem.c.toString()

        if (cinemaItem.v == "true"){
            holder.imageView2.setImageResource(R.drawable.veg_ic)
        }else{
            holder.imageView2.setImageResource(R.drawable.nonveg_ic)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) count else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.textView22)
        var quant: TextView = view.findViewById(R.id.quant)
        var view: View = view.findViewById(R.id.view19)
        var imageView2: ImageView = view.findViewById(R.id.imageView2)
    }

}