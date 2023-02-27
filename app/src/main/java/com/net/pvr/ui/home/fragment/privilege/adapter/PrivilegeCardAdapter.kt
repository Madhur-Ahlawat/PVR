package com.net.pvr.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr.R
import com.net.pvr.databinding.PrivilegeCardsBinding
import com.net.pvr.ui.home.fragment.privilege.response.PrivilegeCardData
import com.net.pvr.utils.Constant
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.squareup.picasso.Picasso


@Suppress("DEPRECATION")
class PrivilegeCardAdapter(
    private var nowShowingList: ArrayList<PrivilegeCardData>,
    private var context: Activity,
    private val preferences: PreferenceManager,
    private val listener: RecycleViewItemClickListener
) : RecyclerView.Adapter<PrivilegeCardAdapter.ViewHolder>() {
    private val displayMetrics = DisplayMetrics()
    private var screenWidth = 0


    inner class ViewHolder(val binding: PrivilegeCardsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        val binding = PrivilegeCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists",
        "UseCompatLoadingForDrawables"
    )
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemWidth = ((screenWidth - 8) / 1.20f).toInt()
        val layoutParams =
            LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        when (position) {
            0 -> {
                layoutParams.leftMargin = Constant().convertDpToPixel(30f, context)
                layoutParams.rightMargin = Constant().convertDpToPixel(5f, context)
            }
            nowShowingList.size - 1 -> {
                layoutParams.leftMargin = Constant().convertDpToPixel(5f, context)
                layoutParams.rightMargin = Constant().convertDpToPixel(30f, context)
            }
            else -> {
                layoutParams.leftMargin = Constant().convertDpToPixel(12f, context)
                layoutParams.rightMargin = Constant().convertDpToPixel(12f, context)
            }
        }
        holder.itemView.layoutParams = layoutParams

        // holder
        val cardData = nowShowingList[position]
        if (!nowShowingList[position].lock!!) {
            if (cardData.type == null || cardData.type.equals("P")) {
                holder.binding.logoImg.setImageResource(R.drawable.passport_logo_new)
                holder.binding.rlMid.setBackgroundResource(R.drawable.gradient_loyalty_corner)
                holder.binding.passportViewB.hide()
                holder.binding.qrCodeImg.show()
                holder.binding.tcBg.hide()
            } else if (cardData.type.equals("PP")) {
                holder.binding.logoImg.setImageResource(R.drawable.loyalty_new_logo)
                holder.binding.rlMid.setBackgroundResource(R.drawable.gradient_passport_corner)
                holder.binding.rlMid.backgroundTintList = context.resources.getColorStateList(R.color.pass)
                holder.binding.passportViewB.show()
                holder.binding.tcBg.hide()
                holder.binding.qrCodeImg.show()
                holder.binding.totalVisits.text = cardData.cardImg + " Visits"
            } else {
                holder.binding.logoImg.setImageResource(R.drawable.kotak_logo)
                holder.binding.rlMid.setBackgroundResource(R.drawable.gradient_kotak_corner)
                holder.binding.passportViewB.hide()
                holder.binding.tcBg.show()
                holder.binding.qrCodeImg.show()
            }
            holder.binding.loyaltyCard.show()
            holder.binding.otherCardView.hide()
            holder.binding.tvPointsData.text = cardData.points + " Points"
            val image: String = preferences.getString(Constant.SharedPreference.USER_IMAGE)
            holder.binding.tvName.text = preferences.getString(Constant.SharedPreference.USER_NAME)
            if (!TextUtils.isEmpty(image)) {
                Picasso.get().load(image)
                    .error(context.resources.getDrawable(R.drawable.user_png_img))
                    .placeholder(context.resources.getDrawable(R.drawable.user_png_img))
                    .into(holder.binding.imgUserImage)

            }
            Picasso.get().load(Constant().getLoyaltyQr(preferences.getString(Constant.SharedPreference.USER_NUMBER),"80x80"))
                    .error(context.resources.getDrawable(R.drawable.user_png_img))
                    .placeholder(context.resources.getDrawable(R.drawable.user_png_img))
                    .into(holder.binding.imgUserImage)

            holder.binding.qrCodeImg.setOnClickListener {
                listener.onQrClick()
            }
        } else {
            Picasso.get().load(cardData.cardImg)
                .error(context.resources.getDrawable(R.drawable.user_png_img))
                .placeholder(context.resources.getDrawable(R.drawable.user_png_img))
                .into(holder.binding.otherCardBg)
            holder.binding.loyaltyCard.hide()
            holder.binding.otherCardView.show()

        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is LinearLayoutManager && itemCount > 0) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
//                    val visiblePosition = manager.findFirstCompletelyVisibleItemPosition()
                }
            })
        }
    }

    interface RecycleViewItemClickListener {
        fun onQrClick()
    }

}