package com.net.pvr.ui.home.fragment.more.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.ProfileItemBinding
import com.net.pvr.ui.home.fragment.more.model.ProfileModel
import com.net.pvr.utils.hide
import com.net.pvr.utils.show


class ProfileCompleteAdapter(
    private var context: Context,
    private var profileList: ArrayList<ProfileModel>,
    private var params: List<String>
) : RecyclerView.Adapter<ProfileCompleteAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ProfileItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(profileList[position]) {
                val profileModel: ProfileModel = profileList[position]
                if (position == 0) {
                    binding.line.hide()
                } else {
                    binding.line.show()
                }
                if (params.contains(profileModel.type)) {
                    binding.check.setImageResource(R.drawable.check_circle_profile)
                    binding.line.setImageResource(R.drawable.line_check)
                } else {
                    binding.check.setImageResource(R.drawable.unchecked_profile)
                    binding.line.setImageResource(R.drawable.line_uncheck)
                }
                binding.countText.text = profileModel.name

                //click
                holder.itemView.setOnClickListener {
//                    listener.alsoPlaying(this,position)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (profileList.isNotEmpty()) profileList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun alsoPlaying(comingSoonItem: String, position: Int)
    }

}