package com.net.pvr.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemFoodBinding
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.invisible
import com.net.pvr.utils.show


class BestSellerFoodAdapter(
    private var nowShowingList: List<FoodResponse.Output.Bestseller>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<BestSellerFoodAdapter.ViewHolder>() {
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

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView132.text = this.nm
                binding.textView133.text = context.getString(R.string.currency)+ Constant.DECIFORMAT.format(this.dp / 100.0)
                if (this.r[0].wt != null && this.r[0].wt !="" && this.r[0].en != null && this.r[0].en != "") {
                    binding.cal.text = this.r[0].wt + "  •  " + this.r[0].en
                    binding.cal.show()
                } else if (this.r[0].wt != null && this.r[0].wt != "") {
                    binding.cal.text = this.r[0].wt
                    binding.cal.show()
                } else if (this.r[0].en != null && this.r[0].en != "") {
                    binding.cal.text = this.r[0].wt
                    binding.cal.show()
                } else {
                    binding.cal.hide()
                }
                if (this.r[0].fa != null && this.r[0].fa != "") {
                    if (this.r[0].fa.split(",").size > 2) {
                        val text =
                            "<font color=#7A7A7A> Allergens " + this.r[0].fa.split(",")[0] + ", " + this.r[0].fa.split(",")[1] + " </font> <font color=#000000><b> +" + (this.r[0].fa.split(",").size - 2) + "</b></font>"
                        binding.fa.text = Html.fromHtml(text)
                    } else {
                        binding.fa.text = "Allergens " + this.r[0].fa
                    }
                    binding.fa.show()
                } else {
                    binding.fa.hide()
                }
                if (this.r.size > 1) {
                    binding.textView134.show()
                    binding.textView135.hide()
                    binding.textView135.setOnClickListener {
                        listener.bestSellerDialogAddFood(this.r, this.nm,this.cid.toString())
                    }
                    if (this.qt>0){
                        binding.uiPlusMinus.foodCount.text = qt.toString()
                    }

                    //SubTract
                    binding.uiPlusMinus.plus.setOnClickListener {
                        listener.bestSellerDialogAddFood(nowShowingList[position].r, this.nm,this.cid.toString())
                        notifyDataSetChanged()
                    }

                    //Add
                    binding.uiPlusMinus.minus.setOnClickListener {
                        listener.bestSellerDialogAddFood(this.r, this.nm,this.cid.toString())
                        notifyDataSetChanged()
                    }

                    //UiShowHide
                    if (this.qt > 0) {
                        binding.consAddUi.show()
                        binding.textView135.hide()
                    } else {
                        binding.consAddUi.hide()
                        binding.textView135.show()
                    }

                } else {
                    binding.uiPlusMinus.foodCount.text = this.qt.toString()
                    binding.textView134.invisible()
                    binding.textView135.setOnClickListener {
                        listener.addFood(this, position)
                        binding.consAddUi.show()
                        binding.textView135.hide()
                        notifyDataSetChanged()
                    }

                    //SubTract
                    binding.uiPlusMinus.plus.setOnClickListener {
                        listener.addFoodPlus(this, position)
                        notifyDataSetChanged()
                    }

                    //Add
                    binding.uiPlusMinus.minus.setOnClickListener {
                        listener.addFoodMinus(this, position)
                        notifyDataSetChanged()
                    }

                    //UiShowHide
                    if (this.qt > 0) {
                        binding.consAddUi.show()
                        binding.textView135.hide()
                    } else {
                        binding.consAddUi.hide()
                        binding.textView135.show()
                    }

                }

                //fssai
                if (this.veg) {
                    binding.imageView69.setImageResource(R.drawable.veg_ic)
                } else {
                    binding.imageView69.setImageResource(R.drawable.nonveg_ic)
                }

                //Image
                Glide.with(context)
                    .load(this.mi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView65)

                //quantity

                binding.imageView65.setOnClickListener {
                    listener.foodBestImageClick(this)
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
        fun bestSellerDialogAddFood(comingSoonItem: List<FoodResponse.Output.Bestseller.R>, position: String ,cid:String)
    }

}