package com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.RetrievalItemBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class BookingRetrievalAdapter(
    private var context: Context,
    private var nowShowingList: ArrayList<BookingRetrievalResponse.Output.C>,
    private var listener: RecycleViewItemClickListener,
    private var textView5: TextView?
) :
    RecyclerView.Adapter<BookingRetrievalAdapter.ViewHolder>() {
    private var rowIndex=-1
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
                binding.textView249.text = this.d.replace("away","")
                //details
                binding.textView250.text = this.ad
                binding.textView248.isSelected=true
                if (this.childs.size > 1) {
                    binding.imageView168.hide()
                    binding.textView248.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
                    binding.textView248.setOnClickListener {
                        if (binding.recyclerView33.visibility==View.VISIBLE){
                            binding.recyclerView33.hide()
                            binding.textView248.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
                        }else{
                            binding.textView248.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0)
                            binding.recyclerView33.show()
                        }
                    }
                } else {
                    binding.imageView168.show()
                    binding.textView248.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                //Recycler
                val gridLayout3 = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
                val bookingShowsParentAdapter = BookingRetrievalChildAdapter(context, this.childs)
                binding.recyclerView33.layoutManager = gridLayout3
                binding.recyclerView33.adapter = bookingShowsParentAdapter

                if (rowIndex==position){
                    binding.recyclerView33.scrollToPosition(position + 1)

                    if (this.childs.size <= 1) {
                        binding.imageView168.background = context.resources.getDrawable(R.drawable.curve_select)
                        binding.constraintLayout139.setBackgroundResource(R.drawable.ui_item_select)
                    } else {
                        binding.constraintLayout139.setBackgroundResource(R.drawable.ui_item_unselect)
                        binding.textView248.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.arrow_down,
                            0
                        )
                    }
                }else{
                    binding.imageView168.setImageResource(R.drawable.curve_unselect)

                    binding.constraintLayout139.setBackgroundResource(R.drawable.ui_item_unselect)
                }

                itemView.setOnClickListener {
                    rowIndex=position
                    notifyDataSetChanged()
                }
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