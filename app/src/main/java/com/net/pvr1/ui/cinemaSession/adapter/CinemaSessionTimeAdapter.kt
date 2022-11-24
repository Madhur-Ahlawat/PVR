package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.seatLayout.SeatLayoutActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.printLog


class CinemaSessionTimeAdapter(
    private var nowShowingList: ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>,
    private var context: Context) :
    RecyclerView.Adapter<CinemaSessionTimeAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaDetailsShowTimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsShowTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language
                binding.textView96.text = this.st
                val colorCode = "#" + this.cc

                binding.textView96.setTextColor(Color.parseColor(colorCode))
                binding.imageView48.setColorFilter(Color.parseColor(colorCode))
                binding.imageView49.setColorFilter(Color.parseColor(colorCode))


                val alpha = 10 //between 0-255
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor(colorCode), alpha)
                val alphaNew2 = alphaColor.toString().substring(0, alphaColor.toString().length - 1)
                val alphaNew3 = "#$alphaNew2"
                context.printLog("colorCode--->${alphaNew2}")
                val gd = GradientDrawable()
                gd.setColor(Color.parseColor(alphaNew3))
                gd.cornerRadius = 10f
                gd.setStroke(2, Color.parseColor(colorCode))
                binding.cardView10.setBackgroundDrawable(gd)

                Constant.SESSION_ID = this.sid.toString()
                Constant.CINEMA_ID = cc

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, SeatLayoutActivity::class.java)
                    intent.putExtra("from", "cinema")
                    intent.putExtra("CinemaShows", nowShowingList)
                    context.startActivity(intent)

                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }



}