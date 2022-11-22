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

//category

class AllFoodAdapter(
    private var nowShowingList: ArrayList<FoodResponse.Output.Mfl>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<AllFoodAdapter.ViewHolder>() {
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
                //Image
                Glide.with(context)
                    .load(this.mi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView65)

                //title
                binding.textView132.text = this.nm
                //price
                val price: String = Constant().removeTrailingZeroFormater(this.dp.toFloat())!!
                binding.textView133.text = context.resources.getString(R.string.currency) + price


                //SubTract
                binding.uiPlusMinus.plus.setOnClickListener {
                    listener.allFoodPlus(this, position)
                    notifyDataSetChanged()
                }
                //Add
                binding.uiPlusMinus.minus.setOnClickListener {
                    listener.allFoodMinus(this, position)
                    notifyDataSetChanged()
                }

                if (this.r.size > 1) {
                    binding.textView134.show()
                    binding.textView135.setOnClickListener {
                        listener.allFoodDialog(this.r, this.nm)
                    }
                } else {
                    binding.textView134.invisible()
                    binding.textView135.setOnClickListener {
                        binding.consAddUi.show()
                        binding.textView134.hide()
                        listener.allFoodClick(this)
                    }

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
        fun allFoodClick(comingSoonItem: FoodResponse.Output.Mfl)
        fun allFoodPlus(comingSoonItem: FoodResponse.Output.Mfl, position: Int)
        fun allFoodMinus(comingSoonItem: FoodResponse.Output.Mfl, position: Int)
        fun allFoodDialog(
            comingSoonItem: List<FoodResponse.Output.Bestseller.R>,
            title: String
        )
    }

}