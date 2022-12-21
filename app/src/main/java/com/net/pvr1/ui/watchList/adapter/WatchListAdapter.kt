package com.net.pvr1.ui.watchList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.databinding.WatchlistItemBinding
import com.net.pvr1.ui.watchList.response.WatchListResponse

//category

class WatchListAdapter(
    private var nowShowingList: ArrayList<WatchListResponse.Output>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: WatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WatchlistItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (this.movieData!=null){
                 Glide.with(context).load(this.movieData.miv).into(binding.imageView114)
                //date
                val date: String = this.movieData.date_caption
                val separated: Array<String> = date.split("on").toTypedArray()
                binding.textView283.text = separated[1]
                //genre
                var genre: String = this.movieData.genre
                genre = genre.replace(",", " \u2022 ")
                binding.textView284.text = genre
                }
                //title
                binding.textView282.text = this.movieName
                val str = this.cinemaCodes
                if (str == "") {
                    binding.textView285.text = "Alert set for all theaters in " + this.city
                } else {
                    val num: Int = str.replace("[^,]".toRegex(), "").length + 1
                    if (num == 1) binding.textView285.text =
                        "Alert set for " + num + " theater in " + this.city
                    else binding.textView285.text =
                        "Alert set for " + num + " theaters in " + this.city
                }

                binding.imageView115.setOnClickListener {
                    listener.deleteAlertClick(this)
                }
                binding.imageView136.setOnClickListener {
                    listener.itemClick(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun deleteAlertClick(comingSoonItem: WatchListResponse.Output)
        fun itemClick(comingSoonItem: WatchListResponse.Output)
    }

}