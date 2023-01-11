package com.net.pvr1.ui.movieDetails.nowShowing.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemDetailsMusicBinding
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class TrailerAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Video>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {
    private var rowIndex = 0

    inner class ViewHolder(val binding: ItemDetailsMusicBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailsMusicBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (rowIndex == position && position == 0) {
                    Constant().setMargins(holder.itemView, 60, 0, 0, 0)
                }
                //title
                binding.textView82.text = this.caption
                //subTitle
                binding.textView83.hide()
                binding.textView83.text = this.type

                //moreDetails
                holder.itemView.setOnClickListener {
                    rowIndex= position
                    listener.trailerClick(this)
                    notifyDataSetChanged()
                }
                if (this.url != "") {
                    binding.imageView34.show()
                } else {
                    binding.imageView34.hide()
                }

                //Image
                Glide.with(context)
                    .load(this.thumbnail)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView33)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun trailerClick(comingSoonItem: MovieDetailsResponse.Mb.Video)
    }
}