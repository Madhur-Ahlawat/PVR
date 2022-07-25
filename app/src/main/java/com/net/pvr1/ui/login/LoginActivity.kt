package com.net.pvr1.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityLoginBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.login.viewModel.LoginViewModel
import com.net.pvr1.ui.otpVerify.OtpVerifyActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.Constant.Companion.SUCCESS_CODE
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityLoginBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        movedNext()
    }

    private fun movedNext() {
        binding?.textView11?.setOnClickListener {
            val mobile = binding?.mobileNumber?.text.toString()
            if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                Toast.makeText(this, "Mobile Number Should We 10 Digit", Toast.LENGTH_SHORT)
                    .show()
            } else {
                authViewModel.loginMobileUser(mobile, "Ncr", "Delhi")
            }
        }
        loginApi()
    }

    private fun loginApi() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                    }else{
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
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
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

    private fun retrieveData(output: LoginResponse.Output?) {
        val intent = Intent(this@LoginActivity, OtpVerifyActivity::class.java)
        intent.putExtra("mobile",binding?.mobileNumber?.text.toString())
        startActivity(intent)
        finish()
    }

}