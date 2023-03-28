package com.net.pvr.ui.home.inCinemaMode

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.net.pvr.R
import com.net.pvr.databinding.ItemFoodOrderBinding
import com.net.pvr.ui.home.FoodItemView
import com.net.pvr.ui.home.inCinemaMode.response.InCinemaFoodResp
import com.net.pvr.utils.RecyclerViewMarginFoodInnerItem
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.databinding.BindableItem


class OrderItemView(var context: Context?, var item: InCinemaFoodResp) :
    BindableItem<ItemFoodOrderBinding>(){
    var viewBinding: ItemFoodOrderBinding? = null
    var rowindex = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewBinding: ItemFoodOrderBinding, position: Int) {
        var groupAdapter = GroupAdapter<ViewHolder>()
        this.viewBinding = viewBinding
        viewBinding.apply {
            if(rvOrdersList.itemDecorationCount==0){
                rvOrdersList.addItemDecoration(RecyclerViewMarginFoodInnerItem(30, 1))
            }
            if (rowindex == position){
                rvOrdersList.show()
            }else{
                rvOrdersList.hide()
            }
            println("item.bookingId--->"+item.bookingId)
            textViewOrderId.text = item.bookingId
            textviewOrderValue.text=item.totalPrice
            groupAdapter = GroupAdapter<ViewHolder>()
            rootOrder.setOnClickListener {
                rowindex = position
                if(rvOrdersList.visibility==View.GONE){
                    groupAdapter.clear()
                    item.foods.forEach {
                        groupAdapter.add(FoodItemView(context,it))
                    }
                    rvOrdersList.adapter=groupAdapter
                    rvOrdersList.visibility=View.VISIBLE
                    imageviewExpandOrder.setImageDrawable(imageviewExpandOrder.context.resources.getDrawable(R.drawable.ic_arrow_up_white))
                }
                else{
                    groupAdapter.clear()
                    rvOrdersList.visibility=View.GONE
                    imageviewExpandOrder.setImageDrawable(imageviewExpandOrder.context.resources.getDrawable(R.drawable.ic_arrow_down))
                }

                groupAdapter.notifyDataSetChanged()

            }
            groupAdapter.clear()
            item.foods.forEach {
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