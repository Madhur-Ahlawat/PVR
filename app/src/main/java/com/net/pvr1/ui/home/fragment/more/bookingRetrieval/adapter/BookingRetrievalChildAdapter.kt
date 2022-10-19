package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.RetrivalItemChildBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse


class BookingRetrievalChildAdapter(
    private var context: BookingRetrievalResponse.Output.C,
    private var nowShowingList: List<BookingRetrievalResponse.Output.Child>,
//    private var listener:RecycleViewItemClickListener
) :
    RecyclerView.Adapter<BookingRetrievalChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RetrivalItemChildBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RetrivalItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //name
                binding.radioButton3.text="  "+this.ccn
               }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun dateClick(comingSoonItem: BookingResponse.Output.Dy)
    }

}