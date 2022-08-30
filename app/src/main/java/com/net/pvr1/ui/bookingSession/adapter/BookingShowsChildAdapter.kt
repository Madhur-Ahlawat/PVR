package com.net.pvr1.ui.bookingSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemBookingSessionCinemaLanguageBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse


class BookingShowsChildAdapter(
    private var nowShowingList: List<BookingResponse.Output.Cinema.Child.Sw>,
    private var context: Context,
) :
    RecyclerView.Adapter<BookingShowsChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingSessionCinemaLanguageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //Language
                binding.textView106.text=this.lng+this.lk
                //Recycler
                val gridLayout3 = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
                val bookingShowsParentAdapter = BookingShowsTimeAdapter(this.s,  context)
                binding.recyclerView11.layoutManager = gridLayout3
                binding.recyclerView11.adapter = bookingShowsParentAdapter
                //click
//                holder.itemView.setOnClickListener {listener.languageClick(this)  }
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