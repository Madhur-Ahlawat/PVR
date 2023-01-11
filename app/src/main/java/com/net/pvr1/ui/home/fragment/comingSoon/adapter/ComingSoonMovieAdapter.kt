package com.net.pvr1.ui.home.fragment.comingSoon.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.comingSoon.response.CommingSoonResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class ComingSoonMovieAdapter(
    private var nowShowingList: List<CommingSoonResponse.Output.Movy>,
    private var context: Context,
    private var listener: VideoPlay,
    private var checkLogin: Boolean
) : RecyclerView.Adapter<ComingSoonMovieAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.coming_soon_item_layout, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        try {
            val comingSoonItem = nowShowingList[position]
            //title
            holder.title.text = comingSoonItem.name
            //Image
            Glide.with(context).load(comingSoonItem.miv).error(R.drawable.placeholder_vertical)
                .into(holder.image)

            //Manage Video Play
            if ((comingSoonItem.videoUrl.contains("v=") || comingSoonItem.videoUrl.contains("list=")) && !TextUtils.isEmpty(
                    comingSoonItem.videoUrl
                )
            )
            {
                holder.play.show()
            } else {
                holder.play.hide()
            }

            //Video Play Click
            holder.play.setOnClickListener {
                listener.onTrailerClick(comingSoonItem)
            }
            //data click
            holder.itemView.setOnClickListener {
                listener.onDateClick(comingSoonItem)
            }
            //Manage Bookmark
            if (!checkLogin) {
                if (comingSoonItem.ul) {
                    holder.wishlist.show()
                    holder.wishlist.background =
                        ContextCompat.getDrawable(context, R.drawable.ic_wishlist_yellow)
                } else {
                    holder.wishlist.show()
                    holder.wishlist.background =
                        ContextCompat.getDrawable(context, R.drawable.ic_wishlist_white)
                }
            }

//        //Release Manage
            holder.release.text = comingSoonItem.date_caption
            if (comingSoonItem.date_caption == "") {
                holder.release.hide()
            } else {
                holder.release.show()
            }

//        //Manage Language
            if (comingSoonItem.masterMovieId == "NHO00017887") {
                println("NHO00021636--->${comingSoonItem.otherlanguages}${comingSoonItem.language}${comingSoonItem.censor}")
            } else {
                censorLanguage(
                    comingSoonItem.otherlanguages,
                    comingSoonItem.language,
                    comingSoonItem.censor,
                    holder.language,
                    context,
                    holder.otherLanguage
                )
            }
//        //Manage Genre
            if (comingSoonItem.othergenres != "") {
                if (comingSoonItem.othergenres.split(",").size > 2) {
                    holder.otherGenre.show()
                    holder.otherGenre.text = "+" + (comingSoonItem.othergenres.split(",").size - 2)
                    holder.genre.text =
                        comingSoonItem.othergenres.split(",")[0] + " | " + comingSoonItem.othergenres.split(
                            ","
                        )[1]
                } else {
                    holder.otherGenre.hide()
                    holder.genre.text = comingSoonItem.othergenres.replace(",", " | ")
                }
            } else {
                holder.genre.text = comingSoonItem.genre
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var image: ImageView = view.findViewById(R.id.image)
        var play: ImageView = view.findViewById(R.id.ivPlay)
        var release: TextView = view.findViewById(R.id.tvRelease)
        var language: TextView = view.findViewById(R.id.tvCensorLang)
        var otherLanguage: TextView = view.findViewById(R.id.languagePlus)
        var genre: TextView = view.findViewById(R.id.tvGenre)
        var otherGenre: TextView = view.findViewById(R.id.genrePlus)
        var wishlist: ImageView = view.findViewById(R.id.ivWishlist)
    }

    interface VideoPlay {
        fun onDateClick(comingSoonItem: CommingSoonResponse.Output.Movy)
        fun onTrailerClick(comingSoonItem: CommingSoonResponse.Output.Movy)
    }

    @SuppressLint("SetTextI18n")
    private fun censorLanguage(
        otherLang: String?,
        lang: String?,
        censor: String,
        tvCensorLang: TextView,
        activityContext: Context,
        otherLanguage: TextView
    ) {
        try {
            val stringBuilder = StringBuilder()
            var isChange = false
            if (censor.replace("\\(".toRegex(), "").replace("\\)".toRegex(), "")
                    .equals("A", ignoreCase = true)
            ) isChange = true
            stringBuilder.append(
                censor.replace("\\(".toRegex(), "").replace("\\)".toRegex(), "") + " • "
            )
            if (otherLang != null && otherLang != "") {
                if (otherLang.split(",").toTypedArray().size > 2) {
                    otherLanguage.visibility = View.VISIBLE
                    otherLanguage.text = "+" + (otherLang.split(",").toTypedArray().size - 2)
                    stringBuilder.append(
                        otherLang.split(",").toTypedArray()[0] + " | " + otherLang.split(",")
                            .toTypedArray()[1]
                    )
                } else {
                    otherLanguage.hide()
                    stringBuilder.append(otherLang.replace(",".toRegex(), " | "))
                }
            } else {
                stringBuilder.append(lang)
            }
            if (!isChange) tvCensorLang.text = stringBuilder else Constant().spannableText(
                activityContext as Activity, stringBuilder, tvCensorLang
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}