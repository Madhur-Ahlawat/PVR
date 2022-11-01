package com.net.pvr1.ui.bookingSession.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.seatLayout.SeatLayoutActivity
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.SESSION_ID
import com.net.pvr1.utils.printLog


@Suppress("DEPRECATION")
class BookingShowsTimeAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Cinema.Child.Sw.S>,
    private var context: Context,
    private var ccid: String
) :
    RecyclerView.Adapter<BookingShowsTimeAdapter.ViewHolder>() {
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
                context.printLog("sessionId--->${this.cc}")
                val colorCode = "#" + this.cc

                binding.textView96.setTextColor(Color.parseColor(colorCode))
                binding.imageView48.setColorFilter(Color.parseColor(colorCode))
                binding.imageView49.setColorFilter(Color.parseColor(colorCode))

                val alpha = 10 //between 0-255
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor(colorCode), alpha)
                val alphaNew2= alphaColor.toString().substring(0, alphaColor.toString().length - 1)
                val alphaNew3= "#$alphaNew2"
                context.printLog("colorCode--->${alphaNew2}")

                val gd = GradientDrawable()
                gd.setColor(Color.parseColor(alphaNew3))
                gd.cornerRadius = 10f
                gd.setStroke(2, Color.parseColor(colorCode))
                binding.cardView10.setBackgroundDrawable(gd)

                //Language
                binding.textView96.text = this.st
                SESSION_ID = this.sid.toString()
                CINEMA_ID = ccid
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, SeatLayoutActivity::class.java)
                    intent.putExtra("transId", "")
                    intent.putExtra("sessionId", this.sid.toString())
                    intent.putExtra("cinemaId", ccid)
                    intent.putExtra("shows", nowShowingList)
                    intent.putExtra("ccId", this.cc)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun languageClick(comingSoonItem: BookingResponse.Output.Cinema)
    }

}