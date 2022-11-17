package com.net.pvr1.ui.setAlert.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.SetAlertItemBinding
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse


class AlertTheaterAdapter(
    private var nowShowingList: ArrayList<BookingRetrievalResponse.Output.C>,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<AlertTheaterAdapter.ViewHolder>() {
    private var itemCount = 0
    private var theatreList: List<BookingRetrievalResponse.Output.C>? = null

    inner class ViewHolder(val binding: SetAlertItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SetAlertItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                theatreList = theatreList
                //name
                binding.textView248.text = this.n
                //distance
                binding.textView249.text = this.d
                //details
                binding.textView250.text = this.ad

                if (position == itemCount) {
                    binding.imageView134.setImageResource(R.drawable.curve_select)
                    binding.constraintLayout92.setBackgroundResource(
                        R.drawable.alert_curve_ui_select
                    )
                } else {
                    binding.imageView134.setImageResource(R.drawable.curve_unselect)
                    binding.constraintLayout92.setBackgroundResource(
                        R.drawable.alert_curve_ui
                    )
                }

                //click
                holder.itemView.setOnClickListener {
                    itemCount = position
                    holder.itemView.setOnClickListener {
                        listener.dateClick(this)
                        notifyDataSetChanged()
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListener {
        fun dateClick(comingSoonItem: BookingRetrievalResponse.Output.C)
    }

}