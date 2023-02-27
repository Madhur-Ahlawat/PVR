package com.net.pvr.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.PassportStoryBinding
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.squareup.picasso.Picasso


@Suppress("DEPRECATION")
class HowItWorkAdapter(
    private var nowShowingList: ArrayList<PrivilegeHomeResponse.Output.St>,
    private var context: Activity,
    private val listner:RecycleViewItemClickListener
) : RecyclerView.Adapter<HowItWorkAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PassportStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PassportStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists",
        "UseCompatLoadingForDrawables"
    )
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // holder
        val cardData = nowShowingList[position]
        if (position == 0) {
            holder.binding.firstView.show()
            holder.binding.otherView.hide()
            Picasso.get().load(cardData.pi).into(holder.binding.ivBannerl)
        } else {
            holder.binding.firstView.hide()
            holder.binding.otherView.show()
            Picasso.get().load(cardData.pi).into(holder.binding.ivBannerl1)
//                ImageLoader.getInstance().displayImage(exmovieVO.getPi(), holder.ivBannerl1, PCApplication.landingRectangleImageOption);
        }

        holder.binding.text1.text = cardData.text
        holder.binding.text.text = cardData.text
        holder.binding.btnProceed.setOnClickListener(View.OnClickListener {
            holder.binding.btnProceed.hide()
            listner.openRedirection()
        })
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun openRedirection()
    }

}