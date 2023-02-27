package com.net.pvr.ui.movieDetails.comingSoonDetails.adapter

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr.utils.Constant

@Suppress("DEPRECATION")
class ComDetailsHomePhAdapter(
    private val context: Activity,
    private val movies: List<CommingSoonResponse.Output.Ph>,
) : RecyclerView.Adapter<ComDetailsHomePhAdapter.MovieViewHolder>() {
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_promotion, parent, false)
        // create ViewHolder
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return MovieViewHolder(view)
    }


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var promotionImage = view.findViewById<ImageView>(R.id.sliderImg)!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]
        Glide.with(context)
            .load(obj.i)
            .error(R.drawable.dummy_prmotion)
            .into(holder.promotionImage)


        if (movies.size > 1) {
            if (movies.size == 2) {
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
            } else if (position == movies.size - 1) {
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