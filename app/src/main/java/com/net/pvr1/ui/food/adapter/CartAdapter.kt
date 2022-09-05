package com.net.pvr1.ui.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemFoodCartBinding
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.utils.Constant

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView147.text = this.title
                // TotalPrice
                calculateQt= this.quantity
                totalPrice= calculateQt*this.price
                val price: String = Constant().removeTrailingZeroFormater(totalPrice.toFloat())!!
                binding.textView153.text = context.resources.getString(R.string.currency) + price
                // ProductPrice
                val price2: String = Constant().removeTrailingZeroFormater(this.price.toFloat())!!
                binding.textView152.text = context.resources.getString(R.string.currency) + price2

                //quantity
                binding.textView154.text = this.quantity.toString()

                //FoodQuantity
                binding.textView151.text = this.quantity.toString()

                //SubTract
                binding.imageView77.setOnClickListener {
                    listener.cartFoodMinus(this, position)
                    notifyDataSetChanged()
                }
                //Add
                binding.imageView76.setOnClickListener {
                    listener.cartFoodPlus(this, position)
                    notifyDataSetChanged()
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