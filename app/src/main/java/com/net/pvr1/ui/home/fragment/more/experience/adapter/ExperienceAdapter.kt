package com.net.pvr1.ui.home.fragment.more.experience.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.databinding.FormatsItemBinding
import com.net.pvr1.ui.home.fragment.more.experience.model.ExperienceResponse


class ExperienceAdapter(
    private var nowShowingList: ArrayList<ExperienceResponse.Output.Format>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FormatsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                val lp = holder.itemView.layoutParams
                lp.height = lp.height
                lp.width = ((screenWidth-40)/1.17f).toInt()
                holder.itemView.layoutParams = lp

                //title
                Glide.with(context)
                    .load(this.imageUrl)
                    .into(binding.imageView141)

                itemView.setOnClickListener {
                    listener.itemClick(this)
                }
                }
            }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun itemClick(comingSoonItem: ExperienceResponse.Output.Format)
    }

}