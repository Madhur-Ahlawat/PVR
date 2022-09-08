package com.net.pvr1.ui.home.experience

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ExperienceReactBinding
import com.net.pvr1.ui.home.experience.model.ReactModel


class ExperienceReactAdapter(
    private var nowShowingList: Array<ReactModel>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<ExperienceReactAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ExperienceReactBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExperienceReactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Image
                Glide.with(context)
                    .load(this.image)
                    .into(binding.imageView87)

                //click
                holder.itemView.setOnClickListener {
                    Glide.with(context)
                        .load(R.drawable.experience_select)
                        .into(binding.imageView87)

                    listener.reactClick(this)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun reactClick(comingSoonItem: ReactModel)
    }

}