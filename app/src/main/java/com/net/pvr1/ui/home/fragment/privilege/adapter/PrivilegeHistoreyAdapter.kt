package com.net.pvr1.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.HistoreyItemBinding
import com.net.pvr1.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr1.utils.PreferenceManager
import com.net.pvr1.utils.hide


@Suppress("DEPRECATION")
class PrivilegeHistoreyAdapter(
    private var nowShowingList: ArrayList<LoyaltyDataResponse.Hi>,
    private var context: Activity,
    private val preferences: PreferenceManager
) : RecyclerView.Adapter<PrivilegeHistoreyAdapter.ViewHolder>() {

    var plus: Drawable? = null
    var cross: Drawable? = null
    var unlock: Drawable? = null
    var tick: Drawable? = null
    var gray_color = 0
    inner class ViewHolder(val binding: HistoreyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoreyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists",
        "UseCompatLoadingForDrawables"
    )
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        // holder
        val cardData = nowShowingList[position]
        plus = context.resources.getDrawable(R.drawable.ic_plus_icon)
        cross = context.resources.getDrawable(R.drawable.ic_cross_pri)
        unlock = context.resources.getDrawable(R.drawable.ic_unlocked_pri)
        tick = context.resources.getDrawable(R.drawable.ic_tick_pri)
        gray_color = context.resources.getColor(R.color.gray)
        if (cardData.ty != null) {
            when (cardData.ty) {
                "p" -> holder.binding.ivImage.setImageDrawable(plus)
                "t" -> holder.binding.ivImage.setImageDrawable(tick)
                "u" -> holder.binding.ivImage.setImageDrawable(unlock)
                "c" -> holder.binding.ivImage.setImageDrawable(cross)
            }
            holder.binding.vocPoints.text = Html.fromHtml(cardData.l1)
            holder.binding.vocDate.text = Html.fromHtml(cardData.l2)
            if (cardData.b)
                holder.binding.vocPoints.setTextColor(gray_color)
        } else {
            holder.binding.vocPoints.text = "Membership unlocked"
            holder.binding.ivHistroy.hide()
            holder.binding.ivImage.setImageDrawable(context.resources.getDrawable(R.drawable.member_icon))
            holder.binding.vocDate.text = cardData.spent_type
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun onItemClick()
    }

}