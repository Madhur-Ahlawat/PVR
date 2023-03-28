package com.net.pvr.ui.home

import android.content.Context
import com.net.pvr.R
import com.net.pvr.databinding.ItemOrderedFoodBinding
import com.net.pvr.ui.home.inCinemaMode.response.Food
import com.xwray.groupie.databinding.BindableItem

class FoodItemView(var context: Context?, var item: Food) :
    BindableItem<ItemOrderedFoodBinding>(){
    override fun bind(viewBinding: ItemOrderedFoodBinding, position: Int) {
        viewBinding.apply {
            textviewItemName.text = item.itemStrDescription
            if(!item.veg){
                imageviewVegNonveg.setImageDrawable(imageviewVegNonveg.context.resources.getDrawable(R.drawable.ic_non_veg_food))
            }
            else{
                imageviewVegNonveg.setImageDrawable(imageviewVegNonveg.context.resources.getDrawable(R.drawable.ic_veg_food))
            }
            textviewItemCount.text=item.itemIntQuantity.toString()
        }
    }


    override fun getLayout(): Int {
        return R.layout.item_ordered_food
    }
}