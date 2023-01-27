package com.net.pvr1.utils

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import com.net.pvr1.utils.Constant.Companion.TimerTime

class BroadcastService : Service() {
    var bi = Intent(COUNTDOWN_BR)
    private var cdt: CountDownTimer? = null
//    var context: Context? = null
    override fun onCreate() {
        super.onCreate()

        cdt = object : CountDownTimer(TimerTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bi.putExtra("countdown", millisUntilFinished)
                sendBroadcast(bi)
            }

            override fun onFinish() {
//                val dialog = OptionDialog((context!!),
//                    R.mipmap.ic_launcher_foreground,
//                    R.string.blank_space,
//                    getString(R.string.sessionExpired),
//                    positiveBtnText = R.string.yes,
//                    negativeBtnText = R.string.no,
//                    positiveClick = {
//                        val intent = Intent(context as Activity, HomeActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                        startActivity(intent)
//                        (context as Activity).finish()
//                    },
//                    negativeClick = {})
//                dialog.show()
//                val intent = Intent(context, HomeActivity::class.java)
//                startActivity(intent)
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