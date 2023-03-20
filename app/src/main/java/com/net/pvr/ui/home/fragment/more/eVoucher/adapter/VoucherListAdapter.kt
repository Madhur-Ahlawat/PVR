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
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import java.util.*


@Suppress("DEPRECATION")
class VoucherListAdapter(
    private var context: Context,
    private var profileList: ArrayList<VoucherListResponse.Output.Ev>,
    private var listner: RecycleViewItemClickListener
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
                binding.textView404.text =
                    context.getString(R.string.valid_till) + " " + Constant().dateFormatter(this.endDate.toString())

                //amount
                val data =
                    "<font color=#7A7A7A>${context.getString(R.string.amount) + " "}</font> <font color=#007D23>${
                        context.getString(R.string.currency) + Constant.DECIFORMAT.format(this.sellAllowedValue / 100.0)
                    }</font>"

                binding.textView405.text = Html.fromHtml(data)

                //discount
                if (this.sellAllowedCPValue != null && this.sellAllowedValue.toString() != null) {
                    if (this.sellAllowedCPValue.isNotEmpty() && this.sellAllowedValue.toString()
                            .isNotEmpty()) {

                        var total = this.sellAllowedCPValue.toInt()
                        val discount = this.sellAllowedValue.toInt()
                        try {
                            if (total==0){
                                total+=1
                            }else{
                                total= this.sellAllowedCPValue.toInt()
                            }

                            println("percent---->${discount}---->${total}")
//                            val value = total / discount
                            val value = discount / total
                            val percentage = value * 100

                            binding.textView406.text = "$percentage %"
                            if (percentage > 0) {
                                binding.cardView22.show()
                            } else {
                                binding.cardView22.hide()
                            }

                        }catch (e:Exception){
                            e.printStackTrace()
                        }
//                        val percentage = (this.sellAllowedCPValue.toInt() - this.sellAllowedValue) * 100 / 20000
                    }
                } else {
                    binding.cardView22.hide()

                }

                //total Price

                if (this.sellAllowedCPValue != null) {
                    if (this.sellAllowedCPValue.isNotEmpty()) {
                        binding.textView412.text =
                            context.getString(R.string.currency) + Constant.DECIFORMAT.format(this.sellAllowedCPValue.toInt() / 100.0)
                    }
                }

                //Image
                Glide.with(context).load(this.imageUrl2).error(R.drawable.placeholder_horizental)
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

    fun filterCinemaList(filterList: ArrayList<VoucherListResponse.Output.Ev>) {
        // below line is to add our filtered
        profileList = filterList
        notifyDataSetChanged()
    }
}