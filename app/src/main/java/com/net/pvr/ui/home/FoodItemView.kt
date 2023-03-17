package com.net.pvr.ui.home

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemFoodOrderBinding
import com.net.pvr.databinding.ItemOrderedFoodBinding
import com.net.pvr.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr.ui.home.inCinemaMode.FoodItem
import com.net.pvr.utils.RecyclerViewMargin
import com.xwray.groupie.databinding.BindableItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodItemView(var context: Context?, var item: FoodItem) :
    BindableItem<ItemOrderedFoodBinding>(){
    override fun bind(viewBinding: ItemOrderedFoodBinding, position: Int) {
        viewBinding.apply {
            textviewItemName.text = item.itemName
            if(item.isNonVeg){
                imageviewVegNonveg.setImageDrawable(imageviewVegNonveg.context.resources.getDrawable(R.drawable.ic_non_veg_food))
            }
            else{
                imageviewVegNonveg.setImageDrawable(imageviewVegNonveg.context.resources.getDrawable(R.drawable.ic_veg_food))
            }
            textviewItemName.text=item.itemName
            textviewItemCount.text=item.count.toString()
        }
    }


    override fun getLayout(): Int {
        return R.layout.item_ordered_food
    }
}