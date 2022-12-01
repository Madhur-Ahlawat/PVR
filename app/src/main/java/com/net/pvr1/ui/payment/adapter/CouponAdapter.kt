package com.net.pvr1.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemPaymentListBinding
import com.net.pvr1.databinding.ItemPaymentPrivlegeBinding
import com.net.pvr1.ui.food.CartModel
import com.net.pvr1.ui.payment.response.CouponResponse

//category

class CouponAdapter(
    private var nowShowingList: ArrayList<CouponResponse.Output>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CouponAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPaymentPrivlegeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentPrivlegeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.radioButton.text = context.getString(R.string.redeem)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun couponClick(comingSoonItem: CartModel, position: Int)
    }

}