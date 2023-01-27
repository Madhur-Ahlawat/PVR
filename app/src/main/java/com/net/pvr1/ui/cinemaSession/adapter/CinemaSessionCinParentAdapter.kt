package com.net.pvr1.ui.cinemaSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemCinemaDetailsMoviesBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show

class CinemaSessionCinParentAdapter(
    private var nowShowingList: List<CinemaSessionResponse.Child>,
    private var context: Context,
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

                binding.constraintLayout18.setOnClickListener {
                    rowIndex = position
                    if (check == 1){
                        binding.textView377.hide()
                        binding.imageView51.setImageResource(R.drawable.arrow_up)
                        binding.constraintLayout26.show()
                        check = 2
                        notifyDataSetChanged()
                    }else{
                        binding.textView377.show()
                        binding.imageView51.setImageResource(R.drawable.arrow_down)
                        binding.constraintLayout26.hide()
                        check = 1
                        notifyDataSetChanged()
                    }
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


}