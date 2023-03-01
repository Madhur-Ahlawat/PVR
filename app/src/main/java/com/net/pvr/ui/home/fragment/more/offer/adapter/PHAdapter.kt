package com.net.pvr.ui.home.fragment.more.offer.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr.utils.Constant

@Suppress("DEPRECATION")
class PHAdapter(
    context: Activity,
    var exmovies: ArrayList<MOfferResponse.Output.Pu>,
    listener: OpenRedirection,
    type: Int
) : RecyclerView.Adapter<PHAdapter.ViewHolder>() {
    var context: Activity
    var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    private var type = 0
    private val listener: OpenRedirection

    init {
        this.type = type
        this.context = context
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        // create a new view
        var itemLayoutView: View? = null
        itemLayoutView = if (type == 1) {
            LayoutInflater.from(parent.context).inflate(R.layout.ph_slider_item, null)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.ph_slider_item_new, null)
        }
        // create ViewHolder
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieVO = exmovies[position]
        if (movieVO.type.equals(
                "VIDEO", ignoreCase = true
            ) && movieVO.trailerUrl != null && !movieVO.trailerUrl.equals("", ignoreCase = true)
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
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else if (position == exmovies.size - 1) {
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else {
                    val itemWidth = ((screenWidth - 0) / 1.25f).toInt()
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
                layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            }
        } else {
            if (exmovies.size > 1) {
                if (exmovies.size == 2) {
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
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
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                    val layoutParams = ConstraintLayout.LayoutParams(
                        itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                    holder.itemView.layoutParams = layoutParams
                } else if (position == exmovies.size - 1) {
                    val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
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
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                holder.itemView.layoutParams = layoutParams
            }
        }
        holder.itemView.setOnClickListener { view: View? -> listener.onOpen(movieVO) }
        Glide.with(context).load(movieVO.i).into(holder.sliderImg)
    }

    override fun getItemCount(): Int {
        return exmovies.size
    }

    interface OpenRedirection {
        fun onOpen(exmovieVO: MOfferResponse.Output.Pu?)
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
}