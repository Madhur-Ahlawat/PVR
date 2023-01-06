package com.net.pvr1.ui.home.fragment.home.adapter

import android.app.Activity
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemHomePromotionBinding
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.ui.player.PlayerActivity
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

@Suppress("DEPRECATION")
class PromotionAdapter(
    private val context: Activity,
    private val movies: List<HomeResponse.Ph>,
) : RecyclerView.Adapter<PromotionAdapter.ViewHolder>() {
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun getItemCount() = movies.size
    inner class ViewHolder(val binding: ItemHomePromotionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        val binding = ItemHomePromotionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(movies[position]) {
                //IMAGE
                Glide.with(context).load(this.i).error(R.drawable.dummy_prmotion)
                    .into(binding.sliderImg)

                //ITEM WIDTH
                if (movies.size > 1) {
                    if (movies.size == 2) {
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
                    } else if (position == movies.size - 1) {
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
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = Constant().convertDpToPixel(13F, context)
                    layoutParams.rightMargin = Constant().convertDpToPixel(13F, context)
                    holder.itemView.layoutParams = layoutParams
                }

                //Manage Functions
                if (this.type == "IMAGE" && this.redirectView == "DEEPLINK") {
                    binding.tvPlay.hide()
                    Constant().shareData(context, "", this.redirectView)

                } else if (this.type == "IMAGE" && this.redirectView == "INAPP") {
                    binding.tvPlay.hide()
                    //click
                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, WebViewActivity::class.java)
                        intent.putExtra("from", "more")
                        intent.putExtra("title", context.getString(R.string.terms_condition_text))
                        intent.putExtra("getUrl", Constant.termsCondition)
                        context.startActivity(intent)

                    }

                } else if (this.type == "VIDEO" && this.redirectView != "") {
                    binding.tvPlay.show()
                    //click
                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, PlayerActivity::class.java)
                        intent.putExtra("trailerUrl", this.trailerUrl)
                        context.startActivity(intent)
                    }
                } else {
                    binding.tvPlay.hide()

                }


            }
        }
    }
}