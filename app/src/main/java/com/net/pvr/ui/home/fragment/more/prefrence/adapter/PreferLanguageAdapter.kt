package com.net.pvr.ui.home.fragment.more.prefrence.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.GenreChildItemBinding
import com.net.pvr.ui.home.fragment.more.prefrence.response.PreferenceResponse


class PreferLanguageAdapter(
    private var context: Context,
    private var nowShowingList: List<PreferenceResponse.Output.Lang.Other>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<PreferLanguageAdapter.ViewHolder>() {
    private var rowIndex=0

    inner class ViewHolder(val binding: GenreChildItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                binding.textView242.text=this.na
                binding.imageView169.setImageResource(R.drawable.curve_select)

                //click
                holder.itemView.setOnClickListener {
                    rowIndex=position
                    listener.languageLikeClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun languageLikeClick(comingSoonItem: PreferenceResponse.Output.Lang.Other)
    }

}