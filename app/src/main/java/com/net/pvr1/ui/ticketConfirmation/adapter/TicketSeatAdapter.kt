package com.net.pvr1.ui.ticketConfirmation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemSeatBinding
import com.net.pvr1.ui.ticketConfirmation.response.TicketBookedResponse

class TicketSeatAdapter(private val faqList: List<TicketBookedResponse.Seat>) :
    RecyclerView.Adapter<TicketSeatAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSeatBinding) : RecyclerView.ViewHolder(binding.root)
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(faqList[position]) {
                binding.textView116.text=this.n
            }
        }
    }
    override fun getItemCount(): Int {
        return faqList.size
    }

}