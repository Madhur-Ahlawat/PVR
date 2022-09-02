package com.net.pvr1.ui.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodFilterBinding
import com.net.pvr1.ui.food.response.FoodResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.invisible
import com.net.pvr1.utils.show

//category

class FilterAdapter(
    private var nowShowingList: ArrayList<FoodResponse.Output.Mfl>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFoodFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodFilterBinding.inflate(
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
                    .load(this.mi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView68)

                //title
                binding.textView139.text = this.nm
                //price
                val price: String = Constant().removeTrailingZeroFormater(this.dp.toFloat())!!
                binding.textView140.text = context.resources.getString(R.string.currency) + price


                //SubTract
                binding.include9.plus.setOnClickListener {
                    listener.categoryFoodPlus(this, position)
                    notifyDataSetChanged()
                }
                //Add
                binding.include9.minus.setOnClickListener {
                    listener.categoryFoodMinus(this, position)
                    notifyDataSetChanged()
                }

                if (this.r.size > 1) {
                    binding.textView142.show()
                    binding.textView141.setOnClickListener {
                        listener.categoryFoodDialog(this.r, this.nm)
                    }
                } else {
                    binding.textView142.invisible()
                    binding.textView141.setOnClickListener {
                        binding.constraintLayout32.show()
                        binding.textView141.hide()
                        listener.categoryFoodClick(this)
                    }

                }
                if (this.veg) {
                    binding.imageView70.setImageDrawable(context.getDrawable(R.drawable.veg_ic))
                } else {
                    binding.imageView70.setImageDrawable(context.getDrawable(R.drawable.nonveg_ic))
                }
                //UiShowHide
                if (this.qt > 0) {
                    binding.constraintLayout32.show()
                    binding.textView141.hide()
                } else {
                    binding.constraintLayout32.hide()
                    binding.textView141.show()
                }

                //quantity
                binding.include9.foodCount.text = this.qt.toString()


            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun categoryFoodClick(comingSoonItem: FoodResponse.Output.Mfl)
        fun categoryFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int)
        fun categoryFoodMinus(comingSoonItem: FoodResponse.Output.Mfl, position: Int)
        fun categoryFoodDialog(
            comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
            title: String
        )
    }

}