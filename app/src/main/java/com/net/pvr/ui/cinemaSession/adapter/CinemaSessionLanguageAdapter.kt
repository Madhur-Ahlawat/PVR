package com.net.pvr.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.ItemCinemaDetailsLanguageBinding


class CinemaSessionLanguageAdapter(
    private var nowShowingList: List<String>,
    private var context: Context,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CinemaSessionLanguageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaDetailsLanguageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //Language
                binding.textView90.text=this
                //click
                holder.itemView.setOnClickListener {listener.languageClick(this)  }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun languageClick(comingSoonItem: String)
    }

}