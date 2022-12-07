@file:Suppress("DEPRECATION")

package com.net.pvr1.ui.login

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsApi
import com.google.android.gms.auth.api.credentials.HintRequest
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityLoginBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.login.otpVerify.OtpVerifyActivity
import com.net.pvr1.ui.login.response.LoginResponse
import com.net.pvr1.ui.login.viewModel.LoginViewModel
import com.net.pvr1.ui.selectCity.SelectCityActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.Constant.Companion.SUCCESS_CODE
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityLoginBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: LoginViewModel by viewModels()
    private val CREDENTIAL_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        movedNext()

        //Auto Show Mobile Number
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()


        val intent: PendingIntent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(
                intent.intentSender,
                CREDENTIAL_PICKER_REQUEST,
                null,
                0,
                0,
                0,
                Bundle()
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun movedNext() {

        binding?.mobileNumber?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //do here your stuff f
                val mobile = binding?.mobileNumber?.text.toString()
                if (mobile == "") {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(R.string.enterMobileNo),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                } else if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(R.string.checkNumber),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                } else {
                    authViewModel.loginMobileUser(mobile, preferences.getCityName(), "INDIA")
                }
                true
            } else false
        }

        binding?.textView11?.setOnClickListener {
            //do here your stuff f
            val mobile = binding?.mobileNumber?.text.toString()
            if (mobile == "") {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.enterMobileNo),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else if (!TextUtils.isEmpty(mobile) && mobile.length != 10) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    getString(R.string.checkNumber),
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                authViewModel.loginMobileUser(mobile, "", "INDIA")
            }
        }
        //Skip
        binding?.textView8?.setOnClickListener {
            if (preferences.getCityName() == "") {
                val intent = Intent(this@LoginActivity, SelectCityActivity::class.java)
                startActivity(intent)
                finish()
            }else{
//                val intent = Intent(this@SplashActivity, FoodActivity::class.java)
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
                            R.string.app_name,
                            it.data.msg,
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                retrieveData(it.data.output)
                            },
                            negativeClick = {
                            })
                        dialog.show()
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
        intent.putExtra("mobile", binding?.mobileNumber?.text.toString())
        intent.putExtra("newUser", output?.nu)
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
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
            }
        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            // *** No phone numbers available ***
//            val dialog = OptionDialog(this,
//                R.mipmap.ic_launcher,
//                R.string.app_name,
//                getString(R.string.mobileNotAvailable),
//                positiveBtnText = R.string.ok,
//                negativeBtnText = R.string.no,
//                positiveClick = {
//                },
//                negativeClick = {
//                })
//            dialog.show()
        }
    }
}