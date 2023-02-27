package com.net.pvr.ui.movieDetails.nowShowing.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemDetailsMusicBinding
import com.net.pvr.ui.movieDetails.nowShowing.response.MovieDetailsResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.show


class TrailerTrsAdapter(
    private var nowShowingList: ArrayList<MovieDetailsResponse.Trs>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<TrailerTrsAdapter.ViewHolder>() {
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
                binding.textView82.text = this.t
                //subTitle
                binding.textView83.show()
                binding.textView83.text = this.d
                //moreDetails
                holder.itemView.setOnClickListener {
                    rowIndex = position
                    listener.trailerTrsClick(this)
                    notifyDataSetChanged()
                }
                val videoId = Constant().extractYoutubeId(this.u)

                val imageUrl =
                    "https://img.youtube.com/vi/" + videoId.toString() + "/mqdefault.jpg" //

                Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView33)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun trailerTrsClick(comingSoonItem: MovieDetailsResponse.Trs)
    }

}