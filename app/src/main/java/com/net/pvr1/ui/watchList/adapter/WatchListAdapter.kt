package com.net.pvr1.ui.watchList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.WatchlistItemBinding
import com.net.pvr1.ui.watchList.response.WatchListResponse

//category

class WatchListAdapter(
    private var nowShowingList: ArrayList<WatchListResponse.Output>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {
    private var rowIndex = 0

    inner class ViewHolder(val binding: WatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WatchlistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
//                Glide.with(context)
//                    .load(this.movieData.image)
//                    .into(binding.imageView114)

                //title
                binding.textView282.text=this.movieData.genre
                //date
                binding.textView283.text=this.movieData.date_caption
                //rating
                binding.textView284.text=this.movieData.genre+context.getString(R.string.dots)+this.movieData.censor

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun watchListClick(comingSoonItem: String)
    }

}