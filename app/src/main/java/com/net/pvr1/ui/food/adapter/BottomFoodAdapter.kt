package com.net.pvr1.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodCartBinding
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

class BottomFoodAdapter(
    private var nowShowingList: List<FoodResponse.Output.Bestseller.R>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<BottomFoodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFoodCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Image
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView146)

                //title
                binding.textView147.text = this.h

                //price
                binding.textView153.text =context.getString(R.string.currency)+" " + Constant.DECIFORMAT.format(this.dp / 100.0)


                //Add
                binding.include25.plus.setOnClickListener {
                    listener.filterBtFoodPlus(this,nowShowingList)
                    notifyDataSetChanged()
                }
                //SubTract
                binding.include25.minus.setOnClickListener {
                    listener.filterBtFoodMinus(this,nowShowingList)
                    notifyDataSetChanged()
                }
                binding.textView146.setOnClickListener {
                    listener.filterBtFoodClick(this,nowShowingList)
                    binding.constraintLayout33.show()
                    binding.textView146.hide()
                    notifyDataSetChanged()
                }

                //fssai
                if (this.veg) {
                    binding.imageView75.setImageResource(R.drawable.veg_ic)
                } else {
                    binding.imageView75.setImageResource(R.drawable.nonveg_ic)
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
                binding.include25.foodCount.text = this.qt.toString()

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun filterBtFoodClick(comingSoonItem: FoodResponse.Output.Bestseller.R,list:List<FoodResponse.Output.Bestseller.R>)
        fun filterBtFoodPlus(comingSoonItem: FoodResponse.Output.Bestseller.R,list:List<FoodResponse.Output.Bestseller.R>)
        fun filterBtFoodMinus(comingSoonItem: FoodResponse.Output.Bestseller.R,list:List<FoodResponse.Output.Bestseller.R>)
    }

}