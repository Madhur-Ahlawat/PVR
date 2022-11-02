package com.net.pvr1.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.PrivilegeHomeItemBinding
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse


class PrivilegeHomeAdapter(
    private var nowShowingList: List<PrivilegeHomeResponse.Output.Pinfo>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<PrivilegeHomeAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PrivilegeHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PrivilegeHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language  .lng+this.lk
                Glide.with(context)
                    .load(this.pi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView110)
                //title
                binding.textView274.text=this.ph
                //desc
                binding.textView275.text=this.psh
                //click
                holder.itemView.setOnClickListener {listener.privilegeHomeClick(this)  }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun privilegeHomeClick(comingSoonItem: PrivilegeHomeResponse.Output.Pinfo)
    }

}