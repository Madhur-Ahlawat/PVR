package com.net.pvr.ui.home.fragment.more.prefrence.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.PreferenceTheatersItemBinding
import com.net.pvr.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr.utils.hide


class FavouriteTheaterAdapter(
    private var context: Context,
    private var nowShowingList: List<PreferenceResponse.Output.Theater.Other>,
    private var listener: RecycleViewItemClickListener
) : RecyclerView.Adapter<FavouriteTheaterAdapter.ViewHolder>() {
    private var rowIndex = -1

    inner class ViewHolder(val binding: PreferenceTheatersItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PreferenceTheatersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                binding.textView50.text = this.na

                binding.textView50.isSelected = true

                binding.textView53.hide()
                binding.textView51.hide()
                binding.imageView68.setImageResource(R.drawable.like)

                //click
                holder.itemView.setOnClickListener {
                    rowIndex = position

                    listener.favouriteTheaterClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun favouriteTheaterClick(comingSoonItem: PreferenceResponse.Output.Theater.Other)
    }

}