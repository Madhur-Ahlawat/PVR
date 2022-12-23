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


class FavouriteTheaterAdapter(
    private var context: Context,
    private var nowShowingList: List<PreferenceResponse.Output.Genre.Liked>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<FavouriteTheaterAdapter.ViewHolder>() {
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
                binding.imageView68.setImageResource(R.drawable.ic_favourite_theatre)
//                if (rowIndex==position){
//                    binding.imageView68.setImageResource(R.drawable.ic_favourite_theatre)
//                }else{
//                    binding.imageView68.setColorFilter(ContextCompat.getColor(context, R.color.textColor), android.graphics.PorterDuff.Mode.MULTIPLY);
//                    binding.imageView68.setImageResource(R.drawable.ic_un_favourite_theatre)
//                }
                //click
                holder.itemView.setOnClickListener {
                    rowIndex=position
                    listener.favouriteTheaterClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun favouriteTheaterClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked)
    }

}