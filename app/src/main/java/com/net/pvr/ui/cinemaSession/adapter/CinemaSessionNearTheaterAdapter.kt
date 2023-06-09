package com.net.pvr.ui.cinemaSession.adapter

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.ItemCinemaDetailsCinemasBinding
import com.net.pvr.ui.cinemaSession.response.CinemaNearTheaterResponse


@Suppress("DEPRECATION")
class CinemaSessionNearTheaterAdapter(
    private var nowShowingList: List<CinemaNearTheaterResponse.Output.C>,
    private var context: Activity,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CinemaSessionNearTheaterAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0
    inner class ViewHolder(val binding: ItemCinemaDetailsCinemasBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaDetailsCinemasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(nowShowingList[position]){
                val lp = holder.itemView.layoutParams



                if (position == nowShowingList.size-1){
                    lp.height = lp.height
                    lp.width = ((screenWidth-40)/1.17f).toInt()
                    holder.itemView.setPadding(0,0,20,0)
                    holder.itemView.layoutParams = lp
                }else{
                    lp.height = lp.height
                    lp.width = ((screenWidth-40)/1.17f).toInt()
                    holder.itemView.layoutParams = lp
                }

                //title
                binding.textView100.text=this.n
                //address
                binding.textView101.text=this.ad
                //Distance
                binding.textView86.text= this.d
                //Shows
                binding.textView85.text = "${this.sc} Shows"
                //Image
                Glide.with(context)
                    .load(this.imob)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView52)
                //Direction
                binding.view64.setOnClickListener {
                    listener.nearTheaterDirectionClick(this)
                }
                //Shows
                binding.cardView.setOnClickListener {
                    listener.showsClick(this)
                }


            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun showsClick(comingSoonItem: CinemaNearTheaterResponse.Output.C)
        fun nearTheaterDirectionClick(comingSoonItem: CinemaNearTheaterResponse.Output.C)
    }

}