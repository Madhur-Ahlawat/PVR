package com.net.pvr1.ui.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodBottomBinding
import com.net.pvr1.databinding.ItemFoodFilterBinding
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

class FilterBottomAdapter(
    private var nowShowingList: List<FoodResponse.Output.Bestseller.R>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<FilterBottomAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFoodBottomBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBottomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Image
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView72)

                //title
                binding.textView144.text = this.h
                //price
//                val price: String = Constant().removeTrailingZeroFormater(this.dp.toFloat())!!
//                binding.textView145.text = context.resources.getString(R.string.currency) + price
                binding.textView145.text = "₹ " + Constant.DECIFORMAT.format(this.dp / 100.0)


                //SubTract
                binding.include10.plus.setOnClickListener {
                    listener.filterBtFoodPlus(this)
                    notifyDataSetChanged()
                }
                //Add
                binding.include10.minus.setOnClickListener {
                    listener.filterBtFoodMinus(this)
                    notifyDataSetChanged()
                }
                binding.textView146.setOnClickListener {
                    listener.filterBtFoodClick(this)
                    binding.constraintLayout33.show()
                    binding.textView146.hide()
                    notifyDataSetChanged()
                }

//                UiShowHide
                if (this.qt > 0) {
                    binding.constraintLayout33.show()
                    binding.textView146.hide()
                } else {
                    binding.constraintLayout33.hide()
                    binding.textView146.show()
                }
                //quantity
                binding.include10.foodCount.text = this.qt.toString()

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun filterBtFoodClick(comingSoonItem: FoodResponse.Output.Bestseller.R)
        fun filterBtFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller.R)
        fun filterBtFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller.R)
    }

}