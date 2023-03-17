package com.net.pvr.ui.home.inCinemaMode

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
import com.net.pvr.ui.home.FoodItemView
import com.net.pvr.ui.home.fragment.home.viewModel.HomeViewModel
import com.net.pvr.utils.RecyclerViewMargin
import com.net.pvr.utils.RecyclerViewMarginFoodInnerItem
import com.net.pvr.utils.RecyclerViewMarginFoodOrder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.databinding.BindableItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderItemView(var context: Context?, var item: Order) :
    BindableItem<ItemFoodOrderBinding>(){
    var pos:Int=-1
    var viewBinding: ItemFoodOrderBinding? = null
    var scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    var rotationAngle = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewBinding: ItemFoodOrderBinding, position: Int) {
        var groupAdapter = GroupAdapter<ViewHolder>()
        this.viewBinding = viewBinding
        viewBinding.apply {
            rvOrdersList.addItemDecoration(RecyclerViewMarginFoodInnerItem(30, 1))
            textViewOrderId.text = item.orderId
            textviewOrderValue.text=item.orderValue.toString()
            groupAdapter = GroupAdapter<ViewHolder>()
            rootOrder.setOnClickListener {
                if(rvOrdersList.visibility==View.GONE){
                    item.foodItems.forEach {
                        groupAdapter.add(FoodItemView(context,it))
                    }
                    rvOrdersList.adapter=groupAdapter
                    rvOrdersList.visibility=View.VISIBLE
                    imageviewExpandOrder.setImageDrawable(imageviewExpandOrder.context.resources.getDrawable(R.drawable.ic_arrow_up_white))
                }
                else{
                    groupAdapter.clear()
                    rvOrdersList.visibility=View.GONE
                    imageviewExpandOrder.setImageDrawable(imageviewExpandOrder.context.resources.getDrawable(R.drawable.ic_arrow_up_white))
                }
            }
            item.foodItems.forEach {
                groupAdapter.add(FoodItemView(context,it))
            }
            rvOrdersList.adapter=groupAdapter
            rvOrdersList.visibility=View.VISIBLE
            imageviewExpandOrder.setImageDrawable(imageviewExpandOrder.context.resources.getDrawable(R.drawable.ic_arrow_up_white))
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_food_order
    }
}