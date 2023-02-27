package com.net.pvr.ui.home.fragment.more.bookingRetrieval.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.RetrivalItemChildBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse


class BookingRetrievalChildAdapter(
    private var context: Context,
    private var nowShowingList: List<BookingRetrievalResponse.Output.Child>,
) :
    RecyclerView.Adapter<BookingRetrievalChildAdapter.ViewHolder>() {

    private  var rowIndex=0
    inner class ViewHolder(val binding: RetrivalItemChildBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RetrivalItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //name
                binding.textView379.text="  "+this.ccn
                if (rowIndex==position){
                    binding.constraintLayout82.setBackgroundResource(R.drawable.retreval_select)
                    binding.radioButton3.setImageResource(R.drawable.curve_select)
                }else{
                    binding.constraintLayout82.setBackgroundResource(R.drawable.retreval_unselect)
                    binding.radioButton3.setImageResource(R.drawable.curve_unselect)

                }
                itemView.setOnClickListener {
                    rowIndex=position
                    notifyDataSetChanged()
                }
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