package com.net.pvr.ui.summery.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.ItemSeatBinding
import com.net.pvr.ui.summery.response.SummeryResponse

class SeatListAdapter(private val faqList: List<SummeryResponse.Output.Seat>) :
    RecyclerView.Adapter<SeatListAdapter.ViewHolder>() {
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