package com.net.pvr.ui.home.fragment.more.offer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R


class OfferFilterAdapter(
    private var data: List<String>,
    private var context: Context,
    private var listener: OffersAdapter,
    rowIndex: Int,
    private var offerRecList: RecyclerView
) : RecyclerView.Adapter<OfferFilterAdapter.FilterViewHolder>() {
    var rowIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_filter_list, parent, false)
        return FilterViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val dataItem = data[position]
        holder.name.text = dataItem
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        holder.name.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        if (rowIndex == position) {
            holder.name.isSelected = true
            holder.name.setTextColor(context.resources.getColor(R.color.black))
        } else {
            holder.name.isSelected = false
            holder.name.setTextColor(context.resources.getColor(R.color.h8Point1Color))
        }

        holder.itemView.setOnClickListener {
            if (rowIndex == position) {
                rowIndex = -1
                listener.onFilterClick(dataItem, 0, rowIndex,offerRecList)
            } else {
                rowIndex = position
                listener.onFilterClick(dataItem, 1, rowIndex,offerRecList)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView

        init {
            name = itemView.findViewById(R.id.name)
        }
    }

    interface RecycleViewItemClickListener {
        fun onFilterClick(offer: String?, type: Int, rowIndex: Int, recyclerView: RecyclerView?)
    }
}