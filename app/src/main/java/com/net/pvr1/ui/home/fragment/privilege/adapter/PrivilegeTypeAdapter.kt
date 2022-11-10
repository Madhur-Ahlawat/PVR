package com.net.pvr1.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.PrivilegeImageItemBinding
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.utils.printLog


@Suppress("DEPRECATION")
class PrivilegeTypeAdapter(
    private var nowShowingList: List<PrivilegeHomeResponse.Output.Pinfo>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListener,
    private var recyclerView: RecyclerView?
) :
    RecyclerView.Adapter<PrivilegeTypeAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    inner class ViewHolder(val binding: PrivilegeImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        val binding =
            PrivilegeImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                // for side show space
                val lp = holder.itemView.layoutParams
                lp.height = lp.height
                lp.width = ((screenWidth - 40) / 1.17f).toInt()
                holder.itemView.layoutParams = lp

                //Language  .lng+this.lk
                Glide.with(context)
                    .load(this.pi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView15)
                //click
                var overallXScroll = 0
                recyclerView?.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        overallXScroll += dx
                        context.printLog("recycler--->${overallXScroll}")
                        listener.privilegeHomeClick(this)
                    }
                })
//                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                            listener.privilegeHomeClick(this)
//                        }
//                    }
//                })
                holder.itemView.setOnClickListener {
//                    listener.privilegeHomeClick(this)
                                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun privilegeHomeClick(comingSoonItem: RecyclerView.OnScrollListener)
    }

}