package com.net.pvr.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.databinding.TransNewBinding
import com.net.pvr.ui.home.fragment.privilege.response.PassportHistory
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr.utils.hide


@Suppress("DEPRECATION")
class TransHistoryAdapter(
    private var hisList: ArrayList<PassportHistory.History>,
    private var context: Activity
) : RecyclerView.Adapter<TransHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TransNewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists",
        "UseCompatLoadingForDrawables"
    )
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // holder
        val cardData = hisList[position]


        /*    if (!TextUtils.isEmpty(hisList.get(position).getPointsEarned())) {
            holder.voc_points.setText(hisList.get(position).getPointsEarned() + " points earned");
        }*/


        /* if (size>0 && position ==size - 1) {
            holder.voc_points.setText("Became a member");
            holder.ivHistroy.setVisibility(View.GONE);
            holder.ivImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.member_icon));
            holder.voc_date.setText(hisList.get(position).getSt());
        }
        else
        {*/
        // System.out.println("out_list=========" + hisList.get(position).getB());
        holder.binding.vocDate.text = hisList[position].complexName + " | " + hisList[position].redeemedDate
        if (hisList.size - position == 1) {
            holder.binding.tvNum.text = "Visit " + (hisList.size - position) + "st"
            holder.binding.vocPoints.text = "Visit " + (hisList.size - position) + "st"
        } else if (hisList.size - position == 2) {
            holder.binding.tvNum.text = "Visit " + (hisList.size - position) + "nd"
            holder.binding.vocPoints.text = "Visit " + (hisList.size - position) + "nd"
        } else if (hisList.size - position == 3) {
            holder.binding.tvNum.text = "Visit " + (hisList.size - position) + "rd"
            holder.binding.vocPoints.text = "Visit " + (hisList.size - position) + "rd"
        } else {
            holder.binding.tvNum.text = "Visit " + (hisList.size - position) + "th"
            holder.binding.vocPoints.text = "Visit " + (hisList.size - position) + "th"
        }
        if (position == hisList.size - 1) {
            holder.binding.ivHistroy.hide()
        }
    }

    override fun getItemCount(): Int {
        return if (hisList.isNotEmpty()) hisList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun openRedirection()
    }

}