package com.net.pvr1.ui.seatLayout.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.SeatShowTimeItemBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.invisible
import com.net.pvr1.utils.show


class ShowsAdapter(
    private var nowShowingList: ArrayList<BookingResponse.Output.Cinema.Child.Sw.S>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
    private val position: Int?,
    private val recyclerView27: RecyclerView
) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {
    private var itemCount = position

    inner class ViewHolder(val binding: SeatShowTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SeatShowTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language
                binding.textView199.text = this.st
                //click
                holder.itemView.setOnClickListener {
                    itemCount = position
                    notifyDataSetChanged()
                    listener.showsClick(this.sid,holder.itemView)
                }
                itemCount?.let { recyclerView27.scrollToPosition(it) }
                if (position == itemCount) {
                    binding.textView199.setTextColor(ContextCompat.getColor(context, R.color.black))
                    binding.linearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.showBackground
                        )
                    )
                    binding.view137.show()
                    val heavy: Typeface = context.resources.getFont(R.font.montserrat_bold)
                    binding.textView199.textSize = 16f
                    binding.textView199.typeface = heavy

                    Constant.focusOnView(itemView, recyclerView27)


                } else {
                    binding.textView199.setTextColor(
                        ContextCompat.getColor(context, R.color.textColorGray)
                    )
                    binding.linearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    binding.view137.invisible()
                    val heavy: Typeface = context.resources.getFont(R.font.montserrat_bold)
                    binding.textView199.textSize = 16f
                    binding.textView199.typeface = heavy
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun showsClick(comingSoonItem: Int, itemView: View)
    }

}