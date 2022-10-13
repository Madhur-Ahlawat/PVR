package com.net.pvr1.ui.seatLayout.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.SummeryItemFoodBinding
import com.net.pvr1.ui.food.CartModel


class AddFoodCartAdapter(
    private var nowShowingList: ArrayList<CartModel>,
    private var context: Context, private var listener: RecycleViewItemClickListenerCity
) :
    RecyclerView.Adapter<AddFoodCartAdapter.ViewHolder>() {
    private var itemCount = 0

    inner class ViewHolder(val binding: SummeryItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SummeryItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language
                binding.textView122.text = this.title
                //quantity
                binding.textView124.text = this.quantity.toString()
                //price
                binding.textView123.text=this.price.toString()
                if (this.veg) {
                    Glide.with(context).load(R.drawable.veg_ic).into(binding.imageView80)
                } else {
                    Glide.with(context).load(R.drawable.nonveg_ic).into(binding.imageView80)
                }
                binding.imageView81.setOnClickListener {
                    listener.increaseFoodClick(this)
                    notifyDataSetChanged()
                }
                binding.imageView82.setOnClickListener {
                    listener.decreaseFoodClick(this)
                    notifyDataSetChanged()
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun increaseFoodClick(comingSoonItem: CartModel)
        fun decreaseFoodClick(comingSoonItem: CartModel)
    }

}