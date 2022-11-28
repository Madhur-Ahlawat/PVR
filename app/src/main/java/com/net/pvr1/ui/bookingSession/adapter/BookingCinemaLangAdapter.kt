package com.net.pvr1.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.net.pvr1.databinding.ItemBookingCinemaLangBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse


class BookingCinemaLangAdapter(
    private var nowShowingList: List<BookingResponse.Output.Cinema.Child.Sw>,
    private var context: Context,
    private val ccid: String,
) :
    RecyclerView.Adapter<BookingCinemaLangAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingCinemaLangBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingCinemaLangBinding.inflate(
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
                binding.textView106.text = this.lng
//                //Recycler
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.FLEX_START
                val bookingShowsParentAdapter =
                    BookingShowsTimeAdapter(this.s!!, context, ccid)
                binding.recyclerView11.layoutManager = layoutManager
                binding.recyclerView11.adapter = bookingShowsParentAdapter
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

}