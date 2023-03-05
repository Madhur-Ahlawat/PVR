package com.net.pvr.ui.home.fragment.more.offer.list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr.R
import com.net.pvr.databinding.OfferItemBinding
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse

//category

class OfferListAdapter(
    private var context: Context,
    private var listener: RecycleViewItemClickListenerCity,
    private var offer: ArrayList<MOfferResponse.Output.Offer>
) :
    RecyclerView.Adapter<OfferListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: OfferItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OfferItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(offer[position]) {
//              Image
                Glide.with(context)
                    .load(this.i)
                    .error(R.drawable.placeholder_horizental)
                    .into(binding.offerImg)

//                title
                binding.name.text = this.c

//                expiry
                binding.expiry.text = "Valid till " + this.vt

                holder.itemView.setOnClickListener {
                    listener.itemClick(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (offer.isNotEmpty()) offer.size else 0
    }


    interface RecycleViewItemClickListenerCity {
        fun itemClick(comingSoonItem: MOfferResponse.Output.Offer)

    }

}