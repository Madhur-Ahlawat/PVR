package com.net.pvr1.ui.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodBinding
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class FoodBestSellerAdapter(
    private var nowShowingList: List<FoodResponse.Output.Bestseller>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<FoodBestSellerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView132.text = this.nm

                //price
                val price: String = Constant().removeTrailingZeroFormater(this.dp.toFloat())!!
                binding.textView133.text = context.resources.getString(R.string.currency) + price

                //fssai
                if (this.veg) {
                    binding.imageView69.setImageDrawable(context.getDrawable(R.drawable.veg_ic))
                } else {
                    binding.imageView69.setImageDrawable(context.getDrawable(R.drawable.nonveg_ic))
                }
                //Image
                Glide.with(context)
                    .load(this.mi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView65)

                //quantity
                binding.uiPlusMinus.foodCount.text = this.qt.toString()

                binding.imageView65.setOnClickListener {
                    listener.foodBestImageClick(this)
                    notifyDataSetChanged()
                }
                //AddFood
                binding.textView135.setOnClickListener {
                    listener.addFood(this,position)
                    binding.consAddUi.show()
                    binding.textView135.hide()
                    notifyDataSetChanged()
                }
                //SubTract
                binding.uiPlusMinus.plus.setOnClickListener {
                    listener.addFoodPlus(this,position)
                    notifyDataSetChanged()
                }
                //Add
                binding.uiPlusMinus.minus.setOnClickListener {
                    listener.addFoodMinus(this,position)

                    notifyDataSetChanged()
                }
                //UiShowHide

                if (this.qt >0) {
                    binding.consAddUi.show()
                    binding.textView135.hide()
                } else {
                    binding.consAddUi.hide()
                    binding.textView135.show()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller)
        fun addFood(comingSoonItem: FoodResponse.Output.Bestseller, position: Int)
        fun addFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller, position: Int)
        fun addFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller, position: Int)
    }

}