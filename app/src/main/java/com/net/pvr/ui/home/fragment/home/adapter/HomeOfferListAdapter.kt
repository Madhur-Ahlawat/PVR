package com.net.pvr.ui.home.fragment.home.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemHomeOfferBinding
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.printLog

@Suppress("DEPRECATION")
class HomeOfferListAdapter(
    private var context: Activity,
    private var nowShowingList: List<HomeResponse.Cp>,
    private var listener: RecycleViewItemClickListener,
) : RecyclerView.Adapter<HomeOfferListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        val binding = ItemHomeOfferBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Image
                Glide.with(context).load(this.i)
                    .error(R.drawable.placeholder_horizontal_movie)
                    .into(binding.imageView11)

                //Click
                holder.itemView.setOnClickListener {
                    listener.onOfferClick(this)
                }

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
                        binding.mainView.layoutParams = layoutParams
                    } else if (position == 0) {
                        printLog("abcd---" + position)

                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                        binding.mainView.layoutParams = layoutParams
                    } else if (position == nowShowingList.size - 1) {
                        printLog("abcd1---" + position)

                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                        binding.mainView.layoutParams = layoutParams
                    } else {
                        printLog("abcd2---" + position)

                        val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                        layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                        binding.mainView.layoutParams = layoutParams
                    }
                } else {
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = Constant().convertDpToPixel(13F, context)
                    layoutParams.rightMargin = Constant().convertDpToPixel(13F, context)
                    binding.mainView.layoutParams = layoutParams
                }

            }
        }


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun onOfferClick(comingSoonItem: HomeResponse.Cp)
    }

}