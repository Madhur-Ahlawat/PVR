package com.net.pvr1.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemBookingSessionCinemaMoviesBinding
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.utils.Constant


class BookingTheatreAdapter(
    private var nowShowingList: List<BookingTheatreResponse.Output.M>,
    private var context: Context,
    var listner: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<BookingTheatreAdapter.ViewHolder>() {
    private var rowIndex=0
    inner class ViewHolder(val binding: ItemBookingSessionCinemaMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaMoviesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (position == 0) {
                    Constant().setMargins(holder.itemView,60,0,0,0)
                }

                //Image
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.placeholder_vertical)
                    .into(binding.imageView57)

                //click
                holder.itemView.setOnClickListener {
                    rowIndex= position
                    listner.theatreClick(this) }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun theatreClick(comingSoonItem: BookingTheatreResponse.Output.M)
    }

}