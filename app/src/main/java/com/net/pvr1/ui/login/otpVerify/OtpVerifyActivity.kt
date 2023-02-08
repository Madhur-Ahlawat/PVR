package com.net.pvr1.ui.login.otpVerify

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOtpVerifyBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.GiftCardSummaryActivity
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.home.fragment.privilege.EnrollInPassportActivity
import com.net.pvr1.ui.home.fragment.privilege.EnrollInPrivilegeActivity
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.ui.location.enableLocation.EnableLocationActivity
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.login.otpVerify.viewModel.OtpVerifyViewModel
import com.net.pvr1.ui.summery.SummeryActivity
import com.net.pvr1.ui.summery.response.ExtendTimeResponse
import com.net.pvr1.utils.*
import com.net.pvr1.utils.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class OtpVerifyActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOtpVerifyBinding? = null
    private val authViewModel: OtpVerifyViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var mobile: String = ""
    private var newUser: String = ""
    private var signUpClick = 0

    //Otp Read
    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    private  var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        Constant.viewModel = authViewModel

        mobile = intent.getStringExtra("mobile").toString()
        newUser = intent.getStringExtra("newUser").toString()
        if (intent.hasExtra("from"))
        from = intent.getStringExtra("from").toString()
        if (from == "summery") {
            timerManage()
        }

        if (newUser == "false") {
            binding?.textView15?.text = getString(R.string.continue_txt)
        } else {
            binding?.textView15?.text = getString(R.string.submit)
        }

//        Read otp
        startSmsUserConsent()

        //moved another Page
        movedNext()

        //verify Otp
        otpVerify()

        //newUser
        registerUser()
        extendTime()
    }

    //For Auto Read otp
    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {

        }.addOnFailureListener {


        }
    }

    //Moved Next
    private fun movedNext() {
        //Resend Otp
        binding?.textView14?.setOnClickListener {
            binding?.otpEditText?.clearText(true)
            authViewModel.loginMobileUser(mobile, preferences.getCityName(), "INDIA")
        }

        //Submit
        binding?.textView15?.setOnClickListener {
            val otp = binding?.otpEditText?.getStringFromFields()
            if (binding?.otpEditText?.getStringFromFields()?.contains("null") == true) {

                binding?.textView15?.isClickable = true
                binding?.textView15?.isFocusable = true
                binding?.textView15?.setBackgroundResource(R.drawable.yellow_book_curve)


                binding?.textView383?.show()
                binding?.textView383?.text=getString(R.string.enterOtp)

            } else {
                if (newUser == "false") {
                    authViewModel.otpVerify(
                        mobile, getHash(
                            "$mobile|$otp"
                        )
                    )
                } else {
                    binding?.textView15?.setOnClickListener {
                        binding?.textView383?.hide()
                        val name = binding?.name?.text.toString()
                        val email = binding?.email?.text.toString()
                        if (signUpClick==0){
                            binding?.constraintLayout38?.show()
                            binding?.textView15?.text= getString(R.string.continue_txt)
                            signUpClick+=1
                        }else{
                            if (name==""){
                                binding?.textView384?.show()
                                binding?.textView384?.hide()
                                binding?.textView384?.text=getString(R.string.enterName)
                            }else if (!InputTextValidator.checkFullName(binding?.name!!)) {
                                binding?.textView384?.hide()
                                binding?.textView384?.show()
                                binding?.textView384?.text=getString(R.string.lastName)
                            } else if (email=="") {
                                binding?.textView384?.hide()
                                binding?.textView385?.show()
                                binding?.textView385?.text=getString(R.string.enterEmail)
                            } else if (!InputTextValidator.validateEmail(binding?.email!!)) {
                                binding?.textView384?.hide()
                                binding?.textView385?.show()
                                binding?.textView385?.text=getString(R.string.wrongEmail)
                            } else {
                                binding?.textView384?.hide()
                                binding?.textView385?.hide()
                                authViewModel.resister(
                                    getHash(
                                        "$mobile|$otp|$email"
                                    ), name, email, mobile, otp.toString(), "INDIA", false
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private fun otpVerify() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
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
                        it.data?.msg.toString(),
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

    private fun registerUser() {
        authViewModel.userResponseResLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveResisterData(it.data.output)
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
                        it.data?.msg.toString(),
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

    private fun retrieveResisterData(output: ResisterResponse.Output) {
        Constant.setAverageUserIdSCM(preferences)
        Constant.setUPSFMCSDK(preferences)

        preferences.saveIsLogin(true)
        output.id.let { preferences.saveUserId(it) }
        output.un.let { preferences.saveUserName(it) }
        output.ph.let { preferences.saveMobileNumber(it) }
        output.em.let { preferences.saveEmail(it) }
        output.token.let { preferences.saveToken(it) }
        if (output.dob!=null){
            output.dob.let { preferences.saveDob(it) }
        }
        authViewModel.privilegeHome(preferences.geMobileNumber(), preferences.getCityName())
        privilegeDataLoad()
    }


    private fun retrieveData(output: ResisterResponse.Output?) {
        Constant.setAverageUserIdSCM(preferences)
        Constant.setUPSFMCSDK(preferences)

        preferences.saveIsLogin(true)
        output?.id?.let { preferences.saveUserId(it) }
        output?.un?.let { preferences.saveUserName(it) }
        output?.ph?.let { preferences.saveMobileNumber(it) }
        output?.token?.let { preferences.saveToken(it) }
        output?.dob?.let { preferences.saveDob(it) }
        output?.ph?.let { preferences.saveString(Constant.SharedPreference.USER_NUMBER,it) }
        output?.em?.let { preferences.saveString(Constant.SharedPreference.USER_EMAIL,it)  }
        output?.gd?.let { preferences.saveString(Constant.SharedPreference.USER_GENDER,it)  }

        authViewModel.privilegeHome(preferences.geMobileNumber(), preferences.getCityName())
        privilegeDataLoad()
    }

    private fun privilegeDataLoad() {
        authViewModel.privilegeHomeResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        Constant.PRIVILEGEPOINT = it.data.output.pt
                        Constant.PRIVILEGEVOUCHER = it.data.output.vou
                        privilegeRetrieveData(it.data.output)

                    } else {
                        if (it.data?.output != null) it.data.output.let { it1 ->
                            privilegeRetrieveData(
                                it1
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
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

    private fun privilegeRetrieveData(output: PrivilegeHomeResponse.Output) {
        try {
            Constant.PrivilegeHomeResponseConst = output
            preferences.saveString("FAQ", output.faq)
            preferences.saveString(Constant.SharedPreference.pcities, output.pcities)
            preferences.saveString("KOTAK_URL", output.pkotakurl)
            val jsonObject1 = JSONObject()
            jsonObject1.put("points", output.pt)
            jsonObject1.put("voucher", output.vou)
            preferences.saveString(Constant.SharedPreference.LOYALITY_POINT, jsonObject1.toString())
            preferences.saveString(
                Constant.SharedPreference.LOYALITY_CARD, output.passportbuy.toString()
            )
            preferences.saveString(Constant.SharedPreference.SUBS_OPEN, output.passport.toString())
            preferences.saveString(Constant.SharedPreference.LOYALITY_STATUS, output.ls)
            preferences.saveString(Constant.SharedPreference.SUBSCRIPTION_STATUS, output.ulm)
            checkMoved()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun checkMoved() {
        if (!Constant().locationServicesEnabled(this@OtpVerifyActivity)) {
            val intent = Intent(this@OtpVerifyActivity, EnableLocationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else if (preferences.getCityName() == "") {
            val intent = Intent(this@OtpVerifyActivity, SelectCityActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            if (from=="summery"){
                val intent = Intent(this@OtpVerifyActivity, SummeryActivity::class.java)
                intent.putExtra("from",from)
                startActivity(intent)
                finish()
            }else if (from == "GC"){
                val intent = Intent(this@OtpVerifyActivity, GiftCardSummaryActivity::class.java)
                startActivity(intent)
                finish()
            }else if (from == "PP"){
                if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == Constant.SharedPreference.ACTIVE){
                    val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java)
                    intent.putExtra("from","P")
                    startActivity(intent)
                    finish()
                }else {
                    val intent = Intent(this@OtpVerifyActivity, EnrollInPassportActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }else if (from == "P"){
                val ls = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)
                val isHl: String = preferences.getString(Constant.SharedPreference.IS_HL)
                val isLy: String = preferences.getString(Constant.SharedPreference.IS_LY)
                if (isLy.equals("true", ignoreCase = true)) {
                    if (ls != null && !ls.equals("", ignoreCase = true)) {
                        if (isHl.equals("true", ignoreCase = true)) {
                            val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java)
                            intent.putExtra("from","P")
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@OtpVerifyActivity, EnrollInPrivilegeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val intent = Intent(this@OtpVerifyActivity, EnrollInPrivilegeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }else{
                    val intent = Intent(this@OtpVerifyActivity, EnrollInPrivilegeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }else{
                val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    @Throws(Exception::class)
    fun getHash(text: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(text.toByteArray())
        val sb = StringBuilder()
        for (i in digest.indices) {
            sb.append(((digest[i] and 0xff) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (result.resultCode == RESULT_OK&& data != null) {
                    val message: String = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)!!
                    getOtpFromMessage(message)
                }
            }
        }

    private fun getOtpFromMessage(message: String) {
        // This will match any 6 digit number in the message
        val pattern: Pattern = Pattern.compile("(|^)\\d{6}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            binding?.otpEditText?.setText(matcher.group(0)!!)
            val otp = binding?.otpEditText?.getStringFromFields()
            authViewModel.otpVerify(
                mobile, getHash(
                    "$mobile|$otp"
                )
            )
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver?.smsBroadcastReceiverListener = object : SmsBroadcastReceiverListener {
            override fun onSuccess(intent: Intent?) {
                resultLauncher.launch(intent)
            }

            override fun onFailure() {

            }
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    @Override
    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
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
        registerReceiver(br, IntentFilter(BroadcastService.COUNTDOWN_BR))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(br)
    }

    @Override
    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)

        try {
            unregisterReceiver(br)
        } catch (e: java.lang.Exception) {
            // Receiver was probably already stopped in onPause()
        }
    }

    override fun onDestroy() {
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

            if (millisUntilFinished.toInt() <= Constant.AVAILABETIME) {
                binding?.constraintLayout168?.show()
            } else {
                binding?.constraintLayout168?.hide()
            }

            binding?.include47?.textView395?.setOnClickListener {
                binding?.constraintLayout168?.hide()
                unregisterReceiver(br)
                authViewModel.extendTime(
                    Constant.TRANSACTION_ID,
                    Constant.BOOKING_ID,
                    Constant.CINEMA_ID
                )

            }

        }
    }

    private fun extendTime() {
        authViewModel.extendTimeLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
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
        //extandTime
        Constant.EXTANDTIME = Constant().convertTime(output.et)

        //AVAIL TIME
        Constant.AVAILABETIME = Constant().convertTime(output.at)

        timerManage()
    }

}