package com.net.pvr.ui.cinemaSession.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.*
import com.net.pvr.databinding.ItemCinemaSessionMovieDetailsBinding
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.net.pvr.R


@Suppress("NAME_SHADOWING")
class CinemaSessionChildAdapter(
    private var nowShowingList: ArrayList<CinemaSessionResponse.Child.Mv>,
    private var context: Context,
    private var cinemaId: String?,
    private var ccn: String,
    private var at: String
) : RecyclerView.Adapter<CinemaSessionChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaSessionMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaSessionMovieDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                //title
                binding.textView91.text = this.mn

                //time
                binding.textView92.text = this.mlength

                //genre
                binding.textView93.text = this.genres.joinToString { it }

                //Language
                binding.textView94.hide()

                //Image
                Glide.with(context)
                    .load(this.miv)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView47)

                //RecyclerView
                val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                val cinemaSessionLanguageAdapter =
                    CinemaSessionCinChildLanguageAdapter(this.ml, context, cinemaId,0,ccn,this.adlt,at,this.mih)
                binding.recyclerView17.layoutManager = layoutManager
                binding.recyclerView17.adapter = cinemaSessionLanguageAdapter

                if(this.ml.size>0){
                    binding.imageView111.hide()
                    binding.constraintLayout87.hide()

                }else{
                    binding.imageView111.show()
                    binding.constraintLayout87.show()
                }

                binding.textView387.setOnClickListener {
                    if (binding.textView387.text==context.getString(R.string.view_more)){
                        binding.textView387.text=context.getString(R.string.view_less)
                        binding.imageView111.hide()

                        val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                        val cinemaSessionLanguageAdapter =
                            CinemaSessionCinChildLanguageAdapter(this.ml, context, cinemaId,0,ccn,this.adlt,at,this.mih)
                        binding.recyclerView17.layoutManager = layoutManager
                        binding.recyclerView17.adapter = cinemaSessionLanguageAdapter

                    }else{
                        binding.imageView111.show()
                        binding.textView387.text=context.getString(R.string.view_more)

                        val layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                        val cinemaSessionLanguageAdapter =
                            CinemaSessionCinChildLanguageAdapter(this.ml, context, cinemaId,1,ccn,this.adlt,at,this.mih)
                        binding.recyclerView17.layoutManager = layoutManager
                        binding.recyclerView17.adapter = cinemaSessionLanguageAdapter
                    }
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }
}