package com.net.pvr1.ui.payment.cardDetails.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemNetBankingBinding

//category

class NetBankingAdapter(
    private var nowShowingList: ArrayList<Map.Entry<String, String>>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<NetBankingAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemNetBankingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNetBankingBinding.inflate(
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
                binding.textView375.text = this.key+ " "+this.value
                holder.itemView.setOnClickListener {
                    listener.netBankingClick(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun netBankingClick(comingSoonItem: Map.Entry<String, String>)
    }

}