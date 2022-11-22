package com.net.pvr1.ui.formats.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.formats.response.FormatResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class FormatMoviesAdapter(
    private var context: Context,
    private var nowShowingList: List<FormatResponse.Output.Cinemas.M>,
    private var listener: RecycleViewItemClickListener,

    ) :
    RecyclerView.Adapter<FormatMoviesAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_cinema_list, parent, false)
        return MyViewHolderNowShowing(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val comingSoonItem = nowShowingList[position]

        //Image
        Glide.with(context)
            .load(comingSoonItem.miv)
            .error(R.drawable.app_icon)
            .into(holder.image)

        holder.title.text=comingSoonItem.n
        holder.rating.text=comingSoonItem.ce
        holder.type.text=comingSoonItem.lng+","+comingSoonItem.tag
        if (comingSoonItem.lng.isEmpty()){
            holder.dots.hide()
        }else{
            holder.dots.show()
        }
        holder.language.text=comingSoonItem.sh
        //Movie Click
        holder.image.setOnClickListener {
            listener.onMoviesClick(comingSoonItem)
        }
        //Book Click
        holder.book.setOnClickListener {
            listener.onBookClick(comingSoonItem)
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView16)
        var dots: ImageView = view.findViewById(R.id.imageView17)
        var title: TextView = view.findViewById(R.id.textView42)
        var rating: TextView = view.findViewById(R.id.textView43)
        var type: TextView = view.findViewById(R.id.textView44)
        var language: TextView = view.findViewById(R.id.textView45)
        var book: TextView = view.findViewById(R.id.textView46)

    }

    interface RecycleViewItemClickListener {
        fun onMoviesClick(comingSoonItem: FormatResponse.Output.Cinemas.M)
        fun onBookClick(comingSoonItem: FormatResponse.Output.Cinemas.M)
    }

}