package com.net.pvr.ui.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemFoodPreviousBinding
import com.net.pvr.ui.food.response.FoodResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.invisible
import com.net.pvr.utils.show

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

                //title
                binding.textView132.text = this.nm
                //price
                binding.textView133.text = "₹ " + Constant.DECIFORMAT.format(this.dp / 100.0)

                if (this.r.size > 1) {
                    binding.textView134.show()
                    binding.textView135.setOnClickListener {
                        listener.categoryFoodDialog(this.r, this.nm,this.cid.toString())
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
            title: String,cid:String
        )
    }

}