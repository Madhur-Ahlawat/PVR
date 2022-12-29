package com.net.pvr1.ui.home.fragment.home.adapter

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
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.printLog
import com.net.pvr1.utils.show

@Suppress("DEPRECATION")
class HomeTrailerAdapter(
    private var context: Activity,
    private var nowShowingList: List<HomeResponse.Cp>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<HomeTrailerAdapter.MyViewHolderNowShowing>() {
    private var displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_trailer, parent, false)
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context)
            .load(comingSoonItem.i)
            .error(R.drawable.placeholder_horizontal_movie)
            .into(holder.image)

        holder.title.text=comingSoonItem.n
        holder.descraption.text=comingSoonItem.rt
        if (comingSoonItem.mtrailerurl.isEmpty()){
            holder.trailer.hide()
        }else{
            holder.trailer.show()
        }

        if (nowShowingList.size > 1) {
            if (nowShowingList.size == 2) {
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
                holder.mainView.layoutParams = layoutParams
            } else if (position == 0) {
                printLog("abcd---"+position)

                val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                holder.mainView.layoutParams = layoutParams
            } else if (position == nowShowingList.size - 1) {
                printLog("abcd1---"+position)

                val itemWidth = ((screenWidth - 0) / 1.10f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                holder.mainView.layoutParams = layoutParams
            } else {
                printLog("abcd2---"+position)

                val itemWidth = ((screenWidth - 0) / 1.1f).toInt()
                val layoutParams = ConstraintLayout.LayoutParams(
                    itemWidth,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.rightMargin = Constant().convertDpToPixel(13f, context)
                layoutParams.leftMargin = Constant().convertDpToPixel(13f, context)
                holder.mainView.layoutParams = layoutParams
            }
        } else {
//            int itemWidth = (int) ((screenWidth - 0)/(1.10f));
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.leftMargin = Constant().convertDpToPixel(13F, context)
            layoutParams.rightMargin = Constant().convertDpToPixel(13F, context)
            holder.mainView.layoutParams = layoutParams
        }

        holder.trailer.setOnClickListener {
            listener.onTrailerClick(comingSoonItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView11)
        var title: TextView = view.findViewById(R.id.textView37)
        var descraption: TextView = view.findViewById(R.id.textView38)
        var trailer: ImageView = view.findViewById(R.id.imageView12)
        var mainView: ConstraintLayout = view.findViewById(R.id.mainView)

    }

    interface RecycleViewItemClickListener {
        fun onTrailerClick(comingSoonItem: HomeResponse.Cp)
    }

}