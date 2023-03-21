package com.net.pvr.ui.myBookings.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.VoucherListItemBinding
import com.net.pvr.ui.myBookings.response.GiftCardResponse


class VoucherListAdapter(
    private var nowShowingList: List<GiftCardResponse.Output.Gc>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<VoucherListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: VoucherListItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VoucherListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cinemaItem = nowShowingList[position]
        with(holder) {
            with(nowShowingList[position]) {

                //title
                binding.textView34.text=""

                //valid

                binding.textView420.text=""

                //visit

                binding.textView421.text=""

                //resend
                binding.textView422.setOnClickListener {
                    listener.voucherBook(this)
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun voucherBook(comingSoonItem: GiftCardResponse.Output.Gc)
    }


}