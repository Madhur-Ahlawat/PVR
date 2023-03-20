package com.net.pvr.ui.ticketConfirmation.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.ui.player.PlayerActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.ga.GoogleAnalytics
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class TicketPlaceHolderAdapter(
    private val context: Activity,
    private val movies: ArrayList<HomeResponse.Ph>,
) : RecyclerView.Adapter<TicketPlaceHolderAdapter.MovieViewHolder>() {

    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.placeholder_big_item, parent, false)
        // create ViewHolder
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return MovieViewHolder(view)
    }


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var promotionImage = view.findViewById<ImageView>(R.id.sliderImg)!!
        var tv_play = view.findViewById<ImageView>(R.id.tv_play)!!
        var imageView142 = view.findViewById<ImageView>(R.id.imageView142)!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]
        Glide.with(context)
            .load(obj.i)
            .error(R.drawable.dummy_prmotion)
            .into(holder.promotionImage)
        if (obj.type == "VIDEO"){
            holder.tv_play.show()
        }else{
            holder.tv_play.hide()
        }

        if (obj.buttonText == "VR"){
            holder.imageView142.show()
        }else{
            holder.imageView142.hide()
        }

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

        //Manage Functions
        holder.itemView.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Ticket Confirmation")
                bundle.putString("var_promotional_banner_Name", obj.name)
                bundle.putString("var_promotional_banner_from",obj.screen)
                GoogleAnalytics.hitEvent(context, "promotional_banner", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Ticket Confirmation")
                bundle.putString("var_thankyou_promotion", obj.name)
                GoogleAnalytics.hitEvent(context, "thankyou_promotion_banner", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }


            if (obj.type == "IMAGE" && obj.redirectView == "DEEPLINK") {
                if (obj.redirect_url.equals("", ignoreCase = true)) {
                    if (obj.redirect_url.lowercase(Locale.ROOT).contains("/loyalty/home")) {
                        val navigationView = HomeActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
                        val bottomMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
                        val newView = bottomMenuView.getChildAt(2)
                        val itemView = newView as BottomNavigationItemView
                        itemView.performClick()
                    } else {
                        val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                obj.redirect_url.replace(
                                    "https", "app"
                                )
                            )
                        )
                        context.startActivity(intent)
                    }
                }

            } else if (obj.type == "IMAGE" && obj.redirectView == "INAPP") {

                if (obj.buttonText == "VR"){
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("title", obj.name)
                    intent.putExtra("from", "Experience")
                    intent.putExtra("getUrl", obj.redirect_url)
                    context.startActivity(intent)
                }else {
                    //click
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("from", "more")
                    intent.putExtra(
                        "title", context.getString(R.string.terms_condition_text)
                    )
                    intent.putExtra("getUrl", obj.redirect_url)
                    context.startActivity(intent)
                }

            } else if (obj.type == "VIDEO" && obj.redirectView != "") {

                //click
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra("trailerUrl", obj.trailerUrl)
                context.startActivity(intent)

            } else if (obj.type == "IMAGE" && obj.redirectView == "WEB") {

                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(obj.redirect_url))
                context.startActivity(intent)
            }
        }


    }
}