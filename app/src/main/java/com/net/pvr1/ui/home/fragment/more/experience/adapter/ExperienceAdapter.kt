package com.net.pvr1.ui.home.fragment.more.experience.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.FormatsItemBinding
import com.net.pvr1.ui.home.fragment.more.experience.response.ExperienceResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class ExperienceAdapter(
    private var nowShowingList: ArrayList<ExperienceResponse.Output.Format>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
) :
    RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FormatsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

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
                binding.constraintLayout165.show()

                //title
                binding.textView190.text= this.text

//                image
                Glide.with(context)
                    .load(this.imageUrl)
                    .error(R.drawable.placeholder_horizental)
                    .placeholder(R.drawable.placeholder_horizental)
                    .into(binding.imageView141)

                if (this.rurl!=""){
                    binding.imageView142.show()
                }else{
                    binding.imageView142.hide()
                }

                binding.imageView142.setOnClickListener {
                    listener.itemPlayerClick(this)
                }

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
        fun itemPlayerClick(comingSoonItem: ExperienceResponse.Output.Format)

        fun itemClick(comingSoonItem: ExperienceResponse.Output.Format)
    }

}