package com.net.pvr.ui.home.fragment.more.eVoucher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.EVoucherItemBinding
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.printLog

import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(profileList[position]) {

                //title
                binding.textView403.text = this.binDiscountName

                //valid
                binding.textView404.text= context.getString(R.string.valid_till)+" "+
                        Constant().dateFormatter(this.endDate.toString())

                //amount
                val data = "<font color=#7A7A7A>${context.getString(R.string.amount)+ " "}</font> <font color=#007D23>${
                    context.getString(R.string.currency)+
                            Constant.DECIFORMAT.format(this.sellAllowedValue / 100.0)}</font>"

                binding.textView405.text= Html.fromHtml(data)

                //discount
                if (this.sellAllowedCPValue.isNotEmpty() && this.sellAllowedValue.toString().isNotEmpty()){
                    val percentage=  (this.sellAllowedCPValue.toInt() - this.sellAllowedValue) * 100 / 20000
                    binding.textView406.text= "$percentage %"
                }

                //total Price
                if (this.sellAllowedCPValue.isNotEmpty()){
                    binding.textView412.text =context.getString(R.string.currency)+
                            Constant.DECIFORMAT.format(this.sellAllowedCPValue.toInt() / 100.0)
                }

                //Image
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