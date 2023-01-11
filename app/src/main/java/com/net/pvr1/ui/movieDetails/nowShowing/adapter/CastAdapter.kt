package com.net.pvr1.ui.movieDetails.nowShowing.adapter

import android.R.attr.*
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemDetailsCastBinding
import com.net.pvr1.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


@Suppress("DEPRECATION")
class CastAdapter(
    private var nowShowingList: List<MovieDetailsResponse.Mb.Cast>,
    private var context: Activity
) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    private var rowIndex = 0
    inner class ViewHolder(val binding: ItemDetailsCastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailsCastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (rowIndex == position && position == 0) {
                    Constant().setMargins(holder.itemView,60,0,0,0)
                }

                //title
                binding.textView54.text = this.name

                //subTitle
                if (this.character == "") {
                    binding.textView80.hide()
                } else {
                    binding.textView80.show()
                    binding.textView80.text = this.character
                }

                //Image
                Glide.with(context)
                    .load("https://" + this.poster)
                    .error(R.drawable.placeholder_vertical)
                    .into(binding.imageView30)

//                click
                holder.itemView.setOnClickListener {
                    rowIndex == position
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

}