package com.net.pvr.ui.splash

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySplashBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.FoodActivity
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.location.selectCity.SelectCityActivity
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.scanner.ScannerActivity
import com.net.pvr.ui.scanner.bookings.SelectBookingsActivity
import com.net.pvr.ui.splash.onBoarding.LandingActivity
import com.net.pvr.ui.splash.response.SplashResponse
import com.net.pvr.ui.splash.viewModel.SplashViewModel
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.DONATION
import com.net.pvr.utils.Constant.Companion.newTag
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
    var path = ""
    var path_part = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        val intent = intent
        val data = intent.data


        if (data != null) {
            if (data.toString().contains("/food") || data.toString().contains("/booking") || data.toString().contains("/getqrcode")) {
                path = data.toString()
//                if (path.contains("utm"))
//                    sendGACampaign(path, "Splash")
                path_part = data.path.toString()

            } else {
                val token = data.getQueryParameter("token")
                //if (token != null) PCApplication.InfyToken = token
            }
        }

        //OnBoarding Click Check
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        clickOnBoarding = sharedpreferences?.getBoolean(OnBoardingClick, false)!!

        //Check interNet Connection
        if (Util.isRooted(this) == true) {
            finish()
        } else {
            if (isConnected()) {
                if (preferences.getCityName() == "") {
                    authViewModel.splash("")
                } else {
                    authViewModel.splash(preferences.getCityName())
                }
            } else {
                networkDialog()
            }
        }

        summeryDetails()
        Constant.setAverageUserIdSCM(preferences)
        Constant.setUPSFMCSDK(preferences)


    }

    private fun movedNext() {
        val runnable = Runnable {
            if (preferences.getIsLogin()) {
                if (path.contains("/food") || path.contains("/booking")) {
                    val parts = path_part.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    if (path.contains("newpromo")) {
                        val intent = Intent(this@SplashActivity, ScannerActivity::class.java)
                        intent.putExtra("promo", "NEWPROMO")
                        startActivity(intent)
                        finish()
                    } else if (parts.size == 5) {
                        val intent = Intent(this@SplashActivity, FoodActivity::class.java)
                        intent.putExtra("from", "scan")
                        intent.putExtra("SEAT", parts[4])
                        intent.putExtra("AUDI", parts[3])
                        startActivity(intent)
                        finish()
                    } else if (parts.size > 2) {
                        var type = ""
                        val intent = Intent(this@SplashActivity, SelectBookingsActivity::class.java)
                        intent.putExtra("from", "pscan")
                        intent.putExtra("SEAT", "")
                        intent.putExtra("cid", parts?.get(2))
                        intent.putExtra("AUDI", "")
                        if (!TextUtils.isEmpty(type))
                            intent.putExtra("type", type)
                        if (path.contains("newpromo"))
                            intent.putExtra("promo", "NEWPROMO")

                        startActivity(intent)
                        finish()
                    }
                }else {
                    if (preferences.getCityName() == "") {
                        val intent = Intent(this@SplashActivity, SelectCityActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
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
        newTag= output.ntb
        DONATION = output.donation
        preferences.saveString(Constant.SharedPreference.NT, output.nt)
        preferences.saveString(Constant.SharedPreference.NTBT, output.ntbn)
        preferences.saveString(Constant.SharedPreference.IS_HL, output.hl)
        preferences.saveString(Constant.SharedPreference.IS_LY, output.ly)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(
                Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                Uri.parse("package:${this.packageName}"))
            startActivity(intent)

        }
        //movedNext()
    }

}