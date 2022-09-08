package com.net.pvr1.ui.splash

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySplashBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.food.FoodActivity
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.onBoarding.LandingActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySplashBinding? = null
    private var networkDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()

        //Check interNet Connection
        if (isConnected()) {
            movedNext()
        } else {
            networkDialog()
        }
        printLog("CheckLogin--->${Constant.ON_BOARDING_CLICK}")
    }

    private fun movedNext() {
        val runnable = Runnable {
            if (preferences.getBoolean(Constant.IS_LOGIN)) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (!Constant.ON_BOARDING_CLICK) {
                val intent = Intent(this@SplashActivity, FoodActivity::class.java)
//                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
//                    val intent = Intent(this@SplashActivity, LandingActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 3000)
    }

    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return connected
    }

    private fun networkDialog() {
        networkDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        networkDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        networkDialog?.setContentView(R.layout.internet_dialog)
        networkDialog?.show()
        val window: Window? = networkDialog?.window
        val wlp: WindowManager.LayoutParams? = window?.attributes
        wlp?.gravity = Gravity.CENTER
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv())
        window?.attributes = wlp
        networkDialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val restart = networkDialog?.findViewById<View>(R.id.view49)
        val network = networkDialog?.findViewById<View>(R.id.view48)
        network?.setOnClickListener {
            val intent2 = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent2)
        }
        restart?.setOnClickListener {
            recreate()
        }
    }

}