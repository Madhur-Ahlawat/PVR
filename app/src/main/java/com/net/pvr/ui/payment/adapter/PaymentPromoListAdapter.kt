package com.net.pvr.ui.payment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.PromoItemBinding
import com.net.pvr.ui.payment.response.PromoCodeList
import java.util.*

//category

class PaymentPromoListAdapter(
    private var nowShowingList: ArrayList<PromoCodeList.Output>,
    private var filterList: ArrayList<PromoCodeList.Output>,
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
) : RecyclerView.Adapter<PaymentPromoListAdapter.ViewHolder>(),Filterable {
    inner class ViewHolder(val binding: PromoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PromoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {

                if (this.image!="") {
                    Glide.with(context)
                        .load(this.image)
                        .error(R.drawable.placeholder_horizental)
                        .into(binding.imageView5)
                } else {
                    binding.imageView5.setImageResource(R.drawable.placeholder_horizental)
                }
//                if (this.promocode == "") {
//                    binding.textView7.visibility = View.GONE
//                    binding.btnApplyCoupon.visibility = View.GONE
//                    binding.textView6.visibility = View.GONE
//                } else {
//                    binding.textView7.visibility = View.VISIBLE
//                    binding.btnApplyCoupon.visibility = View.VISIBLE
//                    binding.textView6.visibility = View.VISIBLE
//                }
                binding.titleText.text = this.title
                binding.textView7.text = this.promocode
                holder.itemView.setOnClickListener(View.OnClickListener {
                    listener.applyClick(
                        this
                    )
                })
//                binding.textView5.setOnClickListener(View.OnClickListener { listener.onTNCClick(this) })

            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun applyClick(data: PromoCodeList.Output)
//        fun onTNCClick(data: PromoCodeList.Output)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    try {
                        try {
                            val theatreListAdd: java.util.ArrayList<PromoCodeList.Output> =
                                java.util.ArrayList<PromoCodeList.Output>()
                            for (data in nowShowingList) {
                                if (data.title.toLowerCase().contains(
                                        constraint.toString().lowercase(Locale.getDefault())
                                    )
                                ) {
                                    theatreListAdd.add(data)
                                }
                            }
                            // Assign the data to the FilterResults
                            filterResults.values = theatreListAdd
                            filterResults.count = theatreListAdd.size
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                nowShowingList = results.values as ArrayList<PromoCodeList.Output>
                notifyDataSetChanged()
                try {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}