package com.net.pvr1.ui.home.fragment.home.adapter

import android.content.Context
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
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class HomeCinemaCategoryAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Mfi>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<HomeCinemaCategoryAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_image, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        if (comingSoonItem.url.isEmpty()) {
            holder.image.hide()
            holder.btText.show()
            holder.textView.text = comingSoonItem.name
        } else {
            holder.image.show()
            holder.btText.hide()
            //Image
            Glide.with(context)
                .load(comingSoonItem.url)
                .error(R.drawable.app_icon)
                .into(holder.image)
        }

        holder.image.setOnClickListener {
            listener.onCategoryClick(comingSoonItem)
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView10)
        var btText: ConstraintLayout = view.findViewById(R.id.btText)
        var textView: TextView = view.findViewById(R.id.textView)

    }

    interface RecycleViewItemClickListener {
        fun onCategoryClick(comingSoonItem: HomeResponse.Mfi)
    }

}