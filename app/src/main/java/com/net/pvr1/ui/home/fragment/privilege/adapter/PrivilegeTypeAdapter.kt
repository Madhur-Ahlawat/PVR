package com.net.pvr1.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.PrivilegeImageItemBinding
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.utils.Constant
import org.json.JSONException


@Suppress("DEPRECATION")
class PrivilegeTypeAdapter(
    private var nowShowingList: ArrayList<PrivilegeHomeResponse.Output.Pinfo>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListener,
    private var recyclerView: RecyclerView?
) : RecyclerView.Adapter<PrivilegeTypeAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    private var rowIndex = 0


    inner class ViewHolder(val binding: PrivilegeImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        val binding = PrivilegeImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Language  .lng+this.lk
                Glide.with(context)
                    .load(this.pi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView15)

                //click
                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            rowIndex= position
                            try {
                                listener.privilegeTypeScroll(nowShowingList,position)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                })

                if (nowShowingList.size > 1) {
                    if (nowShowingList.size == 2) {
                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        if (position == 0) {
                            layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                            layoutParams.rightMargin = Constant().convertDpToPixel(1f, context)
                        } else {
                            layoutParams.leftMargin = Constant().convertDpToPixel(1f, context)
                            layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                        }
                        holder.itemView.layoutParams = layoutParams
                    } else if (position == 0) {
                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                        holder.itemView.layoutParams = layoutParams
                    } else if (position == nowShowingList.size - 1) {
                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                        holder.itemView.layoutParams = layoutParams
                    } else {
                        val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.rightMargin = Constant().convertDpToPixel(1f, context)
                        layoutParams.leftMargin = Constant().convertDpToPixel(1f, context)
                        holder.itemView.layoutParams = layoutParams
                    }
                } else {
//            int itemWidth = (int) ((screenWidth - 0)/(1.10f));
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = Constant().convertDpToPixel(13F, context)
                    layoutParams.rightMargin = Constant().convertDpToPixel(13F, context)
                    holder.itemView.layoutParams = layoutParams
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun privilegeTypeScroll(
            nowShowingList: ArrayList<PrivilegeHomeResponse.Output.Pinfo>,
            position: Int
        )
    }

}