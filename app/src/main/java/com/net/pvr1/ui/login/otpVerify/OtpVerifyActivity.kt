package com.net.pvr1.ui.login.otpVerify

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityOtpVerifyBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.login.otpVerify.response.ResisterResponse
import com.net.pvr1.ui.login.otpVerify.viewModel.OtpVerifyViewModel
import com.net.pvr1.utils.*
import com.net.pvr1.utils.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import java.security.MessageDigest
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class OtpVerifyActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityOtpVerifyBinding? = null
    private val authViewModel: OtpVerifyViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var mobile: String = ""
    private var newUser: String = ""

    //Otp Read
    private val User_Content = 200
    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        mobile = intent.getStringExtra("mobile").toString()
        newUser = intent.getStringExtra("newUser").toString()

        if (newUser == "false") {
            binding?.textView15?.text = getString(R.string.continue_txt)
        } else {
            binding?.textView15?.text = getString(R.string.submit)
        }
        startSmsUserConsent()
        movedNext()
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
        binding?.textView15?.setOnClickListener {
            val otp = binding?.otpEditText?.getStringFromFields()
            if (binding?.otpEditText?.getStringFromFields()?.contains("null") == true) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterOtp),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                if (newUser == "false") {
                    authViewModel.otpVerify(
                        mobile,
                        getHash(
                            "$mobile|$otp"
                        )
                    )
                } else {
                    binding?.textView15?.setOnClickListener {
                        val name = binding?.name?.text.toString()
                        val email = binding?.email?.text.toString()
                        binding?.constraintLayout38?.show()

                        if (name == "") {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                getString(R.string.enterName),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        } else if (email == "") {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                getString(R.string.enterEmail),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        } else {
                            authViewModel.resister(
                                getHash(
                                    "$mobile|$otp|$email"
                                ),
                                name,
                                email, mobile, otp.toString(), "INDIA", false
                            )
                        }
                    }

                }

            }
        }
        otpVerify()
        registerUser()

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
                            positiveClick = {
                            },
                            negativeClick = {
                            })
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
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
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
                            positiveClick = {
                            },
                            negativeClick = {
                            })
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
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveResisterData(output: ResisterResponse.Output) {
        preferences.saveIsLogin(true)
        output.id.let { preferences.saveUserId(it) }
        output.un.let { preferences.saveUserName(it) }
        output.ph.let { preferences.saveMobileNumber(it) }
        output.token.let { preferences.saveToken(it) }
        output.dob.let { preferences.saveDob(it) }


        val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun retrieveData(output: ResisterResponse.Output?) {
        preferences.saveIsLogin(true)
        output?.id?.let { preferences.saveUserId(it) }
        output?.un?.let { preferences.saveUserName(it) }
        output?.ph?.let { preferences.saveMobileNumber(it) }
        output?.token?.let { preferences.saveToken(it) }
        output?.dob?.let { preferences.saveDob(it) }

        val intent = Intent(this@OtpVerifyActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
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
//        println(sb)
//        val mdText = MessageDigest.getInstance("SHA-512")
//        val byteData = mdText.digest(text.toByteArray())
//        val sb = StringBuffer()
//        for (i in byteData.indices) {
//            sb.append(((byteData[i] and 0xff) + 0x100).toString(16).substring(1))
//        }
//        return sb.toString()
    }

    @Deprecated("Deprecated in Java")
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == User_Content) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
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
                mobile,
                getHash(
                    "$mobile|$otp"
                )
            )
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver?.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {

                    startActivityForResult(intent, User_Content)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    @Override
    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    @Override
    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }
}