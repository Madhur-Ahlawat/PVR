package com.net.pvr1.ui.setAlert.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.SetAlertItemBinding
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.ui.setAlert.SetAlertActivity
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import java.util.*


class AlertTheaterAdapter(
    private val context: Context,
    private var nowShowingList: ArrayList<BookingRetrievalResponse.Output.C>,
    private var unableDisable: LiveData<Boolean>,
    private var onItemClick: (item: BookingRetrievalResponse.Output.C, addToList: Boolean) -> Unit
) : RecyclerView.Adapter<AlertTheaterAdapter.ViewHolder>(), Filterable {

    lateinit var mContext: Context
    private var selectedItemCount = 0
    var filteredData = ArrayList(nowShowingList)

    var theatreList: List<BookingRetrievalResponse.Output.C> = nowShowingList

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

                if (this.itemSelectedOrNot) {
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
                unableDisable.observe(context as SetAlertActivity) {
                    if (it) {
                        binding.viewDisableLayout.show()
                        holder.itemView.isClickable = false
                    } else {
                        binding.viewDisableLayout.hide()
                        holder.itemView.isClickable = true
                        holder.itemView.setOnClickListener {
                            if (this.itemSelectedOrNot) {
                                this.itemSelectedOrNot = false
                                selectedItemCount--
                                onItemClick(this, false)
                            } else {
                                if (selectedItemCount < 5) {
                                    this.itemSelectedOrNot = true
                                    selectedItemCount++
                                    onItemClick(this, true)
                                }
                            }
                            notifyDataSetChanged()
                        }
                    }
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
            if (filteredItems.size == 0){
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