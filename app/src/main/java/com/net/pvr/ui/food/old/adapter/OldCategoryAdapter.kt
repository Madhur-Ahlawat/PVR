package com.net.pvr.ui.food.old.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.OldFoodCateItemBinding
import com.net.pvr.utils.invisible
import com.net.pvr.utils.show

//category

class OldCategoryAdapter(
    private var nowShowingList: List<String>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<OldCategoryAdapter.ViewHolder>() {
    private var rowIndex = 0

    inner class ViewHolder(val binding: OldFoodCateItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OldFoodCateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Image
                if (rowIndex == position) {
                    binding.textView380.setTextColor(context.getColor(R.color.yellow))
                    binding.view174.setBackgroundColor(context.getColor(R.color.yellow))
                    binding.view174.show()
                } else {
                    binding.textView380.setTextColor(context.getColor(R.color.black))
                    binding.view174.invisible()
                    binding.view174.setBackgroundColor(context.getColor(R.color.grayEd))
                }

                //Name
                if (this != "")
                binding.textView380.text = this
                holder.itemView.setOnClickListener {
                    rowIndex = position
                    listener.categoryClick(this)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList?.isNotEmpty() == true) nowShowingList?.size!! else 0
    }


    interface RecycleViewItemClickListener{
        fun categoryClick(string: String)

    }

}