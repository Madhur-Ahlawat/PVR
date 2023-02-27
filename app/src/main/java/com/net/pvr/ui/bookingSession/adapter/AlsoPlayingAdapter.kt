package com.net.pvr.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.ItemBookingSessionCinemaMoviesBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse


class AlsoPlayingAdapter(
    private var nowShowingList: List<BookingResponse.Output.Dy>,
    private var context: Context,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<AlsoPlayingAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingSessionCinemaMoviesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
//                Glide.with(context)
//                    .load(image_url)
//                    .into(binding.imageView57)

                //click
                holder.itemView.setOnClickListener {
                    notifyDataSetChanged()
                    listener.alsoPlaying(this)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun alsoPlaying(comingSoonItem: BookingResponse.Output.Dy)
    }

}