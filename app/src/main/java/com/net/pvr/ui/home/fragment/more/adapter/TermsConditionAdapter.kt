package com.net.pvr.ui.home.fragment.more.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.ItemTermsConditionBinding


class TermsConditionAdapter(
    private var context: Context,
    private var  nowShowingList: Array<String>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<TermsConditionAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTermsConditionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTermsConditionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                binding.showTitle.text=this
                //click
                holder.itemView.setOnClickListener {
                    listener.alsoPlaying(this,position)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun alsoPlaying(comingSoonItem: String, position: Int)
    }

}