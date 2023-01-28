package com.net.pvr1.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodPlaceBinding
import com.net.pvr1.ui.food.response.FoodResponse
import kotlin.reflect.jvm.internal.impl.load.java.Constant

//category

class CategoryAdapter(
    private var nowShowingList: ArrayList<FoodResponse.Output.Cat>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
    private var recyclerView20: RecyclerView,
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var rowIndex = 0

    inner class ViewHolder(val binding: ItemFoodPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Image
                if (this.name=="ALL"){
                    Glide.with(context)
                        .load(R.drawable.food_all)
                        .error(R.drawable.food_all)
                        .into(binding.imageView66)
                    binding.imageView66.setBackgroundResource(R.drawable.border_circle_gray)
                    binding.imageView66.setPadding(40)
                }else{

                    Glide.with(context)
                        .load(this.i)
                        .error(R.drawable.app_icon)
                        .into(binding.imageView66)
                    binding.imageView66.setBackgroundResource(0)
                    binding.imageView66.setPadding(15)
                }
                if (rowIndex == position) {
                    binding.imageView66.setBackgroundResource(R.drawable.border_circle)
                    if (this.name=="ALL"){
                        binding.imageView66.setPadding(40)
                    }else{
                        binding.imageView66.setPadding(15)
                    }
                    com.net.pvr1.utils.Constant.focusOnView(holder.itemView,recyclerView20)

                } else {
                    if (this.name=="ALL"){
                        binding.imageView66.setPadding(40)
                        binding.imageView66.setBackgroundResource(R.drawable.border_circle_gray)
                    }else{
                        binding.imageView66.setPadding(15)
                        binding.imageView66.setBackgroundResource(0)
                    }
                }

                //Name
                binding.textView136.text = this.name
                holder.itemView.setOnClickListener {
                    rowIndex = position
                    listener.categoryClick(this)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun categoryClick(comingSoonItem: FoodResponse.Output.Cat)

    }

}