package com.net.pvr1.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.utils.Constant.Companion.EXTANDTIME


class BroadcastService : Service() {
    var bi = Intent(COUNTDOWN_BR)
    private var cdt: CountDownTimer? = null
    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = this


        cdt = object : CountDownTimer(EXTANDTIME.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bi.putExtra("countdown", millisUntilFinished)
                sendBroadcast(bi)
            }

            override fun onFinish() {
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        cdt?.start()
    }


    override fun onDestroy() {
        cdt?.cancel()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {
        const val COUNTDOWN_BR = "your_package_name.countdown_br"
    }
}