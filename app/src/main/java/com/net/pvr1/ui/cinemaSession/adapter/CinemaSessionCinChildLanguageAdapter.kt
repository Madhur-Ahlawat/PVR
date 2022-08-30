package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.ItemCinemaSessionLangChildBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse


class CinemaSessionCinChildLanguageAdapter(
    private var nowShowingList: List<CinemaSessionResponse.Child.Mv.Ml>,
    private var context: Context
//    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<CinemaSessionCinChildLanguageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaSessionLangChildBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaSessionLangChildBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView95.text = this.lng
                //RecyclerView
                val cinemaSessionLanguageAdapter = CinemaSessionTimeAdapter(this.s, context, )
                binding.recyclerView6.layoutManager = GridLayoutManager(context, 3)
                binding.recyclerView6.adapter = cinemaSessionLanguageAdapter

                //click
//                holder.itemView.setOnClickListener {listener.dateClick(this)  }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun dateClick(comingSoonItem: CinemaSessionResponse.Output.Bd)
    }

}