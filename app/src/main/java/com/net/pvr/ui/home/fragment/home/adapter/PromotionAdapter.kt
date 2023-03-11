package com.net.pvr.ui.home.fragment.home.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ItemHomePromotionBinding
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import java.util.*

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
                Glide.with(context).load(this.i).error(R.drawable.placeholder_horizental)
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

                if (this.type == "VIDEO"){
                    binding.tvPlay.show()
                }else{
                    binding.tvPlay.hide()
                }

                //Manage Functions
                holder.itemView.setOnClickListener {
                    // Hit Event
                    try {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home Page")
                        bundle.putString("var_promotional_banner_Name", this.name)
                        bundle.putString("var_promotional_banner_from",this.screen)
                        GoogleAnalytics.hitEvent(context, "promotional_banner", bundle)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                    if (this.type == "IMAGE" && this.redirectView == "DEEPLINK") {
                        binding.tvPlay.hide()
                        if (this.redirect_url.equals("", ignoreCase = true)) {
                            if (this.redirect_url.lowercase(Locale.ROOT).contains("/loyalty/home")) {
                                val navigationView = HomeActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
                                val bottomMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
                                val newView = bottomMenuView.getChildAt(2)
                                val itemView = newView as BottomNavigationItemView
                                itemView.performClick()
                            } else {
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        this.redirect_url.replace(
                                            "https", "app"
                                        )
                                    )
                                )
                                context.startActivity(intent)
                            }
                        }
                    } else if (this.type == "IMAGE" && this.redirectView == "INAPP") {
                        binding.tvPlay.hide()
                        //click
                        val intent = Intent(context, WebViewActivity::class.java)
                        intent.putExtra("from", "more")
                        intent.putExtra(
                            "title",this.name
                        )
                        intent.putExtra("getUrl", this.redirect_url)
                        context.startActivity(intent)

                    } else if (this.type == "VIDEO" && this.redirectView != "") {

                        //click
                        val intent = Intent(context, PlayerActivity::class.java)
                        intent.putExtra("trailerUrl", this.trailerUrl)
                        context.startActivity(intent)

                    } else if (this.type == "IMAGE" && this.redirectView == "WEB") {

                        val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(this.redirect_url))
                        context.startActivity(intent)
                    } else {


                    }
                }


            }
        }
    }
}