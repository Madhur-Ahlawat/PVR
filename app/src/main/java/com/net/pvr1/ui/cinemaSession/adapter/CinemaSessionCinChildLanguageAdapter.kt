package com.net.pvr1.ui.cinemaSession.adapter

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
import com.net.pvr1.R
import com.net.pvr1.databinding.ItemCinemaSessionLangChildBinding
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


@Suppress("NAME_SHADOWING")
class CinemaSessionCinChildLanguageAdapter(
    private var nowShowingList: ArrayList<CinemaSessionResponse.Child.Mv.Ml>,
    private var context: Context,
    private var cinemaId: String?,
    private var showType: Int?,
    private var ccn: String,
    private var adlt: Boolean,
    private var at: String,
    private var mih: String
) : RecyclerView.Adapter<CinemaSessionCinChildLanguageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCinemaSessionLangChildBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCinemaSessionLangChildBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.textView95.text = this.lng
                if (this.lng.contains("Atmos") || this.lng.contains("4Dx") || this.lng.contains("IMAX") || this.lng.contains("DBOX")) {
                    binding.atInfo.show()
                } else {
                    binding.atInfo.hide()
                }
                //RecyclerView
                val layoutManager = FlexboxLayoutManager(context)
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.alignItems = AlignItems.FLEX_START
                val cinemaSessionLanguageAdapter = CinemaSessionTimeAdapter(this.s, context,cinemaId,ccn,adlt,at,mih)
                binding.recyclerView6.layoutManager = layoutManager
                binding.recyclerView6.adapter = cinemaSessionLanguageAdapter
                binding.atInfo.setOnClickListener {
                    showATDialog(context,this.lng)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (showType==0){
            1
        }else{
            nowShowingList.size
        }
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
        close.text = "CLOSE"
        val dboxview = dialog.findViewById<View>(R.id.dboxview)
        logot.hide()
        dboxview?.hide()
        dboxmessage.hide()
        cross.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        close.setOnClickListener(View.OnClickListener { dialog.dismiss() })
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