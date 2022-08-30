package com.net.pvr1.ui.bookingSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemCinemaDetailsShowsBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse


class BookingShowsDaysAdapter(
    private var nowShowingList: List<BookingResponse.Output.Dy>,
    private var context: Context,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<BookingShowsDaysAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaDetailsShowsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsShowsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //Date
                binding.textView88.text=this.d
                //Name
                binding.textView89.text=this.wdf
                //click
                holder.itemView.setOnClickListener {listener.dateClick(this)  }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun dateClick(comingSoonItem: BookingResponse.Output.Dy)
    }

}