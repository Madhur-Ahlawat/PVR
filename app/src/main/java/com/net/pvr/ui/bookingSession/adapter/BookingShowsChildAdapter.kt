package com.net.pvr.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.net.pvr.databinding.ItemBookingSessionCinemaLanguageBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse


class BookingShowsChildAdapter(
    private var nowShowingList: List<BookingResponse.Output.Cinema.Child>,
    private var context: Context,
) :
    RecyclerView.Adapter<BookingShowsChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingSessionCinemaLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language  .lng+this.lk
                binding.textView106.text = this.sws[0].lng + this.sws[0].lk
                //Recycler
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.FLEX_START

//                val bookingShowsParentAdapter =BookingShowsCinemaLanguageAdapter(this.sws, context, this.ccid)
//                binding.recyclerView11.layoutManager = layoutManager
//                binding.recyclerView11.adapter = bookingShowsParentAdapter
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

}