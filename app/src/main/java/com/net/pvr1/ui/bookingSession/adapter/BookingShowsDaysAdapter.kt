package com.net.pvr1.ui.bookingSession.adapter

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
import com.net.pvr1.utils.invisible
import com.net.pvr1.utils.show


class BookingShowsDaysAdapter(
    private var nowShowingList: List<BookingResponse.Output.Dy>,
    private var context: Context,
    private var listener:RecycleViewItemClickListenerCity,
) :
    RecyclerView.Adapter<BookingShowsDaysAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: SeatShowTimeItemBinding) : RecyclerView.ViewHolder(binding.root)
    private var itemCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SeatShowTimeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(nowShowingList[position]){
                binding.textView255.show()
                //Date
                binding.textView199.text=this.d
                //Name
                binding.textView255.text=this.wd

                //click
                holder.itemView.setOnClickListener {
                    itemCount = position
                    notifyDataSetChanged()
                    listener.showsDaysClick(this,holder.itemView)
                }

                if (position == itemCount) {
                    binding.textView199.setTextColor(ContextCompat.getColor(context, R.color.black))
                    binding.textView255.setTextColor(ContextCompat.getColor(context, R.color.black))
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

                } else {
                    binding.textView199.setTextColor(ContextCompat.getColor(context, R.color.textColorGray))
                    binding.textView255.setTextColor(ContextCompat.getColor(context, R.color.gray_1))
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
        fun showsDaysClick(comingSoonItem: BookingResponse.Output.Dy, itemView: View)
    }

}