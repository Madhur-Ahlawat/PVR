package com.net.pvr1.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemBookingSessionCinemaLocationBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.printLog
import com.net.pvr1.utils.show


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
                if (position==0){
                    binding.recyclerView7.show()
                    binding.hcIcon.hide()
                    binding.imageView56.setImageResource(R.drawable.arrow_up)
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
                        binding.imageView56.setImageResource(R.drawable.arrow_up)
                    } else {
                        binding.imageView56.setImageResource(R.drawable.arrow_down)
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
                binding.imageView70.setOnClickListener { listener.alertClick(this) }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    //and assigning it to the list with notifydatasetchanged method
    fun filterList(filterdNames: ArrayList<BookingResponse.Output.Cinema>) {
        this.nowShowingList = filterdNames
        notifyDataSetChanged()
    }


    interface RecycleViewItemClickListener {
        fun alertClick(comingSoonItem: BookingResponse.Output.Cinema)
    }

}