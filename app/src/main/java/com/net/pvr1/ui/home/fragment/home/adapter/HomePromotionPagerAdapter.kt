package com.net.pvr1.ui.home.fragment.home.adapter

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.ui.home.fragment.home.response.HomeResponse
import kotlin.math.abs


class HomePromotionPagerAdapter(
    private val context: Activity,
    private val movies: List<HomeResponse.Mfi>,
    private val viewpager: ViewPager2?
) : RecyclerView.Adapter<HomePromotionPagerAdapter.MovieViewHolder>() {

    private var rowIndex = 0
    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_prmotion, parent, false)
        return MovieViewHolder(view)
    }


    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var promotionImage = view.findViewById<ImageView>(R.id.imageView15)!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val obj = movies[position]
        val transfer = CompositePageTransformer()
        transfer.addTransformer(MarginPageTransformer(30))
        transfer.addTransformer(object : com.github.islamkhsh.viewpager2.ViewPager2.PageTransformer,
            ViewPager2.PageTransformer {
            override fun transformPage(page: View, position: Float) {
                rowIndex = viewpager?.currentItem!!
                val r = 1 - abs(position)
                //page.scaleY = (0.85f+ r*0.14f)
                try {
                    notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
        viewpager?.setPageTransformer(transfer)

        Glide.with(context)
            .load(R.drawable.dummy_prmotion)
            .error(R.drawable.dummy_prmotion)
            .into(holder.promotionImage)


    }
}