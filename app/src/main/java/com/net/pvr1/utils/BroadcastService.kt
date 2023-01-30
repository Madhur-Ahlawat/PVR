package com.net.pvr1.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import com.net.pvr1.ui.food.viewModel.FoodViewModel
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.login.otpVerify.viewModel.OtpVerifyViewModel
import com.net.pvr1.ui.login.viewModel.LoginViewModel
import com.net.pvr1.ui.payment.viewModel.PaymentViewModel
import com.net.pvr1.ui.summery.viewModel.SummeryViewModel
import com.net.pvr1.utils.Constant.Companion.EXTANDTIME
import com.net.pvr1.utils.Constant.Companion.viewModel

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
                if (viewModel!=null) {
                    when (viewModel) {
                        is SummeryViewModel -> {
                            (viewModel as SummeryViewModel).cancelTrans(
                                Constant.CINEMA_ID, Constant.TRANSACTION_ID, Constant.BOOKING_ID
                            )
                        }
                        is PaymentViewModel -> {
                            (viewModel as PaymentViewModel).cancelTrans(
                                Constant.CINEMA_ID, Constant.TRANSACTION_ID, Constant.BOOKING_ID
                            )
                        }
                        is LoginViewModel -> {
                            (viewModel as LoginViewModel).cancelTrans(
                                Constant.CINEMA_ID, Constant.TRANSACTION_ID, Constant.BOOKING_ID
                            )
                        }
                        is OtpVerifyViewModel -> {
                            (viewModel as OtpVerifyViewModel).cancelTrans(
                                Constant.CINEMA_ID, Constant.TRANSACTION_ID, Constant.BOOKING_ID
                            )
                        }
                        is FoodViewModel -> {
                            (viewModel as FoodViewModel).cancelTrans(
                                Constant.CINEMA_ID, Constant.TRANSACTION_ID, Constant.BOOKING_ID
                            )
                        }
                    }
                }
                val intent = Intent(context, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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