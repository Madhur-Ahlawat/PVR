package com.net.pvr1.ui.home.fragment.more.contactUs.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ContactUsItemBinding

//category

class ContactUsItemAdapter(
    private var nowShowingList: ArrayList<String>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<ContactUsItemAdapter.ViewHolder>() {
    private var rowIndex = 0

    inner class ViewHolder(val binding: ContactUsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContactUsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView301.text = this

                if (rowIndex == position) {
                    binding.imageView143.setImageResource(R.drawable.curve_select)
                    binding.constraintLayout102.setBackgroundResource(R.drawable.ui_item_select)
                } else {
                    binding.imageView143.setImageResource(R.drawable.curve_unselect)
                    binding.constraintLayout102.setBackgroundResource(R.drawable.ui_item_unselect)

                }

                itemView.setOnClickListener {
                    rowIndex = position
                    listener.contactUsClick(this)
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun contactUsClick(comingSoonItem: String)
    }

}