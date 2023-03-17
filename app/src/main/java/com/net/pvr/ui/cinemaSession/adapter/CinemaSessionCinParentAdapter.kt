package com.net.pvr.ui.cinemaSession.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.ItemCinemaDetailsMoviesBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show

class CinemaSessionCinParentAdapter(
    private var nowShowingList: List<CinemaSessionResponse.Child>,
    private var context: Activity,
    private var cinemaId: String) : RecyclerView.Adapter<CinemaSessionCinParentAdapter.ViewHolder>() {
    private var check = 1
    private var rowIndex = 0

    inner class ViewHolder(val binding: ItemCinemaDetailsMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Cinema
                binding.textView102.text = this.ccn
                cinemaId= this.ccid
                //RecyclerView
                val gridLayout = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                val cinemaSessionCinemasChildAdapter = CinemaSessionChildAdapter(this.mvs, context,cinemaId,this.ccn,this.at)
                binding.recyclerView16.layoutManager = gridLayout
                binding.recyclerView16.adapter = cinemaSessionCinemasChildAdapter

                if (nowShowingList.size == 1){
                    binding.constraintLayout18.hide()
                    binding.imageView51.hide()
                    binding.constraintLayout26.show()
                }

                if (rowIndex == position){
                    binding.textView377.hide()
                    binding.imageView51.rotation = 180F
                    binding.constraintLayout26.show()
                }else{
                    binding.textView377.show()
                    binding.imageView51.rotation = 360F
                    binding.constraintLayout26.hide()
                }

                binding.constraintLayout18.setOnClickListener {
                    rowIndex = position
                    notifyDataSetChanged()
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterdNames: List<CinemaSessionResponse.Child>) {
        this.nowShowingList = filterdNames
        notifyDataSetChanged()
    }


}