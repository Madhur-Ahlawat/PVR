package com.net.pvr1.ui.payment.starPass.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.StarPassItemBinding
import com.net.pvr1.ui.payment.starPass.StarPasModel
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

//category

class StarPassAdapter(
    private var nowShowingList: ArrayList<StarPasModel>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<StarPassAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: StarPassItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StarPassItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        with(holder) {
//            with(nowShowingList[position]) {
//                binding.textInputLayout.hint = this.name
//                binding.Count.text = nowShowingList.size.toString()
//
//                if (position == nowShowingList.size - 1) {
//                    binding.count.show()
//                } else {
//                    binding.count.hide()
//                }
//
//                binding.minus.setOnClickListener {
//                    listener.starPassMinusClick(this)
//                }
//                binding.plus.setOnClickListener {
//                    listener.starPassAddClick(this)
//
//                }
//            }
//        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun starPassAddClick(comingSoonItem: StarPasModel)
        fun starPassMinusClick(comingSoonItem: StarPasModel)
    }

}