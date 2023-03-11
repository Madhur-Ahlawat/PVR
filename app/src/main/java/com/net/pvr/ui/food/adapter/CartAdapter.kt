package com.net.pvr.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemFoodCartBinding
import com.net.pvr.ui.food.CartModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

//category

class CartAdapter(
    private var nowShowingList: ArrayList<CartModel>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemFoodCartBinding) :
        RecyclerView.ViewHolder(binding.root)
    private var calculateQt:Int=0
    private var totalPrice:Int=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView147.text = this.title
                // TotalPrice
                calculateQt= this.quantity
                totalPrice= calculateQt*this.price
                //total price
                binding.textView153.text = context.getString(R.string.currency)+ Constant.DECIFORMAT.format(this.price / 100.0)

//                binding.textView152.text = "₹" + Constant.DECIFORMAT.format(this.price / 100.0)
                Glide.with(context).load(this.image).into(binding.imageView146);

                //quantity
                binding.include25.foodCount.text=this.quantity.toString()

                //FoodQuantity
                binding.textView151.text = this.quantity.toString()

                //SubTract
                binding.include25.minus.setOnClickListener {
                    listener.cartFoodMinus(this, position)
                    notifyDataSetChanged()
                }
                //Add
                binding.include25.plus.setOnClickListener {
                    listener.cartFoodPlus(this, position)
                    notifyDataSetChanged()
                }

//                UiShowHide
                if (this.quantity > 0) {
                    binding.constraintLayout33.show()
                    binding.textView146.hide()
                } else {
                    binding.constraintLayout33.hide()
                    binding.textView146.show()
                }

                if (this.veg) {
                    binding.imageView75.setImageDrawable(context.getDrawable(R.drawable.veg_ic))
                } else {
                    binding.imageView75.setImageDrawable(context.getDrawable(R.drawable.nonveg_ic))
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun cartFoodPlus(comingSoonItem: CartModel, position: Int)
        fun cartFoodMinus(comingSoonItem: CartModel, position: Int)

    }

}