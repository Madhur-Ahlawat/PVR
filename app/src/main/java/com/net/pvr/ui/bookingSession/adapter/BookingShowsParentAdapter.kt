package com.net.pvr.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.ItemBookingSessionCinemaLocationBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.printLog
import com.net.pvr.utils.show
import com.net.pvr.utils.view.RightDrawableOnTouchListener


class BookingShowsParentAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Cinema>,
    private var context: Context,
    private var listener: RecycleViewItemClickListener,
    private var adlt: Boolean
) : RecyclerView.Adapter<BookingShowsParentAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingSessionCinemaLocationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingSessionCinemaLocationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        println("BookingResponse--->${nowShowingList[position].cn}")

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
                if (this.newCinemaText!= null && this.newCinemaText!= ""){
                    binding.cinemaLocation.show()
                    binding.cinemaLocation.text = this.newCinemaText
                } else {
                    binding.cinemaLocation.hide()
                }
                if (position==0){
                    binding.recyclerView7.show()
                    binding.hcIcon.hide()
                    binding.imageView56.rotation = 180f
                    val cellSize = this.childs.size
                    if (cellSize > 1) {
                        val gridLayout3 =
                            GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                        val bookingShowsParentAdapter =
                            BookingCinemaNameAdapter(this.childs, context, adlt, cid)
                        binding.recyclerView7.layoutManager = gridLayout3
                        binding.recyclerView7.adapter = bookingShowsParentAdapter
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

                    }
                }
                binding.constraintLayout121.setOnClickListener {
                    printLog("size--->${this.childs[0].sws.size}")
                    if (binding.recyclerView7.visibility == View.GONE) {
                        binding.recyclerView7.show()
                        binding.hcIcon.hide()
                        binding.imageView56.rotation = 180f
                    } else {
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

                    }
                }

                //click
//                binding.textView105.setOnClickListener { listener.alertClick(this) }


                binding.textView105.setOnTouchListener(object : RightDrawableOnTouchListener(binding.textView105) {
                    @SuppressLint("ClickableViewAccessibility")
                    override fun onDrawableTouch(event: MotionEvent?): Boolean {
                        listener.alertClick(nowShowingList[position])
                        return true
                    }
                })
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
    public fun clearList(){
        nowShowingList.clear()
        notifyDataSetChanged()
    }


    interface RecycleViewItemClickListener {
        fun alertClick(comingSoonItem: BookingResponse.Output.Cinema)
    }

}