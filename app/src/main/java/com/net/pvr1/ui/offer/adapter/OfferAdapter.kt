package com.net.pvr1.ui.offer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.databinding.OfferItemBinding
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.utils.Util


class OfferAdapter(
    private var nowShowingList: List<OfferResponse.Output>,
    private var context: Context,
    private var listener: Direction,
) :
    RecyclerView.Adapter<OfferAdapter.ViewHolder>() {
    private var screenWidth = 0
    private val type: String? = null

    inner class ViewHolder(val binding: OfferItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OfferItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        screenWidth = context.resources.displayMetrics.widthPixels

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (!type.equals("N", ignoreCase = true)) {
                    if (nowShowingList.size == 1) {
                        val itemWidth = ((screenWidth - 8) * 0.88f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        holder.itemView.layoutParams = layoutParams
                    } else {
                        val itemWidth = (screenWidth * 0.80f).toInt()
                        val layoutParams = ConstraintLayout.LayoutParams(
                            itemWidth,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                        holder.itemView.layoutParams = layoutParams
                    }
                } else {
                    val layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.leftMargin = Util.convertDpToPixel(12F, context)
                    layoutParams.rightMargin = Util.convertDpToPixel(12F, context)
                    layoutParams.bottomMargin = Util.convertDpToPixel(1F, context)
                    layoutParams.topMargin = Util.convertDpToPixel(6F, context)
                    holder.itemView.layoutParams = layoutParams
                }

                binding.name.text = this.c
                binding.expiry.text = "Valid till " + this.vt
                Glide.with(context)
                    .load(this.image)
                    .into(binding.offerImg)

            }

        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface Direction {
        fun offerClick(comingSoonItem: OfferResponse.Output)
    }

}