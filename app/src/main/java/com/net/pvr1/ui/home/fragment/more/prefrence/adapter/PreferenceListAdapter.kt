package com.net.pvr1.ui.home.fragment.more.prefrence.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.GenreCategoryBinding
import com.net.pvr1.utils.invisible
import com.net.pvr1.utils.show


class PreferenceListAdapter(
    private var context: Context,
    private var  nowShowingList: Array<String>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<PreferenceListAdapter.ViewHolder>() {
    private var rowIndex=0

    inner class ViewHolder(val binding: GenreCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                binding.textView243.text=this
                if (rowIndex==position){
                    binding.view160.show()
                    binding.textView243.setTextColor(context.getColor(R.color.black))
                }else{
                    binding.textView243.setTextColor(context.getColor(R.color.otherCityColor))
                    binding.view160.invisible()
                }
                
                //click
                holder.itemView.setOnClickListener {
                    rowIndex=position
                    listener.prefrenceTypeClick(this,position)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun prefrenceTypeClick(comingSoonItem: String, position: Int)
    }

}