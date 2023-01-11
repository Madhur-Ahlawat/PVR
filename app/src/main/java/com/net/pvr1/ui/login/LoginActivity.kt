@file:Suppress("DEPRECATION")

package com.net.pvr1.ui.login

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsApi
import com.google.android.gms.auth.api.credentials.HintRequest
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityLoginBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.location.enableLocation.EnableLocationActivity
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.ui.login.otpVerify.OtpVerifyActivity
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.login.viewModel.LoginViewModel
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.SUCCESS_CODE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        //Auto Show Mobile Number
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent: PendingIntent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(
                intent.intentSender, mobileRequest, null, 0, 0, 0, Bundle()
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }

        //moved Another Pages
        movedNext()
    }

    private fun movedNext() {
        binding?.mobileNumber?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.toString()!=" "){
                    binding?.textView382?.hide()
                }else{
                    binding?.textView382?.text=getString(R.string.checkNumber)
                    binding?.textView382?.show()
                }
            }
        })
        binding?.mobileNumber?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val mobile = binding?.mobileNumber?.text.toString()
                if (mobile == "") {
                    binding?.textView382?.show()
                    binding?.textView382?.text=getString(R.string.enterMobileNo)
                } else if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                    binding?.textView382?.show()
                    binding?.textView382?.text=getString(R.string.checkNumber)
                } else {
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
                binding?.textView382?.text=getString(R.string.enterMobileNo)
            } else if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                binding?.textView382?.show()
                binding?.textView382?.text=getString(R.string.checkNumber)
            } else {
                binding?.textView382?.hide()
                authViewModel.loginMobileUser(mobile, preferences.getCityName(), "INDIA")
            }
        }

        //Skip
        binding?.textView8?.setOnClickListener {
            if (!Constant().locationServicesEnabled(this@LoginActivity)) {
                val intent = Intent(this@LoginActivity, EnableLocationActivity::class.java)
                startActivity(intent)
//                finish()
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

        // OutSide Click
        binding?.loginClick?.setOnClickListener {
            binding?.textInputLayout?.isSelected = false
            binding?.mobileNumber?.isFocusableInTouchMode = false
            binding?.textInputLayout?.isFocusableInTouchMode = false
            Constant().hideKeyboard(this)
        }

        //Login
        loginApi()
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
        intent.putExtra("newUser", output?.nu)
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    @Override
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
}