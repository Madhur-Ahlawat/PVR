package com.net.pvr1.ui.otpVerify

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.databinding.ActivityOtpVerifyBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.onBoarding.LandingActivity
import com.net.pvr1.ui.otpVerify.viewModel.OtpVerifyViewModel
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import java.security.MessageDigest

@AndroidEntryPoint
class OtpVerifyActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityOtpVerifyBinding? = null
    private val authViewModel: OtpVerifyViewModel by viewModels()
    private var mobile:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        mobile= intent.getStringExtra("mobile").toString()

        movedNext()
    }

    private fun movedNext() {
        binding?.textView15?.setOnClickListener {
//            val mobile = binding?.textView15?.text.toString()
//            if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
//                Toast.makeText(this, "Mobile Number Should We 10 Digit", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
                authViewModel.otpVerify(mobile,
                    getHash(
                        "$mobile|111111"
                    )!!
                )
//            }
        }
        loginApi()
    }

    private fun loginApi() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    retrieveData(it.data?.output)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    println("Loading--->")
                }
            }
        }
    }

    private fun retrieveData(output: LoginResponse.Output?) {
        val intent = Intent(this@OtpVerifyActivity, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Throws(Exception::class)
    fun getHash(text: String): String? {
        val mdText = MessageDigest.getInstance("SHA-512")
        val byteData = mdText.digest(text.toByteArray())
        val sb = StringBuffer()
        for (i in byteData.indices) {
            sb.append(((byteData[i] and 0xff) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }

}