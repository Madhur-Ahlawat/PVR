package com.net.pvr.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemHomePromotionBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.utils.Constant


class BookingPlaceHolderAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Ph>,
    private var context: Activity,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<BookingPlaceHolderAdapter.ViewHolder>() {
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    inner class ViewHolder(val binding: ItemHomePromotionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomePromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.dummy_prmotion)
                    .into(binding.sliderImg)

                //click
                holder.itemView.setOnClickListener {
                    notifyDataSetChanged()
                    listener.placeHolder(this)
                }

                if (nowShowingList.size > 1) {
                    if (nowShowingList.size == 2) {
                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
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
                            itemWidth,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                        holder.itemView.layoutParams = layoutParams
                    } else if (position == nowShowingList.size - 1) {
                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                        holder.itemView.layoutParams = layoutParams
                    } else {
                        val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
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


    interface RecycleViewItemClickListenerCity {
        fun placeHolder(comingSoonItem: BookingResponse.Output.Ph)
    }

}