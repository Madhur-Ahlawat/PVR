package com.net.pvr1.ui.home.fragment.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemHomeCinemaListBinding
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

class HomeMoviesAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Mv>,
    private var listener: RecycleViewItemClickListener,
    private var singleCheck: Boolean,

    ) : RecyclerView.Adapter<HomeMoviesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeCinemaListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeCinemaListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Image
                val lastIndex = nowShowingList.size - 1
                if (singleCheck && position == lastIndex) {
                    binding.imageHori.show()
                    binding.imageView16.hide()
                    Glide.with(context)
                        .load(this.mih)
                        .error(R.drawable.placeholder_horizental)
                        .into(binding.imageHori)
                } else {
                    binding.imageHori.hide()
                    binding.imageView16.show()
                    Glide.with(context)
                        .load(this.miv)
                        .error(R.drawable.placeholder_vertical)
                        .into(binding.imageView16)
                }

                //title
                binding.textView42.text = this.n

                //Movie Click
                binding.imageView16.setOnClickListener {
                    listener.onMoviesClick(this)
                }

                //Book Click
                binding.textView46.setOnClickListener {
                    listener.onBookClick(this)
                }

//New Release
                if (this.rt !=""){
                    binding.tvNewRe.show()
                    binding.tvNewRe.text= this.rt
                }else{
                    binding.tvNewRe.hide()
                }

                //trailer
                if (this.mtrailerurl.isNotEmpty()) {
                    binding.imageView35.show()
                } else {
                    binding.imageView35.hide()
                }

                binding.imageView35.setOnClickListener {
                    listener.onTrailerClick(this.mtrailerurl,this)
                }

                censorLanguage(
                    this.otherlanguages,
                    this.lng,
                    this.ce,
                    binding.textView43,
                    context,
                    binding.textView43)


 //Manage Genre
                if (this.othergenres != "") {
                    if (this.othergenres.split(",").size > 2) {
                        binding.genrePlus.show()
                        binding.genrePlus.text = "+" + (this.othergenres.split(",").size - 2)
                        binding.textView45.text =
                            this.othergenres.split(",")[0] + " | " + this.othergenres.split(
                                ","
                            )[1]
                    } else {
                        binding.genrePlus.hide()
                        binding.textView45.text = this.othergenres.replace(",", " | ")
                    }
                } else {
                    binding.textView45.text = this.tag
                }


            }
        }
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
                stringBuilder.append(
                    otherLang.split(",").toTypedArray()[0] + " | " + otherLang.split(",")
                        .toTypedArray()[1]
                )
            } else {
//                otherLanguage.hide()
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

    interface RecycleViewItemClickListener {
        fun onTrailerClick(comingSoonItem: String, trs: HomeResponse.Mv)
        fun onMoviesClick(comingSoonItem: HomeResponse.Mv)
        fun onBookClick(comingSoonItem: HomeResponse.Mv)
    }

}