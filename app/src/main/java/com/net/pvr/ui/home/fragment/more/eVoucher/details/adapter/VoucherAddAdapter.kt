package com.net.pvr.ui.home.fragment.more.eVoucher.details.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.VoucherPurchaseListBinding
import com.net.pvr.ui.home.fragment.more.eVoucher.response.VoucherListResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.net.pvr.utils.toast
import java.util.*


@Suppress("DEPRECATION")
class VoucherAddAdapter(
    private var context: Context,
    private var profileList: ArrayList<VoucherListResponse.Output.Ev>,
    private var listner: RecycleViewItemClickListener
) : RecyclerView.Adapter<VoucherAddAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: VoucherPurchaseListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            VoucherPurchaseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(profileList[position]) {

                //amount
                val data = "<font color=#7A7A7A>${""}</font> <font color=#000000>${
                    context.getString(R.string.currency) + Constant.DECIFORMAT.format(this.sellAllowedValue / 100.0)}</font>"

                binding.textView410.text = Html.fromHtml(data)

                //discount
                if (this.sellAllowedCPValue != null && this.sellAllowedValue.toString() != null) {
                    if (this.sellAllowedCPValue.isNotEmpty() && this.sellAllowedValue.toString()
                            .isNotEmpty()
                    ) {
                        val percentage =
                            (this.sellAllowedCPValue.toInt() - this.sellAllowedValue) * 100 / 20000
                        binding.textView417.text = "$percentage % Off"
                    }
                }

                //total Price
                if (this.sellAllowedCPValue != null) {
                    if (this.sellAllowedCPValue.isNotEmpty()) {
                        binding.textView411.text =
                            context.getString(R.string.currency) + Constant.DECIFORMAT.format(this.sellAllowedCPValue.toInt() / 100.0)
                    }
                }

                //manageCart
                // Ui Show Hide
                if (this.quantity > 0) {
                    //+ - ui
                    binding.constraintLayout180.hide()
                    //add Ui
                    binding.constraintLayout181.show()
                } else {
                    //+ - ui
                    binding.constraintLayout180.show()
                    //add Ui
                    binding.constraintLayout181.hide()
                }


                //Quantity
                binding.uiPlusMinus.foodCount.text = this.quantity.toString()

                //click
                binding.addItem.setOnClickListener {
                    listner.addItem(this)
                    notifyDataSetChanged()
                }
                //decrease
                binding.uiPlusMinus.minus.setOnClickListener {
                    listner.decreaseItem(this)
                    notifyDataSetChanged()
                }

                //increase
                binding.uiPlusMinus.plus.setOnClickListener {
                    listner.increaseItem(this)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (profileList.isNotEmpty()) profileList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun addItem(ev: VoucherListResponse.Output.Ev)
        fun increaseItem(ev: VoucherListResponse.Output.Ev)
        fun decreaseItem(ev: VoucherListResponse.Output.Ev)
    }

}