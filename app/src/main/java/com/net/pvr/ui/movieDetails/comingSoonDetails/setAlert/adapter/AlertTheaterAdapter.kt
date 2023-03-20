package com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.SetAlertItemBinding
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr.ui.movieDetails.comingSoonDetails.setAlert.SetAlertActivity
import com.net.pvr.utils.printLog
import com.net.pvr.utils.toast
import java.util.*


class AlertTheaterAdapter(
    private val context: Context,
    private var nowShowingList: ArrayList<BookingRetrievalResponse.Output.C>,
    private var unableDisable: LiveData<Boolean>,
    private var toggle: SwitchCompat,
    private var onItemClick: (item: BookingRetrievalResponse.Output.C, addToList: Boolean) -> Unit
) : RecyclerView.Adapter<AlertTheaterAdapter.ViewHolder>(), Filterable {

    lateinit var mContext: Context
    private var selectedItemCount = 0
    private var filteredData = ArrayList(nowShowingList)

    private var theatreList: List<BookingRetrievalResponse.Output.C> = nowShowingList

    inner class ViewHolder(val binding: SetAlertItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(
            SetAlertItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                binding.nameTv.text = this.n
                binding.distanceTv.text = this.d
                binding.detailTV.text = this.ad

                if (toggle.isChecked) {
                    unableDisable.observe(context as SetAlertActivity) {
                        println("SetAlertActivity--->$it")
                        if (it) {
                            binding.imageView134.setImageResource(R.drawable.curve_select)
                            binding.constraintLayout92.setBackgroundResource(
                                R.drawable.alert_curve_ui_select
                            )
                            holder.itemView.isClickable = false
                        } else {
                            binding.imageView134.setImageResource(R.drawable.curve_unselect)
                            binding.constraintLayout92.setBackgroundResource(
                                R.drawable.alert_curve_ui
                            )
                            holder.itemView.isClickable = true

                        }
                    }
                }

                holder.itemView.setOnClickListener {
                    if (this.itemSelectedOrNot) {
                        onItemClick(this, false)


                        binding.imageView134.setImageResource(R.drawable.curve_unselect)
                        binding.constraintLayout92.setBackgroundResource(
                            R.drawable.alert_curve_ui
                        )


                        this.itemSelectedOrNot = false
                        selectedItemCount--

                    } else {

                        if (selectedItemCount < 5) {
                            this.itemSelectedOrNot = true
                            selectedItemCount++
                            onItemClick(this, true)
                        }

                        if (this.itemSelectedOrNot) {
                            try {
                                binding.imageView134.setImageResource(R.drawable.curve_select)
                                binding.constraintLayout92.setBackgroundResource(R.drawable.alert_curve_ui_select)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else {

                            try {
                                binding.imageView134.setImageResource(R.drawable.curve_unselect)
                                binding.constraintLayout92.setBackgroundResource(
                                    R.drawable.alert_curve_ui
                                )
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                    notifyDataSetChanged()
                }


            }
        }

    }

    override fun getItemCount(): Int {
        //  return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
        return if (filteredData.isNotEmpty()) filteredData.size else 0
    }

    override fun getFilter(): Filter {
        return TheaterFilter()
    }

    inner class TheaterFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString().lowercase(Locale.ROOT)
            val result = FilterResults()
            val listForSearch = theatreList
            val filteredItems = ArrayList<BookingRetrievalResponse.Output.C>()
            listForSearch.forEach {
                if (it.n.lowercase().contains(filterString)) {
                    filteredItems.add(it)
                }
            }
            if (filteredItems.size == 0) {
                result.values = nowShowingList
            }
            result.values = filteredItems
            return result
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results!!.values as ArrayList<BookingRetrievalResponse.Output.C>
            notifyDataSetChanged()
        }
    }
}