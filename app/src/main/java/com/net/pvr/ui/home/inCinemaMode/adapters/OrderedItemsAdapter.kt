package com.net.pvr.ui.home.inCinemaMode.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.net.pvr.databinding.ItemFoodOrderBinding

class OrderedItemsAdapter(var context: Context,var items:MutableList<String>):RecyclerView.Adapter<OrderedItemsAdapter.OrderedItemsViewHolder>() {
    class OrderedItemsViewHolder(var binding:ItemFoodOrderBinding):ViewHolder(binding.root) {
        fun bind(item:String){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedItemsViewHolder {
        var binding = ItemFoodOrderBinding.inflate(LayoutInflater.from(context),parent,true)
        return OrderedItemsViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OrderedItemsViewHolder, position: Int) {
        holder.bind(items[position])
    }
}