package com.net.pvr1.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodBinding
import com.net.pvr1.databinding.ItemFoodPreviousBinding
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.invisible
import com.net.pvr1.utils.show

//category

class PreviousFoodAdapter(
    private var nowShowingList: ArrayList<FoodResponse.Output.Mfl>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
    s: String,
) :
    RecyclerView.Adapter<PreviousFoodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFoodPreviousBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodPreviousBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Image
                Glide.with(context)
                    .load(this.mi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView65)

                //title
                binding.textView132.text = this.nm
                //price
                binding.textView133.text = "₹ " + Constant.DECIFORMAT.format(this.dp / 100.0)

                if (this.r.size > 1) {
                    binding.textView134.show()
                    binding.textView135.setOnClickListener {
                        listener.categoryFoodDialog(this.r, this.nm)
                    }
                } else {
                    binding.textView134.invisible()
                    binding.textView135.setOnClickListener {
                        binding.consAddUi.show()
                        binding.textView135.hide()
                        listener.categoryFoodClick(this)
                        notifyDataSetChanged()
                    }

                }
                //SubTract
                binding.uiPlusMinus.plus.setOnClickListener {
                    listener.categoryFoodPlus(this, position)
                    notifyDataSetChanged()
                }
                //Add
                binding.uiPlusMinus.minus.setOnClickListener {
                    listener.categoryFoodMinus(this, position)
                    notifyDataSetChanged()
                }

                binding.imageView65.setOnClickListener {
                    listener.categoryFoodImageClick(this)
                }
                if (this.veg) {
                    binding.imageView69.setImageDrawable(context.getDrawable(R.drawable.veg_ic))
                } else {
                    binding.imageView69.setImageDrawable(context.getDrawable(R.drawable.nonveg_ic))
                }
                //UiShowHide
                if (this.qt > 0) {
                    binding.consAddUi.show()
                    binding.textView135.hide()
                } else {
                    binding.consAddUi.hide()
                    binding.textView135.show()
                }
                //quantity
                binding.uiPlusMinus.foodCount.text = this.qt.toString()
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun categoryFoodClick(comingSoonItem: FoodResponse.Output.Mfl)
        fun categoryFoodImageClick(comingSoonItem: FoodResponse.Output.Mfl)
        fun categoryFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int)
        fun categoryFoodMinus(comingSoonItem: FoodResponse.Output.Mfl, position: Int)
        fun categoryFoodDialog(
            comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
            title: String
        )
    }

}