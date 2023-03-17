package com.net.pvr.ui.home.inCinemaMode.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.net.pvr.databinding.ItemFoodOrderBinding

class FoodOrderAdapter(var context: Context, var items: MutableList<String>) :
    RecyclerView.Adapter<FoodOrderAdapter.FoodOrderViewHolder>() {
    private val viewPool = RecycledViewPool()

    class FoodOrderViewHolder(var context: Context, var binding: ItemFoodOrderBinding) :
        ViewHolder(binding.root) {

        fun bind(item: String) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodOrderViewHolder {
        var binding = ItemFoodOrderBinding.inflate(LayoutInflater.from(context), parent, false)
        return FoodOrderViewHolder(context = context, binding = binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FoodOrderViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.rvOrdersList.apply {
            post {
                this.layoutManager=LinearLayoutManager(holder.binding.rvOrdersList.context)
                (this.layoutManager as LinearLayoutManager).orientation= LinearLayoutManager.VERTICAL
                (this.layoutManager as LinearLayoutManager).initialPrefetchItemCount=3
                this.adapter = OrderedItemsAdapter(context = holder.binding.rvOrdersList.context, mutableListOf("", "", ""))
                setRecycledViewPool(viewPool)
            }
        }
    }
}