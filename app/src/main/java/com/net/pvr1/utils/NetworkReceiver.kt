package com.net.pvr1.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.net.pvr1.databinding.InternetDialogBinding

class NetworkReceiver : BroadcastReceiver() {
    var dialog: Dialog? = null
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        var status = NetworkUtil.getConnectivityStatusString(context)
        dialog = Dialog(context, android.R.style.ThemeOverlay_Material_ActionBar)
        val inflater = LayoutInflater.from(context)
        val bindingBottom = InternetDialogBinding.inflate(inflater)
        dialog?.setContentView(bindingBottom.root)

        bindingBottom.view49.setOnClickListener { v: View? ->
            (context as Activity).finish()
            context.startActivity(context.intent)
            context.overridePendingTransition(0, 0)
        }
        bindingBottom.view48.setOnClickListener {
            (context as Activity).startActivity(
                Intent(
                    Settings.ACTION_WIRELESS_SETTINGS
                )
            )
        }
        status?.let { Log.d("network", it) }

        try {
            if (status?.isEmpty() == true || status == "No internet is available" || status == "No Internet Connection") {
                status = "No Internet Connection"
                dialog?.show()
            }
            else{
                dialog?.hide()
            }
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

    }
}