package com.net.pvr1.ui.home.fragment.commingSoon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R


class LanguageAdapter(
    private var nowShowingList: ArrayList<String>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<LanguageAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coming_soon_filter, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        holder.title.text = comingSoonItem
        holder.itemView.setOnClickListener {
            listener.onDateClick(comingSoonItem)
        }


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.name)
    }

    interface RecycleViewItemClickListener {
        fun onDateClick(comingSoonItem: Any)
    }


}