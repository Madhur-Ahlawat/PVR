package com.net.pvr1.ui.home.fragment.home.adapter

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
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class HomeTrailerAdapter(
    private var context: Context,
    private var nowShowingList: List<HomeResponse.Cp>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<HomeTrailerAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_trailer, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context)
            .load(comingSoonItem.i)
            .error(R.drawable.app_icon)
            .into(holder.image)

        holder.title.text=comingSoonItem.n
        holder.descraption.text=comingSoonItem.rt
        if (comingSoonItem.mtrailerurl.isEmpty()){
            holder.trailer.hide()
        }else{
            holder.trailer.show()
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

    }

    interface RecycleViewItemClickListener {
        fun onTrailerClick(comingSoonItem: HomeResponse.Cp)
    }

}