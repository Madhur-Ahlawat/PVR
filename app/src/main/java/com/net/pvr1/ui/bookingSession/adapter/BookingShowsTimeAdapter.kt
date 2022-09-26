package com.net.pvr1.ui.bookingSession.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemCinemaDetailsShowTimeBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.seatLayout.SeatLayoutActivity


class BookingShowsTimeAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Cinema.Child.Sw.S>,
    private var context: Context
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
                //Language
                binding.textView96.text = this.st
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, SeatLayoutActivity::class.java)
                    intent.putExtra("transId","")
                    intent.putExtra("shows",nowShowingList)
                    intent.putExtra("ccId",this.cc)
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