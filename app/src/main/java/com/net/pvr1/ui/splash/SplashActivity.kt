package com.net.pvr1.ui.splash

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySplashBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.location.enableLocation.EnableLocationActivity
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.ui.splash.onBoarding.LandingActivity
import com.net.pvr1.ui.splash.response.SplashResponse
import com.net.pvr1.ui.splash.viewModel.SplashViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySplashBinding? = null
    private var networkDialog: Dialog? = null
    private val authViewModel: SplashViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private val MyPREFERENCES = "MyPrefs"
    private var sharedpreferences: SharedPreferences? = null
    private val OnBoardingClick = "Name"
    private var clickOnBoarding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        //OnBoarding Click Check
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        clickOnBoarding = sharedpreferences?.getBoolean(OnBoardingClick, false)!!

        //Check interNet Connection
        if (isConnected()) {
            if (preferences.getCityName() == "") {
                authViewModel.splash("")
            } else {
                authViewModel.splash(preferences.getCityName())
            }
        } else {
            networkDialog()
        }
        summeryDetails()

    }

    private fun movedNext() {
        val runnable = Runnable {
            if (preferences.getIsLogin()) {
                printLog("location--->${Constant().locationServicesEnabled(this@SplashActivity)}---->${preferences.getCityName()}")
                if (!Constant().locationServicesEnabled(this@SplashActivity) ) {
                    val intent = Intent(this@SplashActivity, EnableLocationActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (preferences.getCityName() == "") {
                    val intent = Intent(this@SplashActivity, SelectCityActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                if (!clickOnBoarding) {
                    val intent = Intent(this@SplashActivity, LandingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
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
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
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


    //Splash
    private fun summeryDetails() {
        authViewModel.liveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                }
            }
        }

    }

    private fun retrieveData(output: SplashResponse.Output) {
        preferences.saveString(Constant.SharedPreference.NT, output.nt)
        preferences.saveString(Constant.SharedPreference.NTBT, output.ntbn)
        preferences.saveString(Constant.SharedPreference.IS_HL, output.hl)
        preferences.saveString(Constant.SharedPreference.IS_LY, output.ly)
        movedNext()
    }

}