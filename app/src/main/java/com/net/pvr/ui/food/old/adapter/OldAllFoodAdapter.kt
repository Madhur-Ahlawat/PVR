package com.net.pvr.ui.food.old.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.OldFoodItemsBinding
import com.net.pvr.ui.food.old.reponse.OldFoodResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

class OldAllFoodAdapter(
    private var nowShowingList: ArrayList<OldFoodResponse.Output.R>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<OldAllFoodAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: OldFoodItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OldFoodItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Image
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.app_icon)
                    .into(binding.ivBnr)

                if (this.veg) {
                    binding.vegNonveg.setImageDrawable(context.getDrawable(R.drawable.veg_ic))
                } else {
                    binding.vegNonveg.setImageDrawable(context.getDrawable(R.drawable.nonveg_ic))
                }

                binding.ivBnr.setOnClickListener {
                    if (binding.ivBnr.visibility == View.VISIBLE) {
                        if (binding.ivBnr.tag == null) {
                            showComponents(binding.CLlayout) // if the animation is NOT shown, we animate the views
                            binding.ivBnr.tag = "hide"
                        } else {
                            hideComponents(binding.CLlayout) // if the animation is shown, we hide back the views
                            binding.ivBnr.tag = null
                        }
                    }
                }

                //title
                binding.tvName.text = this.h

                //price
                binding.tvPrice.text = context.getString(R.string.currency) + Constant.DECIFORMAT.format(this.dp / 100.0)
                //UiShowHide
                if (this.qt > 0) {
                    binding.beforeAdd.hide()
                    binding.llCounter.show()
                } else {
                    binding.beforeAdd.show()
                    binding.llCounter.hide()
                }

                //quantity
                binding.uiPlusMinus.foodCount.text = this.qt.toString()

                //add Button Click
                binding.beforeAdd.setOnClickListener {
                    listener.foodClick(this)
                    notifyDataSetChanged()
                }

                //Add
                binding.uiPlusMinus.plus.setOnClickListener {
                    listener.foodAddClick(this)
                    notifyDataSetChanged()
                }

                //Subtract
                binding.uiPlusMinus.minus.setOnClickListener {
                    listener.foodSubtractClick(this)
                    notifyDataSetChanged()
                }
            }
        }

    }

    private fun hideComponents(constraintLayout: ConstraintLayout): Boolean {
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.old_food_items)
        val transition = ChangeBounds()
        transition.interpolator = DecelerateInterpolator()
        transition.duration = 600
        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        constraintSet.applyTo(constraintLayout) //here constraint is the name of view to which we are applying the constraintSet
        return false
    }

    private fun showComponents(constraintLayout: ConstraintLayout): Boolean {
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.old_food_item_expand)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateInterpolator()
        transition.duration = 600
        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        constraintSet.applyTo(constraintLayout) //here constraint is the name of view to which we are applying the constraintSet
        return true
    }


    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun foodClick(comingSoonItem: OldFoodResponse.Output.R)
        fun foodAddClick(comingSoonItem: OldFoodResponse.Output.R)
        fun foodSubtractClick(comingSoonItem: OldFoodResponse.Output.R)

    }

}