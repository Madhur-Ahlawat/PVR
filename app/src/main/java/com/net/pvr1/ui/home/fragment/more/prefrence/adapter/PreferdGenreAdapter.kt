package com.net.pvr1.ui.home.fragment.more.prefrence.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.GenreChildItemBinding
import com.net.pvr1.ui.home.fragment.more.prefrence.response.PreferenceResponse


class PreferdGenreAdapter(
    private var context: Context,
    private var nowShowingList: List<PreferenceResponse.Output.Genre.Liked>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<PreferdGenreAdapter.ViewHolder>() {
    private var rowIndex=0

    inner class ViewHolder(val binding: GenreChildItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                binding.textView242.text=this.na
                binding.imageView169.setImageResource(R.drawable.curve_select)
//                if (rowIndex==position){
//                    binding.view160.show()
//                    binding.textView243.setTextColor(context.getColor(R.color.black))
//                }else{
//                    binding.textView243.setTextColor(context.getColor(R.color.otherCityColor))
//                    binding.view160.invisible()
//                }
//
                //click
                holder.itemView.setOnClickListener {
                    rowIndex=position
                    listener.genreLikeClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun genreLikeClick(comingSoonItem: PreferenceResponse.Output.Genre.Liked)
    }

}