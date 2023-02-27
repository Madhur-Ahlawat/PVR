package com.net.pvr.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.PrivilegeImageItemBinding
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.utils.Constant


@Suppress("DEPRECATION")
class PrivilegeTypeAdapter(
    private var nowShowingList: ArrayList<PrivilegeHomeResponse.Output.Pinfo>,
    private var context: Activity
) : RecyclerView.Adapter<PrivilegeTypeAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    private var rowIndex = 0


    inner class ViewHolder(val binding: PrivilegeImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        val binding = PrivilegeImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Language  .lng+this.lk
                Glide.with(context)
                    .load(this.pi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView15)

                //click

                val itemWidth = ((screenWidth - 8) / 1.20f).toInt()
                val layoutParams =
                    LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
                when (position) {
                    0 -> {
                        layoutParams.leftMargin = Constant().convertDpToPixel(30f, context)
                        layoutParams.rightMargin = Constant().convertDpToPixel(5f, context)
                    }
                    nowShowingList.size - 1 -> {
                        layoutParams.leftMargin = Constant().convertDpToPixel(5f, context)
                        layoutParams.rightMargin = Constant().convertDpToPixel(30f, context)
                    }
                    else -> {
                        layoutParams.leftMargin = Constant().convertDpToPixel(12f, context)
                        layoutParams.rightMargin = Constant().convertDpToPixel(12f, context)
                    }
                }
                holder.itemView.layoutParams = layoutParams


            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is LinearLayoutManager && itemCount > 0) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePosition = manager.findFirstCompletelyVisibleItemPosition()
                    //                    System.out.println("review_position1234--->"+visiblePosition);
                    if (visiblePosition > -1) {
//                        View v = llm.findViewByPosition(visiblePosition);
//                        //do something
//                        v.setBackgroundColor(Color.parseColor("#777777"));
                    }
                }
            })
        }
    }

//    interface RecycleViewItemClickListener {
//        fun privilegeTypeScroll(
//            nowShowingList: ArrayList<PrivilegeHomeResponse.Output.Pinfo>,
//            position: Int
//        )
//    }

}