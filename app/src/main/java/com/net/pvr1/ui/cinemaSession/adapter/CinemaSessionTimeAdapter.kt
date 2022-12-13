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
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


@Suppress("DEPRECATION")
class CinemaSessionTimeAdapter(
    private var nowShowingList: ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>,
    private var context: Context,
    private  var cinemaId: String?
) :
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

                val alpha = 10
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor(colorCode), alpha)
                val alphaNew2 = alphaColor.toString().substring(0, alphaColor.toString().length - 1)
                val alphaNew3 = "#$alphaNew2"
                val gd = GradientDrawable()
                gd.setColor(Color.parseColor(alphaNew3))
                gd.cornerRadius = 10f
                gd.setStroke(2, Color.parseColor(colorCode))
                binding.cardView10.setBackgroundDrawable(gd)

                //handicap
                if (this.hc){
                    binding.constraintLayout133.show()
                }else{
                    binding.constraintLayout133.hide()
                }

                Constant.SESSION_ID = this.sid.toString()
                Constant.CINEMA_ID = cinemaId.toString()

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, SeatLayoutActivity::class.java)
                    intent.putExtra("from", "cinema")
                    intent.putExtra("skip", "true")
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