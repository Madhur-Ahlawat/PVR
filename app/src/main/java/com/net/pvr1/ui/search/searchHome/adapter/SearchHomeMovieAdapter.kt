package com.net.pvr1.ui.search.searchHome.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse

class SearchHomeMovieAdapter(
    private var selectCityList: List<HomeSearchResponse.Output.M>,
    private var context: Context,
    private var listner: RecycleViewItemClickListenerCity
) :
    RecyclerView.Adapter<SearchHomeMovieAdapter.MyViewHolderNowShowing>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNowShowing {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_search_movie, parent, false)
        return MyViewHolderNowShowing(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolderNowShowing, position: Int) {
        val selectCityItemList = selectCityList[position]
//        title
        holder.title.text = selectCityItemList.n

//        CAt
         val category = selectCityItemList.c.replace("[", "").replace("]", "").replace("(", "").replace(")", "")
        holder.timeCategory.text =
            category +" "+
                context.getString(R.string.dots)+" "+
                selectCityItemList.length+" "+
                context.getString(R.string.dots)+" "+
                selectCityItemList.genre

//lng
        holder.language.text = selectCityItemList.l

        //Image
        Glide.with(context)
            .load(selectCityItemList.im)
            .error(R.drawable.placeholder_vertical)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            listner.onSearchMovie(selectCityItemList)
        }

    }

    override fun getItemCount(): Int {
        return if (selectCityList.isNotEmpty()) selectCityList.size else 0
    }

    class MyViewHolderNowShowing(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.imageView25)
        var title: TextView = view.findViewById(R.id.textView47)
        var timeCategory: TextView = view.findViewById(R.id.textView48)
        var language: TextView = view.findViewById(R.id.textView49)
    }

    interface RecycleViewItemClickListenerCity {
        fun onSearchMovie(selectCityList: HomeSearchResponse.Output.M)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterMovieList(filterList: ArrayList<HomeSearchResponse.Output.M>) {
        // below line is to add our filtered
        selectCityList = filterList
        notifyDataSetChanged()
    }

}
