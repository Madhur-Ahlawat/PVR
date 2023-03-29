package com.net.pvr.ui.home.fragment.home.adapter

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.net.pvr.utils.toast


class HomeCinemaCategoryAdapter(
    private var context: Activity,
    private var nowShowingList: List<HomeResponse.Mfi>,
    private var listener: RecycleViewItemClickListener,
    private var recyclerView: RecyclerView) :
    RecyclerView.Adapter<HomeCinemaCategoryAdapter.MyViewHolderNowShowing>() {
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_image, parent, false)
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
//        recyclerView.layoutParams.width = screenWidth
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        if (nowShowingList.size == 1){
            holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            holder.mainView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            holder.imageCatNew.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            holder.cardTag.hide()
            holder.imageCatNew.show()
            Glide.with(context)
                .load(comingSoonItem.nurl)
                .placeholder(R.drawable.multipleformats)
                .error(R.drawable.multipleformats)
                .into(holder.imageCatNew)
        }else if (nowShowingList.size<5){
            holder.cardTag.layoutParams.width = (screenWidth/nowShowingList.size)-50
            holder.itemView.layoutParams.width = (screenWidth/nowShowingList.size)-50
            holder.cardTag.show()
            holder.imageCatNew.hide()
            Glide.with(context)
                .load(comingSoonItem.nurl)
                .placeholder(R.drawable.multipleformats)
                .error(R.drawable.multipleformats)
                .into(holder.imageCat)
        }else{
            holder.mainView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            holder.cardTag.show()
            holder.imageCatNew.hide()
            Glide.with(context)
                .load(comingSoonItem.nurl)
                .placeholder(R.drawable.multipleformats)
                .error(R.drawable.multipleformats)
                .into(holder.imageCat)
        }

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(comingSoonItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var mainView: LinearLayout = view.findViewById(R.id.mainView)
        var cardTag: CardView = view.findViewById(R.id.cardTag)
        var imageCat: ImageView = view.findViewById(R.id.ivTag)
        var imageCatNew: ImageView = view.findViewById(R.id.ivTagNew)

    }

    interface RecycleViewItemClickListener {
        fun onCategoryClick(comingSoonItem: HomeResponse.Mfi)
    }

}