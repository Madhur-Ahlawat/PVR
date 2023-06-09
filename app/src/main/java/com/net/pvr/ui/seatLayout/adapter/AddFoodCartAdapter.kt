package com.net.pvr.ui.seatLayout.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.SummeryItemFoodBinding
import com.net.pvr.ui.food.CartModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide


class AddFoodCartAdapter(
    private var nowShowingList: ArrayList<CartModel>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity
) : RecyclerView.Adapter<AddFoodCartAdapter.ViewHolder>() {

    private var calculateQt = 0
    private var totalPrice = 0

    inner class ViewHolder(val binding: SummeryItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SummeryItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language
                binding.textView122.text = this.title
                //quantity
                binding.uiPlusMinus.foodCount.text = this.quantity.toString()

                if (Constant.BOOK_TYPE == "FOOD"){
                    binding?.constraintLayout41?.hide()
                }

                calculateQt = this.quantity
                totalPrice = calculateQt * this.price
                //price
                binding.textView123.text = context.getString(R.string.currency) + Constant.DECIFORMAT.format(totalPrice / 100.0)
//                    context.getString(R.string.currency) + Constant.DECIFORMAT.format(this.price / 100.0)

                if (this.veg) {
                    Glide.with(context).load(R.drawable.veg_ic).into(binding.imageView80)
                } else {
                    Glide.with(context).load(R.drawable.nonveg_ic).into(binding.imageView80)
                }

                binding.uiPlusMinus.minus.setOnClickListener {
                    listener.decreaseFoodClick(this)
                    notifyDataSetChanged()
                }

                binding.uiPlusMinus.plus.setOnClickListener {
                    listener.increaseFoodClick(this)
                    notifyDataSetChanged()
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun increaseFoodClick(comingSoonItem: CartModel)
        fun decreaseFoodClick(comingSoonItem: CartModel)
    }

}