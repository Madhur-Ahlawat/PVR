package com.net.pvr.ui.movieDetails.nowShowing.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemDetailsCastBinding
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.utils.Constant


class CrewAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Crew.Role>,
    private var context: Context,
) :
    RecyclerView.Adapter<CrewAdapter.ViewHolder>() {
    private var rowIndex = 0
    inner class ViewHolder(val binding: ItemDetailsCastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailsCastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (rowIndex == position && position == 0) {
                    Constant().setMargins(holder.itemView,60,0,0,0)
                }
                //title
                binding.textView54.text =this.name
                //subTitle
                binding.textView80.text =this.role

                //Image
                Glide.with(context)
                    .load("https://"+this.poster)
                    .error(R.drawable.placeholder_vertical)
                    .into(binding.imageView30)

                //     click
                holder.itemView.setOnClickListener {
                    rowIndex == position
                    notifyDataSetChanged()
                }
            }
        }
        val cinemaItem = nowShowingList[position]


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


}