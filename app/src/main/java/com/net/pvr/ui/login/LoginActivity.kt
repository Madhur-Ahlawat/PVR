@file:Suppress("DEPRECATION")

package com.net.pvr.ui.login

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.*
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsApi
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityLoginBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.location.enableLocation.EnableLocationActivity
import com.net.pvr.ui.location.selectCity.SelectCityActivity
import com.net.pvr.ui.login.otpVerify.OtpVerifyActivity
import com.net.pvr.ui.login.response.LoginResponse
import com.net.pvr.ui.login.viewModel.LoginViewModel
import com.net.pvr.ui.summery.response.ExtendTimeResponse
import com.net.pvr.utils.*
import com.net.pvr.utils.BroadcastService
import com.net.pvr.utils.Constant.Companion.AVAILABETIME
import com.net.pvr.utils.Constant.Companion.BOOKING_ID
import com.net.pvr.utils.Constant.Companion.CINEMA_ID
import com.net.pvr.utils.Constant.Companion.EXTANDTIME
import com.net.pvr.utils.Constant.Companion.SUCCESS_CODE
import com.net.pvr.utils.Constant.Companion.TRANSACTION_ID
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityLoginBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: LoginViewModel by viewModels()
    private val mobileRequest = 1
    private var from: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        setContentView(binding?.root)

        manageFunction()
    }

    private fun manageFunction() {
        Constant.viewModel = authViewModel
        from = intent.getStringExtra("from").toString()
        if (from == "summery") {
            if (Constant.BOOK_TYPE == "BOOKING")
            timerManage()
        }

        //moved Another Pages
        movedNext()
        extendTime()
        //Auto Show Mobile Number
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent: PendingIntent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(intent.intentSender, mobileRequest, null, 0, 0, 0, Bundle())
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }

    }

    private fun movedNext() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding?.mobileNumber?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.toString() != " ") {
                    customMargin(0)
                    binding?.textView382?.invisible()
                } else {
                    customMargin(1)
                    binding?.textView382?.text = getString(R.string.checkNumber)
                    binding?.textView382?.show()

                }
            }
        })

        binding?.mobileNumber?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val mobile = binding?.mobileNumber?.text.toString()
                Constant().hideKeyboard(this@LoginActivity)
                if (mobile == "") {
                    binding?.textView382?.show()
                    binding?.textView382?.text = getString(R.string.enterMobileNo)
                } else if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                    binding?.textView382?.show()
                    binding?.textView382?.text = getString(R.string.checkNumber)
                } else {
                    // Hit Event
                    try {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                        GoogleAnalytics.hitEvent(this, "login_with_otp", bundle)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    binding?.textView382?.hide()
                    authViewModel.loginMobileUser(mobile, preferences.getCityName(), "INDIA")
                }
                true
            } else false
        }

        binding?.textView11?.setOnClickListener {
            val mobile = binding?.mobileNumber?.text.toString()
            if (mobile == "") {
                binding?.textView382?.show()
                binding?.textView382?.text = getString(R.string.enterMobileNo)
            } else if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                binding?.textView382?.show()
                binding?.textView382?.text = getString(R.string.checkNumber)
            } else {
                binding?.textView382?.hide()
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                    GoogleAnalytics.hitEvent(this, "login_with_otp", bundle)
                }catch (e:Exception){
                    e.printStackTrace()
                }
                authViewModel.loginMobileUser(mobile, preferences.getCityName(), "INDIA")
            }
        }

        //Skip
        binding?.textView8?.setOnClickListener {

            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                GoogleAnalytics.hitEvent(this, "login_skip", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (intent.hasExtra("from")){
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.blank_space,
                    "Your booking is not yet complete.",
                    positiveBtnText = R.string.accept,
                    negativeBtnText = R.string.cancel,
                    positiveClick = {
                        launchActivity(
                            HomeActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                        finish()
                    },
                    negativeClick = {})
                dialog.show()
            }else {

                if (!Constant().locationServicesEnabled(this@LoginActivity)) {
                    val intent = Intent(this@LoginActivity, EnableLocationActivity::class.java)
                    startActivity(intent)
                } else if (preferences.getCityName() == "") {
                    val intent = Intent(this@LoginActivity, SelectCityActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


        //Login
        loginApi()
    }

    private fun customMargin(i: Int) {
        if (i==0){
            binding?.textView382?.setPadding(0,0,0,50)

        }else{
            binding?.textView382?.setPadding(0,0,0,150)

        }
    }

    private fun loginApi() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && SUCCESS_CODE == it.data.code) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.blank_space,
                            it.data.msg,
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                retrieveData(it.data.output)
                            },
                            negativeClick = {})
                        dialog.show()
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.blank_space,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.blank_space,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: LoginResponse.Output?) {
        val intent = Intent(this@LoginActivity, OtpVerifyActivity::class.java)
        intent.putExtra("mobile", binding?.mobileNumber?.text.toString())
        intent.putExtra("from", from)
        intent.putExtra("newUser", output?.nu)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mobileRequest && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            val cred: Credential = data?.getParcelableExtra(Credential.EXTRA_KEY)!!
            try {
                if (!TextUtils.isEmpty(cred.id) && cred.id.length > 4) {
                    if (cred.id.startsWith("+91")) {
                        binding?.mobileNumber?.setText(
                            "" + cred.id.substring(3, cred.id.length)
                        )
                    } else {
                        binding?.mobileNumber?.setText("" + cred.id)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == mobileRequest && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            // *** No phone numbers available ***
            printLog("numberNotFound---->")
        }
    }

    override fun onBackPressed() {
        if (intent.hasExtra("from")){
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.blank_space,
                "Your booking is not yet complete.",
                positiveBtnText = R.string.accept,
                negativeBtnText = R.string.cancel,
                positiveClick = {
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    finish()
                },
                negativeClick = {})
            dialog.show()
        }else {
            if (from == "seat") {
                seatRedirectDialog()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun seatRedirectDialog() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher_foreground,
            R.string.blank_space,
            getString(R.string.logoutBack),
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.no,
            positiveClick = {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            },
            negativeClick = {})
        dialog.show()
    }


    private fun timerManage() {
        startService(Intent(this, BroadcastService::class.java))

    }

    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            updateGUI(intent) // or whatever method used to update your GUI fields
        }
    }

    override fun onResume() {
        super.onResume()
        if (Constant.BOOK_TYPE == "BOOKING")
        registerReceiver(br, IntentFilter(BroadcastService.COUNTDOWN_BR))
    }

    override fun onPause() {
        super.onPause()
        if (Constant.BOOK_TYPE == "BOOKING")
        unregisterReceiver(br)
    }

    override fun onStop() {
        try {
            if (Constant.BOOK_TYPE == "BOOKING")
            unregisterReceiver(br)
        } catch (e: java.lang.Exception) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (Constant.BOOK_TYPE == "BOOKING")
        stopService(Intent(this, BroadcastService::class.java))
        super.onDestroy()
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun updateGUI(intent: Intent) {
        if (intent.extras != null) {
            val millisUntilFinished = intent.getLongExtra("countdown", 0)
            val second = millisUntilFinished / 1000 % 60
            val minutes = millisUntilFinished / (1000 * 60) % 60
            val display = java.lang.String.format("%02d:%02d", minutes, second)

            binding?.include47?.textView394?.text = display + " " + getString(R.string.minRemaining)

            if (millisUntilFinished.toInt() <= AVAILABETIME) {
                binding?.constraintLayout168?.show()
            } else {
                binding?.constraintLayout168?.hide()
            }

            binding?.include47?.textView395?.setOnClickListener {
                binding?.constraintLayout168?.hide()
                unregisterReceiver(br)
                authViewModel.extendTime(TRANSACTION_ID, BOOKING_ID, CINEMA_ID)

            }

        }
    }

    private fun extendTime() {
        authViewModel.extendTimeLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && SUCCESS_CODE == it.data.code) {
                        retrieveExtendData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveExtendData(output: ExtendTimeResponse.Output) {
        AVAILABETIME = Constant().convertTime(output.et.toInt()) -  Constant().convertTime(output.at.toInt())
        EXTANDTIME = Constant().convertTime(output.at.toInt())
        PCTimer.startTimer(
            Constant.EXTANDTIME,
            Constant.AVAILABETIME,
            Constant.CINEMA_ID,
            Constant.TRANSACTION_ID,
            Constant.BOOK_TYPE,
            null,
            false,
            authViewModel
        )
        timerManage()
    }

}