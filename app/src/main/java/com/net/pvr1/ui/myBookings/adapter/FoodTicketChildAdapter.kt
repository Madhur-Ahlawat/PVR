package com.net.pvr1.ui.myBookings.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.ticketConfirmation.TicketConfirmationActivity
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class FoodTicketChildAdapter(
    private var nowShowingList: List<FoodTicketResponse.Output.C>,
    private var context: Context
) :
    RecyclerView.Adapter<FoodTicketChildAdapter.MyViewHolderNowShowing>() {

    private var type=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_food_child_item, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val cinemaItem = nowShowingList[position]
        //Image
        Glide.with(context)
            .load(cinemaItem.im)
            .error(R.drawable.app_icon)
            .into(holder.image)

        //title
        holder.title.text = cinemaItem.m
        //Language
        holder.language.text = cinemaItem.cen + " " + cinemaItem.st + " " + cinemaItem.lg
        //time
        holder.timeDate.text = cinemaItem.bd
        //location
        holder.location.text = cinemaItem.c
        //OrderId
        holder.orderId.text = context.getString(R.string.orderId) + cinemaItem.bi

        //OnlyFood
        //Time
        holder.time.text = cinemaItem.bd
        //Location
        holder.locationFood.text = cinemaItem.c

        if (cinemaItem.is_only_fd) {
            holder.cinemaWithFood.hide()
            holder.onlyFood.show()
//Food Item
            val gridLayout2 = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            val foodTicketFoodAdapter = FoodTicketFoodAdapter(cinemaItem.f,"food")
            holder.recyclerView.layoutManager = gridLayout2
            holder.recyclerView.adapter = foodTicketFoodAdapter
        } else {
            holder.onlyFood.hide()
            holder.cinemaWithFood.show()

////Food Item
//            val gridLayout2 = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
//            val foodTicketFoodAdapter = FoodTicketFoodAdapter(cinemaItem.f,"cinema")
//            holder.recyclerView.layoutManager = gridLayout2
//            holder.recyclerView.adapter = foodTicketFoodAdapter

        }


        holder.itemView.setOnClickListener {
            type = if (cinemaItem.is_only_fd){
                "FOOD"
            }else{
                "BOOKING"

            }
            val intent = Intent( context,TicketConfirmationActivity::class.java)
            intent.putExtra("bookingId",cinemaItem.bi)
            intent.putExtra("type","myBooking")
            intent.putExtra("bookType",type)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView3)
        var title: TextView = view.findViewById(R.id.textView16)
        var language: TextView = view.findViewById(R.id.textView17)
        var timeDate: TextView = view.findViewById(R.id.textView18)
        var location: TextView = view.findViewById(R.id.textView19)
        var orderId: TextView = view.findViewById(R.id.orderId)
        var time: TextView = view.findViewById(R.id.time)
        var locationFood: TextView = view.findViewById(R.id.textView23)
        var cinemaWithFood: ConstraintLayout = view.findViewById(R.id.movieWithFood)
        var onlyFood: ConstraintLayout = view.findViewById(R.id.onlyFood)
        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerFoodChild)
    }

}