package com.net.pvr.ui.home.fragment.more.prefrence.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.GenreChildItemBinding
import com.net.pvr.ui.home.fragment.more.prefrence.response.PreferenceResponse


class AllGenreAdapter(
    private var context: Context,
    private var nowShowingList: ArrayList<PreferenceResponse.Output.Genre.Other>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<AllGenreAdapter.ViewHolder>() {
    private var rowIndex=-1

    inner class ViewHolder(val binding: GenreChildItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){

                binding.textView242.text=this.na
                if (this.liked){
                    binding.imageView169.setImageResource(R.drawable.curve_select)
                }else{
                    binding.imageView169.setImageResource(R.drawable.curve_unselect)
                }
//
                //click
                holder.itemView.setOnClickListener {
                    rowIndex=position
                    this.liked = true
                    listener.allGenreClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun allGenreClick(comingSoonItem: PreferenceResponse.Output.Genre.Other)
    }

}