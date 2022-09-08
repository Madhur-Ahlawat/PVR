package com.net.pvr1.ui.bookingSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemBookingSessionCinemaLocationBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse


class BookingShowsParentAdapter(
    private var nowShowingList: List<BookingResponse.Output.Cinema>,
    private var context: Context
//    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<BookingShowsParentAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingSessionCinemaLocationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //Language
                binding.textView105.text=this.cn
                //Recycler


                val gridLayout3 = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                val bookingShowsParentAdapter = BookingShowsChildAdapter(this.childs[0].sws,context,)
                binding.recyclerView7.layoutManager = gridLayout3
                binding.recyclerView7.adapter = bookingShowsParentAdapter
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