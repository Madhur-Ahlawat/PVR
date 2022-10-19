package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.databinding.RetrievalItemBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class BookingRetrievalAdapter(
    private var context: Context,
    private var nowShowingList: ArrayList<BookingRetrievalResponse.Output.C>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<BookingRetrievalAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RetrievalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RetrievalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //name
                binding.textView248.text = this.n
                //distance
                binding.textView249.text = ""
                //details
                binding.textView250.text = this.ad

                binding.imageView107.setOnClickListener {
                    binding.recyclerView33.show()
                }
                if (this.childs.size >0) {
                    binding.imageView107.hide()
                } else {
                    binding.imageView107.show()

                }
                //Recycler
                val gridLayout3 = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                val bookingShowsParentAdapter = BookingRetrievalChildAdapter(this, this.childs)
                binding.recyclerView33.layoutManager = gridLayout3
                binding.recyclerView33.adapter = bookingShowsParentAdapter
                //click
//                holder.itemView.setOnClickListener {listener.languageClick(this)  }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun dateClick(comingSoonItem: BookingResponse.Output.Dy)
    }

}