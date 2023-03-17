package com.net.pvr.ui.home.inCinemaMode

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.net.pvr.databinding.MovieDetailsItemBinding

class MoviewDetailsViewpagerAdapter(var context: Context,var list:MutableList<String>): RecyclerView.Adapter<MoviewDetailsViewpagerAdapter.MovieDetailsViewHolder>() {
    private var binding: MovieDetailsItemBinding?=null

    class MovieDetailsViewHolder(context:Context, binding:MovieDetailsItemBinding): ViewHolder(binding.root) {
        fun bind(get: String) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailsViewHolder {
        binding =
            MovieDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDetailsViewHolder(context = context,binding!!)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: MovieDetailsViewHolder, position: Int) {
        holder.bind(list.get(position))
    }
}