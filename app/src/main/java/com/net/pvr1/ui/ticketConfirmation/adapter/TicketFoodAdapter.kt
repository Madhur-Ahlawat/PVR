package com.net.pvr1.ui.ticketConfirmation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.TicketConfirmationFoodItemBinding
import com.net.pvr1.ui.ticketConfirmation.response.TicketBookedResponse

class TicketFoodAdapter(private val faqList: List<TicketBookedResponse.Food>) :
    RecyclerView.Adapter<TicketFoodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: TicketConfirmationFoodItemBinding) : RecyclerView.ViewHolder(binding.root)
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TicketConfirmationFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(faqList[position]) {
                //title
                binding.textView332.text=this.h
                //qt
                binding.textView340.text="X${this.qt}"
                if (this.veg){
                    binding.imageView155.setImageResource(R.drawable.veg_ic)
                }else{
                    binding.imageView155.setImageResource(R.drawable.nonveg_ic)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return faqList.size
    }

}