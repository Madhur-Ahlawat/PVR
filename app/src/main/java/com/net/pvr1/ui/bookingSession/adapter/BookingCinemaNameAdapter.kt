package com.net.pvr1.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemBookingSessionCinemaLanguageBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.printLog
import com.net.pvr1.utils.show
import com.net.pvr1.utils.toast

class BookingCinemaNameAdapter(
    private var nowShowingList: List<BookingResponse.Output.Cinema.Child>,
    private var context: Context,
) :
    RecyclerView.Adapter<BookingCinemaNameAdapter.ViewHolder>() {

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
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language  .lng+this.lk
                binding.textView106.text = this.ccn

                binding.constraintLayout111.setOnClickListener {
                    if (binding.recyclerView11.visibility == View.GONE) {
                        binding.recyclerView11.show()
                        binding.imageView56.setImageResource(R.drawable.arrow_up)
                    } else {
                        binding.imageView56.setImageResource(R.drawable.arrow_down)
                        binding.recyclerView11.hide()
                    }
                }
                //Recycler
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                val bookingShowsParentAdapter =
                    BookingCinemaLangAdapter(this.sws, context, this.ccid)
                binding.recyclerView11.layoutManager = layoutManager
                binding.recyclerView11.adapter = bookingShowsParentAdapter
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

}