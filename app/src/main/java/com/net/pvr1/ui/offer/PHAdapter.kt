package com.net.pvr1.ui.offer

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.offer.response.MOfferResponse
import com.net.pvr1.utils.Util.convertDpToPixel

@Suppress("DEPRECATION")
class PHAdapter(
    context: Activity,
    var exmovies: ArrayList<MOfferResponse.Output.Ph>,
    type: Int
) : RecyclerView.Adapter<PHAdapter.ViewHolder>() {
    var context: Activity
    var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    private var type = 0
//    private val listener: OpenRedirection

    init {
        this.type = type
        this.context = context
//        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        var itemLayoutView: View? = null
        itemLayoutView = if (type == 1) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ph_slider_item, null)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ph_slider_item_new, null)
        }
        // create ViewHolder
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("datasize-->" + exmovies.size)
        val exmovieVO = exmovies[position]
        if (exmovieVO.type.equals(
                "VIDEO",
                ignoreCase = true
            ) && !exmovieVO.trailerUrl.equals("", ignoreCase = true)
        ) {
            holder.tv_play.visibility = View.VISIBLE
        } else {
            holder.tv_play.visibility = View.GONE
        }
        if (type == 0) {
            if (exmovies.size > 1) {
                if (exmovies.size == 2) {
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    if (position == 0) {
                        layoutParams.leftMargin = convertDpToPixel(13f, context)
                        layoutParams.rightMargin = convertDpToPixel(1f, context)
                    } else {
                        layoutParams.leftMargin = convertDpToPixel(1f, context)
                        layoutParams.rightMargin = convertDpToPixel(13f, context)
                    }
                    holder.itemView.layoutParams = layoutParams
                } else if (position == 0) {
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else if (position == exmovies.size - 1) {
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.rightMargin = convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else {
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.rightMargin = convertDpToPixel(1f, context)
                    layoutParams.leftMargin = convertDpToPixel(1f, context)
                    holder.itemView.layoutParams = layoutParams
                }
            } else {
//            int itemWidth = (int) ((screenWidth - 0)/(1.10f));
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = convertDpToPixel(13f, context)
                layoutParams.rightMargin = convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            }
        } else {
            if (exmovies.size > 1) {
                if (exmovies.size == 2) {
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    if (position == 0) {
                        layoutParams.leftMargin = convertDpToPixel(13f, context)
                        layoutParams.rightMargin = convertDpToPixel(1f, context)
                    } else {
                        layoutParams.leftMargin = convertDpToPixel(1f, context)
                        layoutParams.rightMargin = convertDpToPixel(13f, context)
                    }
                    holder.itemView.layoutParams = layoutParams
                } else if (position == 0) {
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else if (position == exmovies.size - 1) {
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.rightMargin = convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else {
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.rightMargin = convertDpToPixel(1f, context)
                    layoutParams.leftMargin = convertDpToPixel(1f, context)
                    holder.itemView.layoutParams = layoutParams
                }
            } else {
//            int itemWidth = (int) ((screenWidth - 0)/(1.10f));
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = convertDpToPixel(13f, context)
                layoutParams.rightMargin = convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            }
        }
//        holder.itemView.setOnClickListener { listener.onOpen(exmovieVO) }
        // holder.constraintLayout.getLayoutParams().width = itemWidth;

        Glide.with(context).load(exmovieVO.i).into(holder.sliderImg)

//        ImageLoader.getInstance()
//            .displayImage(exmovieVO.i, , PCApplication.landingRectangleImageOption)
    }

    override fun getItemCount(): Int {
        return exmovies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sliderImg: ImageView
        var tv_play: ImageView
        var constraintLayout: ConstraintLayout

        init {
            sliderImg = itemView.findViewById(R.id.sliderImg)
            tv_play = itemView.findViewById(R.id.tv_play)
            constraintLayout = itemView.findViewById(R.id.constraintLayout)
        }
    }

    interface OpenRedirection {
        fun onOpen(exmovieVO: MOfferResponse.Output.Pu?)
    }

    companion object {
        const val TAG = "PCComingSoonAdapter"
    }
}