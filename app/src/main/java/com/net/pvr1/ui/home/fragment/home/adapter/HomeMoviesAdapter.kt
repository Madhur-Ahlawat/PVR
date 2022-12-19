package com.net.pvr1.ui.home.fragment.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide


class HomeMoviesAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Mv>,
    private var listener: RecycleViewItemClickListener,

    ) : RecyclerView.Adapter<HomeMoviesAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_cinema_list, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context).load(comingSoonItem.miv).error(R.drawable.app_icon).into(holder.image)

        holder.title.text = comingSoonItem.n
        holder.rating.text =
            comingSoonItem.ce +" "+ context.getString(R.string.dots) +" "+ comingSoonItem.lng
        holder.language.text = comingSoonItem.tag
        //Movie Click
        holder.image.setOnClickListener {
            listener.onMoviesClick(comingSoonItem)
        }
        //Book Click
        holder.book.setOnClickListener {
            listener.onBookClick(comingSoonItem)
        }

        censorLanguage(
            comingSoonItem.otherlanguages,
            comingSoonItem.lng,
            "",
            holder.language,
            context,
            holder.language
        )

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
    }


    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView16)
        var title: TextView = view.findViewById(R.id.textView42)
        var rating: TextView = view.findViewById(R.id.textView43)
        var language: TextView = view.findViewById(R.id.textView45)
        var book: TextView = view.findViewById(R.id.textView46)

    }

    interface RecycleViewItemClickListener {
        fun onMoviesClick(comingSoonItem: HomeResponse.Mv)
        fun onBookClick(comingSoonItem: HomeResponse.Mv)
    }

}