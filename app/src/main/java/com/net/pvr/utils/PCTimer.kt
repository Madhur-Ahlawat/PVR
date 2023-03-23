package com.net.pvr.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.net.pvr.R
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.viewModel.FoodViewModel
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.login.otpVerify.viewModel.OtpVerifyViewModel
import com.net.pvr.ui.login.viewModel.LoginViewModel
import com.net.pvr.ui.payment.viewModel.PaymentViewModel
import com.net.pvr.ui.seatLayout.viewModel.SeatLayoutViewModel
import com.net.pvr.ui.summery.viewModel.SummeryViewModel

@SuppressLint("StaticFieldLeak")
object PCTimer {
    var timer: CountDownTimer? = null
    var context: Activity? = null
    var viewModel:ViewModel? = null
    fun startTimer(
        expireTime: Int, alertTime: Int, cinemaID: String?,
        sessionID: String?, paymentType: String?, bookingid: String?, b: Boolean,viewModels: ViewModel
    ) {
        Constant.timerCounter = Constant.timerCounter + 1
        viewModel = viewModels
        timer = object : CountDownTimer(expireTime.toLong(), 1000) {
            override fun onTick(l: Long) {
                if (l < alertTime) {
                    try {
                        val intent = Intent()
                        intent.action = Constant.BroadCast.ACTIVE_BROADCAST
                        intent.putExtra(Constant.IntentKey.IS_SESSION_EXPIRE, false)
                        intent.putExtra("value", b)
                        intent.putExtra(Constant.IntentKey.REMAING_TIME, l)
                        context!!.sendBroadcast(intent)
                        println("remaingTime123-->$l")
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFinish() {
                try {
                    stopTimer()
                    try {
                        (viewModel as SummeryViewModel).cancelTrans(
                            cinemaID!!,
                            sessionID!!,
                            bookingid!!
                        )
                    }catch (e:ClassCastException){
                        (viewModel as PaymentViewModel).cancelTrans(cinemaID!!,sessionID!!,bookingid!!)
                    }catch (e:ClassCastException){
                        (viewModel as SeatLayoutViewModel).cancelTrans(cinemaID!!,sessionID!!,bookingid!!)
                    }catch (e:ClassCastException){
                        (viewModel as FoodViewModel).cancelTrans(cinemaID!!,sessionID!!,bookingid!!)
                    }catch (e:ClassCastException){
                        (viewModel as LoginViewModel).cancelTrans(cinemaID!!,sessionID!!,bookingid!!)
                    }catch (e:ClassCastException){
                        (viewModel as OtpVerifyViewModel).cancelTrans(cinemaID!!,sessionID!!,bookingid!!)
                    }
                    println("cancelSession==========")
                    try {
                        println("cancelSession==========")
                            val dialog = OptionDialog(
                                context!!,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                context!!.getString(R.string.your_session_expired_message),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                    context?.launchActivity(
                                        HomeActivity::class.java,
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    )
                                },
                                negativeClick = {}
                            )
                            dialog.show()
                       // }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    /*   Intent intent = new Intent();
                    intent.setAction(PCConstants.BroadCast.ACTIVE_BROADCAST);
                    intent.putExtra(PCConstants.IntentKey.IS_SESSION_EXPIRE, true);
                    intent.putExtra(PCConstants.IntentKey.REMAING_TIME, 0);
                    context.sendBroadcast(intent);*/
                } catch (e: Exception) {
                  e.printStackTrace()
                }
            }
        }.start()
    }

    fun stopTimer() {
        if (timer != null) {
            println("Call2")
            timer!!.cancel()
            //            timer.onFinish();
//            timer.onFinish();
        }
    }

    private fun isAppOnForeground(context: Context?, appPackageName: String): Boolean {
        val activityManager =
            context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == appPackageName) {
                //                Log.e("app",appPackageName);
                return true
            }
        }
        return false
    }
}