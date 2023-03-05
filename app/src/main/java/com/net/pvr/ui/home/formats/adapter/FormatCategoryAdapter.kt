package com.net.pvr.ui.home.formats.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.FormatsItemBinding
import com.net.pvr.ui.home.formats.response.FormatResponse
import com.net.pvr.utils.Constant
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

//category
class FormatCategoryAdapter(
    private var nowShowingList: ArrayList<FormatResponse.Output.Ph>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListener, ) :

    RecyclerView.Adapter<FormatCategoryAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0

    inner class ViewHolder(val binding: FormatsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        val binding = FormatsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
//                if (nowShowingList.size > 1) {
//                    if (nowShowingList.size == 2) {
//                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
//                        val layoutParams = ConstraintLayout.LayoutParams(
//                            itemWidth,
//                            ConstraintLayout.LayoutParams.WRAP_CONTENT
//                        )
//                        if (position == 0) {
//                            layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
//                            layoutParams.rightMargin = Constant().convertDpToPixel(1f, context)
//                        } else {
//                            layoutParams.leftMargin = Constant().convertDpToPixel(1f, context)
//                            layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
//                        }
//                        holder.itemView.layoutParams = layoutParams
//                    } else if (position == 0) {
//                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
//                        val layoutParams = ConstraintLayout.LayoutParams(
//                            itemWidth,
//                            ConstraintLayout.LayoutParams.WRAP_CONTENT
//                        )
//                        layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
//                        holder.itemView.layoutParams = layoutParams
//                    } else if (position == nowShowingList.size - 1) {
//                        val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
//                        val layoutParams = ConstraintLayout.LayoutParams(
//                            itemWidth,
//                            ConstraintLayout.LayoutParams.WRAP_CONTENT
//                        )
//                        layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
//                        holder.itemView.layoutParams = layoutParams
//                    } else {
//                        val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
//                        val layoutParams = ConstraintLayout.LayoutParams(
//                            itemWidth,
//                            ConstraintLayout.LayoutParams.WRAP_CONTENT
//                        )
//                        layoutParams.rightMargin = Constant().convertDpToPixel(1f, context)
//                        layoutParams.leftMargin = Constant().convertDpToPixel(1f, context)
//                        holder.itemView.layoutParams = layoutParams
//                    }
//                } else {
////            int itemWidth = (int) ((screenWidth - 0)/(1.10f));
//                    val layoutParams = ConstraintLayout.LayoutParams(
//                        ConstraintLayout.LayoutParams.MATCH_PARENT,
//                        ConstraintLayout.LayoutParams.WRAP_CONTENT
//                    )
//                    layoutParams.leftMargin = Constant().convertDpToPixel(13F, context)
//                    layoutParams.rightMargin = Constant().convertDpToPixel(13F, context)
//                    holder.itemView.layoutParams = layoutParams
//                }

                //title
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.placeholder_horizental)
                    .into(binding.imageView141)

                if (this.redirect_url!=""){
                    binding.imageView142.show()
                } else {
                    binding.imageView142.hide()
                }

                itemView.setOnClickListener {
                    listener.categoryClick(this)
              }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun categoryClick(comingSoonItem: FormatResponse.Output.Ph)
    }

}