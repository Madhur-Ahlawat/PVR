package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemCinemaSessionMovieDetailsBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse


class CinemaSessionChildAdapter(
    private var nowShowingList: List<CinemaSessionResponse.Child.Mv>,
    private var context: Context
//    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CinemaSessionChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaSessionMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaSessionMovieDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView91.text = this.mn
                //time
                binding.textView92.text = this.mlength
                //genre
                binding.textView93.text = this.genres.joinToString { it }
                //Language
                binding.textView94.text = "Hindi Static"
                //Image
                Glide.with(context)
                    .load(this.miv)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView47)

                //RecyclerView
                val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                val cinemaSessionLanguageAdapter = CinemaSessionCinChildLanguageAdapter(this.ml, context, )
                binding.recyclerView17.layoutManager = layoutManager
                binding.recyclerView17.adapter = cinemaSessionLanguageAdapter

                //click
//                holder.itemView.setOnClickListener {listener.dateClick(this)  }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun dateClick(comingSoonItem: CinemaSessionResponse.Output.Bd)
    }

}