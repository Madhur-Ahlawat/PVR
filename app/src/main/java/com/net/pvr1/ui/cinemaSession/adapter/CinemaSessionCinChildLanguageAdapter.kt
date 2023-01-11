package com.net.pvr1.ui.cinemaSession.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.net.pvr1.databinding.ItemCinemaSessionLangChildBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse


@Suppress("NAME_SHADOWING")
class CinemaSessionCinChildLanguageAdapter(
    private var nowShowingList: ArrayList<CinemaSessionResponse.Child.Mv.Ml>,
    private var context: Context,
    private var cinemaId: String?,
    private var showType:Int?
) : RecyclerView.Adapter<CinemaSessionCinChildLanguageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaSessionLangChildBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaSessionLangChildBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView95.text = this.lng
                //RecyclerView
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.FLEX_START
                val cinemaSessionLanguageAdapter = CinemaSessionTimeAdapter(this.s, context,cinemaId)
                binding.recyclerView6.layoutManager = layoutManager
                binding.recyclerView6.adapter = cinemaSessionLanguageAdapter

            }
        }
    }

    override fun getItemCount(): Int {
        return if (showType==0){
            1
        }else{
            nowShowingList.size
        }
    }

}