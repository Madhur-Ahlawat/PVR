package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemCinemaDetailsCinemasBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse


class CinemaSessionNearTheaterAdapter(
    private var nowShowingList: List<CinemaNearTheaterResponse.Output.C>,
    private var context: Context,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CinemaSessionNearTheaterAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaDetailsCinemasBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsCinemasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                //title
                binding.textView100.text=this.n
                //address
                binding.textView101.text=this.ad
                //Distance
                binding.textView86.text= this.d
                //Shows
                binding.textView85.text = "${this.sc} Shows"
                //Image
                Glide.with(context)
                    .load(this.iwt)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView52)
                //Direction
                binding.view64.setOnClickListener {
                    holder.itemView.setOnClickListener {listener.nearTheaterDirectionClick(this)  }
                }
                //click
                holder.itemView.setOnClickListener {listener.nearTheaterClick(this)  }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun nearTheaterClick(comingSoonItem: CinemaNearTheaterResponse.Output.C)
        fun nearTheaterDirectionClick(comingSoonItem: CinemaNearTheaterResponse.Output.C)
    }

}