package com.net.pvr.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemFoodCartBinding
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

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
                binding.textView153.text =context.getString(R.string.currency)+Constant.DECIFORMAT.format(this.dp / 100.0)

                if (this.wt != null && this.wt !="" && this.en != null && this.en != "") {
                    binding.cal.text = this.wt + "  •  " + this.en
                    binding.cal.show()
                } else if (this.wt != null && this.wt != "") {
                    binding.cal.text = this.wt
                    binding.cal.show()
                } else if (this.en != null && this.en != "") {
                    binding.cal.text = this.wt
                    binding.cal.show()
                } else {
                    binding.cal.hide()
                }
                if (this.fa != null && this.fa != "") {
                    if (this.fa.split(",").size > 2) {
                        val text =
                            "<font color=#7A7A7A> Allergens " + this.fa.split(",")[0] + ", " + this.fa.split(",")[1] + " </font> <font color=#000000><b> +" + (this.fa.split(",").size - 2) + "</b></font>"
                        binding.fa.text = Html.fromHtml(text)
                    } else {
                        binding.fa.text = "Allergens " + this.fa
                    }
                    binding.fa.show()
                } else {
                    binding.fa.hide()
                }
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