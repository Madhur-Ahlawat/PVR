package com.net.pvr1.ui.offer.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.offer.response.MOfferResponse
import com.net.pvr1.ui.offer.response.OfferResponse
import com.net.pvr1.utils.Util

//context: Activity, offerList: List<Datum>?, type: String
class OffersFAdapter(
    private var context: Activity,
    private var offerList: ArrayList<MOfferResponse.Output.Offer>,
    private var type: String
) :
    RecyclerView.Adapter<OffersFAdapter.ViewHolder>() {
    //    private val offerList: List<Datum>?
//    private val context: Context
//    private val type: String
    private var screenWidth = 0
    var displayMetrics = DisplayMetrics()

    init {
//        this.offerList = offerList
//        this.context = context
//        this.type = type
//        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        screenWidth = context.resources.displayMetrics.widthPixels
//        return LayoutInflater.from(parent.context)
//            .inflate(R.layout.offer_item, null)

        val itemLayoutView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, null)
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: OffersFAdapter.ViewHolder, position: Int) {
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

//        if (!type.equalsIgnoreCase("N")){
//            if (offerList.size() > 1) {
//                if (offerList.size() == 2) {
//                    int itemWidth = (int) ((screenWidth - 0) / (1.33f));
//                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                    if (position == 0) {
//                        layoutParams.leftMargin = (int) Util.convertDpToPixel(14, context);
//                        layoutParams.rightMargin = (int) Util.convertDpToPixel(7, context);
//                    } else {
//                        layoutParams.leftMargin = (int) Util.convertDpToPixel(7, context);
//                        layoutParams.rightMargin = (int) Util.convertDpToPixel(14, context);
//                    }
//                    holder.itemView.setLayoutParams(layoutParams);
//                } else if (position == 0) {
//                    int itemWidth = (int) ((screenWidth - 8) * (0.8f));
//                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.leftMargin = (int) Util.convertDpToPixel(0, context);
////                    layoutParams.rightMargin = (int) Util.convertDpToPixel(7, context);
//                    holder.itemView.setLayoutParams(layoutParams);
//                } else if (position == offerList.size() - 1) {
//                    int itemWidth = (int) ((screenWidth - 8) * (0.95f));
//                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.rightMargin = (int) Util.convertDpToPixel(7, context);
//                    layoutParams.leftMargin = (int) Util.convertDpToPixel(7, context);
//                    holder.itemView.setLayoutParams(layoutParams);
//                } else {
//                    int itemWidth = (int) ((screenWidth - 8) * (0.85f));
//                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.rightMargin = (int) Util.convertDpToPixel(7, context);
//                    layoutParams.leftMargin = (int) Util.convertDpToPixel(7, context);
//                    holder.itemView.setLayoutParams(layoutParams);
//                }
//
//            } else {
//                int itemWidth = (int) ((screenWidth - 0) / (1.15f));
//                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                layoutParams.leftMargin = (int) Util.convertDpToPixel(7, context);
//                holder.itemView.setLayoutParams(layoutParams);
//            }
//    }else {
//            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.leftMargin = (int) Util.convertDpToPixel(25, context);
//            layoutParams.rightMargin = (int) Util.convertDpToPixel(25, context);
//            layoutParams.bottomMargin = (int) Util.convertDpToPixel(1, context);
//            layoutParams.topMargin = (int) Util.convertDpToPixel(6, context);
//            holder.itemView.setLayoutParams(layoutParams);
//        }

        Glide.with(context)
            .load(obj.i)
            .into(holder.offerImg)

//        ImageLoader.getInstance()
//            .displayImage(obj.getI1(), holder.offerImg, PCApplication.landingRectangleImageOption)
        holder.itemView.setOnClickListener(View.OnClickListener {
//            GoogleAnalyitics.setGAEventName(context as Activity, "Offers", "Offers", obj.getT())
//            val bundle = Bundle()
//            FirebaseEvent.hitEvent(
//                context,
//                FirebaseEvent.OFFER_EVENT + "_" + obj.getT().replaceAll(" ", "_"),
//                bundle
//            )
//            val intent = Intent(context, PcOfferDetailActivity::class.java)
//            intent.putExtra(PCConstants.IntentKey.OFFER_ID, Integer.toString(obj.getId()))
//            intent.putExtra(PCConstants.IntentKey.TITLE_HEADER, obj.getT())
//            context.startActivity(intent)
        })
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

    companion object
}