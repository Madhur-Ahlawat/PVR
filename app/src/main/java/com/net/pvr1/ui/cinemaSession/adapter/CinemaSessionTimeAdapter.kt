package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse


class CinemaSessionTimeAdapter(
    private var nowShowingList: List<CinemaSessionResponse.Child.Mv.Ml.S>,
    private var context: Context,
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