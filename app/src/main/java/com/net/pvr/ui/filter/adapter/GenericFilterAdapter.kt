package com.net.pvr.ui.filter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.ui.filter.adapter.GenericFilterAdapter.GenericFilterViewHolder
import java.util.*

class GenericFilterAdapter(
    var data: ArrayList<String>,
    var context: Context,
    var type: String, //    OnItemSelected onItemSelected;
    private val listener: onFilterItemSelected,
    var selectedItem: HashMap<String, String>,
    var selectedType: ArrayList<String>
) : RecyclerView.Adapter<GenericFilterViewHolder>() {
    var rowIndex = -1
    var countPos = 0
    var flag = false
    fun setAllreadyselected(data: ArrayList<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericFilterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false)
        return GenericFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericFilterViewHolder, position: Int) {
        Log.d("GenericAdapter", "filter selectedType$selectedType")
        val myString = data[position]
        var upperString = ""
        upperString = if (myString.equals("3D", ignoreCase = true) || myString.equals(
                "2D",
                ignoreCase = true
            ) || type.equals("format", ignoreCase = true) || type.contains("cinema")
        ) {
            myString
        } else {
            myString.substring(0, 1).uppercase(Locale.getDefault()) + myString.substring(1)
                .lowercase(Locale.getDefault())
        }
        holder.name.text = upperString
        if (selectedType.size > 0) setAlreadySelected(holder, position)
        //        if(!selectedItem.equals(""))
//        if(myString.equals(selectedItem) && type.equals(selectedType))
//            holder.name.setSelected(true);
//        else holder.name.setSelected(false);
        //setSingleSelectedView(holder,position);
        setView(holder, position)
        if (type.equals("price", ignoreCase = true)) {
            if (flag) {
                rowIndex = countPos
            }
            if (rowIndex == position) {
                holder.name.isSelected = true
            } else if (rowIndex == -1) {
                holder.name.isSelected = false
            } else {
                holder.name.isSelected = false
            }
        }
    }

    private fun setAlreadySelected(holder: GenericFilterViewHolder, position: Int) {
        if (selectedType.size > 0) {
            val containLanguage = selectedType.contains("language")
            if (containLanguage) {
                val index = selectedType.indexOf("language")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.uppercase()
                        .contains(holder.name.text.toString().uppercase())
                ) {
                    holder.name.isSelected = true
                }
            }
            val containGeners = selectedType.contains("geners")
            if (containGeners) {
                val index = selectedType.indexOf("geners")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.uppercase()
                        .contains(holder.name.text.toString().uppercase())
                ) {
                    holder.name.isSelected = true
                }
            }
            val containTime = selectedType.contains("time")
            if (containTime) {
                val index = selectedType.indexOf("time")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.contains(holder.name.text.toString())) {
                    holder.name.isSelected = true
                }
            }
            val containFormat = selectedType.contains("format")
            if (containFormat) {
                val index = selectedType.indexOf("format")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.uppercase()
                        .contains(holder.name.text.toString().uppercase())
                ) {
                    holder.name.isSelected = true
                }
            }
            val containCinemaFormat = selectedType.contains("cinema")
            if (containCinemaFormat) {
                val index = selectedType.indexOf("cinema")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.uppercase()
                        .contains(holder.name.text.toString().uppercase())
                ) {
                    holder.name.isSelected = true
                }
            }
            val containSpecialFormat = selectedType.contains("special")
            if (containSpecialFormat) {
                val index = selectedType.indexOf("special")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.uppercase()
                        .contains(holder.name.text.toString().uppercase())
                ) {
                    holder.name.isSelected = true
                }
            }
            val containPrice = selectedType.contains("price")
            if (containPrice) {
                val index = selectedType.indexOf("price")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.contains(holder.name.text.toString())) {
                    println("selectedType--->" + value + "---" + holder.name.text)
                    holder.name.isSelected = true
                    countPos = position
                    flag = true
                }
            }
            val containAccessability = selectedType.contains("accessability")
            if (containAccessability) {
                val index = selectedType.indexOf("accessability")
                val value = selectedItem[selectedType[index]]
                if (value != null && value.uppercase()
                        .contains(holder.name.text.toString().uppercase())
                ) {
                    holder.name.isSelected = true
                }
            }
        }
    }

    private fun setView(holder: GenericFilterViewHolder, position: Int) {
        holder.name.setOnClickListener {
            val itemSelected = holder.name.text.toString()
            Log.d("GenericAdapter", "filter item name$type")
            if (holder.name.isSelected) {
                holder.name.isSelected = false
                listener.onFilterItemClick(position, type, itemSelected, false)
                rowIndex = -1
            } else {
                holder.name.isSelected = true
                listener.onFilterItemClick(position, type, itemSelected, true)
                rowIndex = position
            }
            //                if (!data.get(position).isDataValues()) {
//                    onItemSelected.onValueSelected(holder.getAdapterPosition(), type, done, true);
//                    listener.onFilterItemClick();
//                    if (!done) {
//                        swapView(holder, position);
//                    }
//                } else {
//                    listener.onFilterItemClick((holder.getAdapterPosition(), type, done, false);
//                    // swapView(holder, position);
//                }
            if (type.equals("price", ignoreCase = true)) {
                flag = false
                notifyDataSetChanged()
            }
        }
        //        swapView(holder, position);
    }

    private fun swapView(holder: GenericFilterViewHolder, position: Int) {
//        if (data.get(position).isDataValues()) {
//            holder.name.setSelected(true);
//            holder.name.setTextColor(context.getResources().getColor(R.color.black));
//        } else {
//            holder.name.setSelected(false);
//            holder.name.setTextColor(context.getResources().getColor(R.color.gray_));
//
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class GenericFilterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView

        init {
            name = itemView.findViewById(R.id.name)
        }
    }

    interface onFilterItemSelected {
        fun onFilterItemClick(
            position: Int,
            type: String?,
            itemSelected: String?,
            isSelected: Boolean
        )
    }
}