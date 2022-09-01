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
import com.net.pvr1.utils.invisible
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
                val price: String = Constant().removeTrailingZeroFormater(this.op.toFloat())!!

                //price
                binding.textView133.text = context.resources.getString(R.string.currency) + price
                //customize
//                binding.textView134.text = this.genres.joinToString { it }
                //Add
                binding.textView135.text = "Hindi Static"
                //fssai
                if (this.veg) {
                    binding.imageView69.setImageDrawable(context.resources.getDrawable(R.drawable.veg_ic))
                } else {
                    binding.imageView69.setImageDrawable(context.resources.getDrawable(R.drawable.nonveg_ic))
                }
                //Image
                Glide.with(context)
                    .load(this.mi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView65)

                binding.imageView65.setOnClickListener {
                    listener.foodBestImageClick(this)
                }
                //AddFood
                binding.textView135.setOnClickListener {
                    listener.addFood(this)
                    binding.consAddUi.show()
                    binding.textView135.hide()
                }
                //SubTract
                binding.uiPlusMinus.plus.setOnClickListener {
                    listener.addFoodPlus(this)
                }
                //Add
                binding.uiPlusMinus.minus.setOnClickListener {
                    listener.addFoodMinus(this)
                }

                if (this.r.size > 1) {
                    binding.textView134.hide()
                    binding.textView132.text = this.nm
                    val price: String = Constant().removeTrailingZeroFormater(this.dp.toFloat())!!
                    binding.textView133.text =
                        context.resources.getString(R.string.currency) + price
                } else {
                    binding.textView134.invisible()
                    binding.textView132.text = this.r[0].h
                    val price: String =
                        Constant().removeTrailingZeroFormater(r.get(0).dp.toFloat())!!
                    binding.textView133.text =
                        context.resources.getString(R.string.currency) + price
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun foodBestImageClick(comingSoonItem: FoodResponse.Output.Bestseller)
        fun addFood(comingSoonItem: FoodResponse.Output.Bestseller)
        fun addFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller)
        fun addFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller)
    }

}