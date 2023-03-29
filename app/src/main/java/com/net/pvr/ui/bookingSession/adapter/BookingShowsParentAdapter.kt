package com.net.pvr.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.net.pvr.R
import com.net.pvr.databinding.ItemBookingSessionCinemaLocationBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show


class BookingShowsParentAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Cinema>,
    private var context: Activity,
    private var listener: RecycleViewItemClickListener,
    private var adlt: Boolean
) : RecyclerView.Adapter<BookingShowsParentAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingSessionCinemaLocationBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var rowIndex=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaLocationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //Language
                binding.textView105.text = this.cn

                //Recycler
                if(this.hc){
                    binding.hcIcon.show()
                }else{
                    binding.hcIcon.hide()
                }

                if (this.acct){
//                    binding.hcIcon.setImageResource(R.drawable.accst_icon)
                }else{
                    binding.hcIcon.setImageResource(R.drawable.hc_icon)
                }

                if (this.newCinemaText!= null && this.newCinemaText!= ""){
                    binding.cinemaLocation.show()
                    binding.cinemaLocation.text = this.newCinemaText
                } else {
                    binding.cinemaLocation.hide()
                }

//                if (position==0){
//                    binding.recyclerView7.show()
//                    binding.hcIcon.hide()
//                    binding.imageView56.rotation = 180f
//                    val cellSize = this.childs.size
//                    if (cellSize > 1) {
//                        val gridLayout3 =
//                            GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
//                        val bookingShowsParentAdapter =
//                            BookingCinemaNameAdapter(this.childs, context, adlt, cid)
//                        binding.recyclerView7.layoutManager = gridLayout3
//                        binding.recyclerView7.adapter = bookingShowsParentAdapter
//                    } else {
//                        val gridLayout3 =
//                            GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
//                        val bookingShowsParentAdapter = BookingCinemaLangAdapter(
//                            this.childs[0].sws,
//                            context,
//                            this.childs[0].ccid,
//                            this.childs[0].ccn,
//                            this.childs[0].at,
//                            adlt
//                        )
//                        binding.recyclerView7.layoutManager = gridLayout3
//                        binding.recyclerView7.adapter = bookingShowsParentAdapter
//
//                    }
//                }

                val smoothScroller: SmoothScroller =
                    object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }
                    }

                if (rowIndex==position){
                    binding.recyclerView7.show()
                    binding.hcIcon.hide()
                    binding.imageView56.rotation = 180f
                }else{
                    binding.imageView56.rotation = 360f
                    binding.recyclerView7.hide()
                    binding.hcIcon.show()
                }

                val cellSize = this.childs.size

                if (cellSize > 1) {
                    val gridLayout3 =
                        GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                    val bookingShowsParentAdapter =
                        BookingCinemaNameAdapter(this.childs, context,adlt,this.cid)
                    binding.recyclerView7.layoutManager = gridLayout3
                    binding.recyclerView7.adapter = bookingShowsParentAdapter
                    smoothScroller.targetPosition = position
                    binding.recyclerView7.layoutManager?.startSmoothScroll(smoothScroller)

                } else {
                    val gridLayout3 =
                        GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                    val bookingShowsParentAdapter = BookingCinemaLangAdapter(
                        this.childs[0].sws,
                        context,
                        this.childs[0].ccid,
                        this.childs[0].ccn,
                        this.childs[0].at,
                        adlt
                    )
                    binding.recyclerView7.layoutManager = gridLayout3
                    binding.recyclerView7.adapter = bookingShowsParentAdapter
                    smoothScroller.targetPosition = position
                    binding.recyclerView7.layoutManager?.startSmoothScroll(smoothScroller)
                }

                binding.constraintLayout121.setOnClickListener {
                    rowIndex= position
                    notifyDataSetChanged()
//                    if (binding.recyclerView7.visibility == View.GONE) {
//                        binding.recyclerView7.show()
//                        binding.hcIcon.hide()
//                        binding.imageView56.rotation = 180f
//                    } else {
//                        binding.imageView56.rotation = 360f
//                        binding.recyclerView7.hide()
//                        binding.hcIcon.show()
//                    }

                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    //and assigning it to the list with notifydatasetchanged method
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterdNames: ArrayList<BookingResponse.Output.Cinema>) {
        this.nowShowingList = filterdNames
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearList(){
        nowShowingList.clear()
        notifyDataSetChanged()
    }


    interface RecycleViewItemClickListener {
        fun alertClick(comingSoonItem: BookingResponse.Output.Cinema)
    }

}