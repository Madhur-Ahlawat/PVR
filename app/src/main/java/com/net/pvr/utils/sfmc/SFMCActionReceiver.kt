package com.net.pvr.utils.sfmc

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SFMCActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val nID = intent.getIntExtra("NT", 0)
        println("nID---->" + intent.getStringExtra("VALUE"))
        println("nID123---->" + intent.data)
        val intent1 = Intent(Intent.ACTION_VIEW, intent.data)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent1)

        manager.cancel(nID) //doesnt work
        manager.cancelAll() //works
    }
}