package com.net.pvr.ui.home.fragment.more.eVoucher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.EVoucherItemBinding
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse


class VoucherListAdapter(
    private var context: Context,
    private var profileList: ArrayList<VoucherListResponse.Output.Ev>
    ,private var listner:RecycleViewItemClickListener
) : RecyclerView.Adapter<VoucherListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: EVoucherItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            EVoucherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(profileList[position]) {
                //title
                binding.textView403.text = this.binDiscountName
                //valid
                binding.textView404.text= this.voucherCategory
                //amount
                binding.textView405.text= this.voucherCategory
                //discount
                binding.textView406.text= this.voucherCategory

                Glide.with(context)
                    .load(this.imageUrl1)
                    .error(R.drawable.placeholder_horizental)
                    .into(binding.imageView160)

                //click
                holder.itemView.setOnClickListener {
                    listner.itemClick(this)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return if (profileList.isNotEmpty()) profileList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun itemClick(ev: VoucherListResponse.Output.Ev)
    }

}