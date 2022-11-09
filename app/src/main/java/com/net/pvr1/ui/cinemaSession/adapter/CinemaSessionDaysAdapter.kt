package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemCinemaDetailsShowsBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse


class CinemaSessionDaysAdapter(
    private var nowShowingList: List<CinemaSessionResponse.Output.Bd>,
    private var context: Context,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CinemaSessionDaysAdapter.ViewHolder>() {

    private var rowIndex = 0;
    inner class ViewHolder(val binding: ItemCinemaDetailsShowsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsShowsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == rowIndex){
            holder.itemView.setBackgroundResource(R.drawable.bottom_bar_yellow)
        }else{
            holder.itemView.setBackgroundResource(0)
        }
        with(holder){
            with(nowShowingList[position]){
                //Date
                binding.textView88.text=this.d
                //Name
                binding.textView89.text=this.wd
                //click
                holder.itemView.setOnClickListener {
                    rowIndex = position
                    listener.dateClick(this)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun dateClick(comingSoonItem: CinemaSessionResponse.Output.Bd)
    }

}