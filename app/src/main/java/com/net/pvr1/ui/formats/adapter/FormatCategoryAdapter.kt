package com.net.pvr1.ui.formats.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.databinding.FormatsItemBinding
import com.net.pvr1.ui.formats.response.FormatResponse

//category

class FormatCategoryAdapter(
    private var nowShowingList: ArrayList<FormatResponse.Output.Ph>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<FormatCategoryAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    inner class ViewHolder(val binding: FormatsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        val binding = FormatsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                val lp = holder.itemView.layoutParams
                lp.height = lp.height
                lp.width = ((screenWidth-40)/1.17f).toInt()
                holder.itemView.layoutParams = lp

                //title
                Glide.with(context)
                    .load(this.i)
                    .into(binding.imageView141)

                itemView.setOnClickListener {
                    listener.categoryClick(this)
              }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun categoryClick(comingSoonItem: FormatResponse.Output.Ph)
    }

}