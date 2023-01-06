package com.net.pvr1.ui.home.fragment.more.prefrence.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.PreferenceTheatersItemBinding
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr1.utils.hide


class AllTheaterAdapter(
    private var context: Context,
    private var nowShowingList: List<PreferenceResponse.Output.Genre.Other>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<AllTheaterAdapter.ViewHolder>() {
    private var rowIndex=-1

    inner class ViewHolder(val binding: PreferenceTheatersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PreferenceTheatersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                binding.textView50.text=this.na
//                binding.textView53.text=this.na
//                binding.textView51.text=this.na
                binding.textView53.hide()
                binding.textView51.hide()

                if (rowIndex==position){
                    binding.imageView68.setImageResource(R.drawable.like)
                    binding.constraintLayout142.setBackgroundResource(R.drawable.ui_item_select)
                }else{
                    binding.constraintLayout142.setBackgroundResource(R.drawable.ui_item_unselect)
                    binding.imageView68.setImageResource(R.drawable.white_round_shape)
                }

                //click
                holder.itemView.setOnClickListener {
                    rowIndex=position
                    listener.allTheaterClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun allTheaterClick(comingSoonItem: PreferenceResponse.Output.Genre.Other)
    }

}