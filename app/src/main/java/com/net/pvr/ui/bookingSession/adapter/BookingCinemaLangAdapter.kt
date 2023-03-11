package com.net.pvr.ui.bookingSession.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.databinding.ItemBookingCinemaLangBinding
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.utils.hide
import com.net.pvr.utils.show
import com.net.pvr.R


class BookingCinemaLangAdapter(
    private var nowShowingList: List<BookingResponse.Output.Cinema.Child.Sw>,
    private var context: Activity,
    private val ccid: String,
    private val ccn: String,
    private val at: String,
    private val adlt: Boolean,
) :
    RecyclerView.Adapter<BookingCinemaLangAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemBookingCinemaLangBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingCinemaLangBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                if (this.lng.contains("Atmos") || this.lng.contains("4Dx") || this.lng.contains("IMAX") || this.lng.contains("DBOX")) {
                    binding.atInfo.show()
                } else {
                    binding.atInfo.hide()
                }
                binding.textView106.text = this.lng
//                //Recycler
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.FLEX_START
                val bookingShowsParentAdapter =
                    BookingShowsTimeAdapter(this.s!!, context, ccid,ccn,at,adlt)
                binding.recyclerView11.layoutManager = layoutManager
                binding.recyclerView11.adapter = bookingShowsParentAdapter
                binding.atInfo.setOnClickListener {
                    showATDialog(context,this.lng)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    private fun showATDialog(mContext: Context?, format: String) {

//        type = new ArrayList<>();
        val dialog = BottomSheetDialog(context, R.style.NoBackgroundDialogTheme)
        val behavior: BottomSheetBehavior<*> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.atinfo_dialoge)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv: TextView = dialog.findViewById<View>(R.id.message) as TextView
        val cross: TextView = dialog.findViewById<View>(R.id.cross) as TextView
        val close: TextView = dialog.findViewById<View>(R.id.close) as TextView
        val dboxmessage: TextView = dialog.findViewById<View>(R.id.dboxmessage) as TextView
        val logot = dialog.findViewById<View>(R.id.logot) as ImageView
        close.text = "Close"
        val dboxview = dialog.findViewById<View>(R.id.dboxview)
        logot.hide()
        dboxview?.hide()
        dboxmessage.hide()
        cross.setOnClickListener { dialog.dismiss() }
        close.setOnClickListener { dialog.dismiss() }
        dialog.show()

        if (format.contains("Atmos")) {
            logot.visibility = View.VISIBLE
            cross.text = "Dolby Atmos"
            tv.text = mContext?.resources?.getString(R.string.atmos_text)

        } else if (format.contains("IMAX")) {
            logot.visibility = View.GONE
            cross.text = "IMAX"
            tv.text = mContext?.resources?.getString(R.string.imax_text)
        } else if (format.contains("4Dx")) {
            cross.text = "4DX"
            logot.visibility = View.GONE
            tv.text = mContext?.resources?.getString(R.string.fdx_text)
        } else if (format.contains("PlayHouse")) {
            cross.text = "PlayHouse"
            logot.visibility = View.GONE
            tv.text = mContext?.resources?.getString(R.string.playhouse_text)
        } else if (format.contains("PVR Gold")) {
            cross.text = "PVR Gold"
            logot.visibility = View.GONE
            tv.text = mContext?.resources?.getString(R.string.pvrgold_text)
        } else if (format.contains("PVR ONYX")) {
            cross.text = "PVR ONYX"
            logot.visibility = View.GONE
            tv.text = mContext?.resources?.getString(R.string.pvronyx_text)
        } else if (format.contains("DBOX")) {
            cross.text = "DBOX"
            logot.visibility = View.GONE
            tv.text = mContext?.resources?.getString(R.string.dbox_dialog_show)
        }
    }


}