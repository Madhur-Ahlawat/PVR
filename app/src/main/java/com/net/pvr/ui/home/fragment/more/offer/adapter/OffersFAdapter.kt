package com.net.pvr.ui.home.fragment.more.offer.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.more.offer.offerDetails.OfferDetailsActivity
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse
import com.net.pvr.utils.Util

class OffersFAdapter(
    private var context: Activity,
    private var offerList: ArrayList<MOfferResponse.Output.Offer>,
    private var type: String
) :
    RecyclerView.Adapter<OffersFAdapter.ViewHolder>() {
    private var screenWidth = 0
    var displayMetrics = DisplayMetrics()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        screenWidth = context.resources.displayMetrics.widthPixels

        val itemLayoutView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, null)
        return ViewHolder(itemLayoutView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        screenWidth = context.resources.displayMetrics.widthPixels
        val obj: MOfferResponse.Output.Offer = offerList[position]
        holder.name.text = obj.c
        holder.name.isSelected = true
        holder.expiry.text = "Valid till " + obj.vt
        if (!type.equals("N", ignoreCase = true)) {
            if (offerList.size == 1) {
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

        Glide.with(context)
            .load(obj.i)
            .into(holder.offerImg)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, OfferDetailsActivity::class.java)
            intent.putExtra("title", obj.c)
            intent.putExtra("disc", obj.vt)
            intent.putExtra("id", obj.id.toString())
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var offerImg: ImageView
        var name: TextView
        var expiry: TextView

        init {
            name = itemView.findViewById(R.id.name)
            expiry = itemView.findViewById(R.id.expiry)
            offerImg = itemView.findViewById(R.id.offerImg)
        }
    }

    override fun getItemCount(): Int {
        return offerList.size
    }
    }