package com.net.pvr.ui.payment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator.ProgressTextAdapter
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPaymentStatusBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.GiftCardActivity
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.payment.adapter.*
import com.net.pvr.ui.payment.response.*
import com.net.pvr.ui.payment.viewModel.PaymentViewModel
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.BOOKING_ID
import com.net.pvr.utils.Constant.Companion.BOOK_TYPE
import com.net.pvr.utils.Constant.Companion.TRANSACTION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class PaymentStatusActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPaymentStatusBinding? = null
    private val authViewModel: PaymentViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var paidAmount = ""
    private var title = ""
    private var upiCount = 0
    private var progressCount = 0
    private var upiLoader = false

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentStatusBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        paidAmount = intent.getStringExtra("paidAmount").toString()
        title = intent.getStringExtra("title").toString()
        binding?.include27?.textView108?.text = title
        binding?.textView287?.text = "₹$paidAmount"

        manageFunction()
    }

    @SuppressLint("SetTextI18n")
    private fun manageFunction() {
        binding?.circularProgress?.interpolator = LinearInterpolator()
        binding?.circularProgress?.maxProgress = 60.0
        binding?.circularProgress?.setProgressTextAdapter(progressTextAdapter)
        if (title == "UPI") {
            if (BOOK_TYPE == "LOYALTYUNLIMITED") {
                toast("LOYALTYUNLIMITED")
            } else {
                authViewModel.upiStatus(
                    BOOKING_ID, BOOK_TYPE
                )
            }

        } else if (title == "PhonePe") {
            authViewModel.phonepeStatus(
                preferences.getUserId(), BOOKING_ID, BOOK_TYPE, TRANSACTION_ID
            )
        }
        movedNext()
        phonePeStatus()
        upiStatus()

        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("onTick-->${millisUntilFinished / 1000}")
                binding?.circularProgress?.setCurrentProgress(millisUntilFinished.toDouble() / 1000.0)
            }

            override fun onFinish() {

            }
        }
        timer.start()
    }

    private val progressTextAdapter =
        ProgressTextAdapter { time ->
            var time = time
            val hours = (time / 3600).toInt()
            time %= 3600.0
            val minutes = (time / 60).toInt()
            val seconds = (time % 60).toInt()
            val sb = java.lang.StringBuilder()
            if (hours < 10) {
                sb.append(0)
            }
            sb.append(hours).append(":")
            if (minutes < 10) {
                sb.append(0)
            }
            sb.append(minutes).append(":")
            if (seconds < 10) {
                sb.append(0)
            }
            sb.append(seconds)
            sb.toString()
        }

    private fun movedNext() {
        binding?.include27?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    override fun onBackPressed() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            "Do you want to end the session?",
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.cancel,
            positiveClick = {
                when (BOOK_TYPE) {
                    "GIFTCARD" -> {
                        launchActivity(
                            GiftCardActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    }
                    "BOOKING", "FOOD" -> {
                        launchActivity(
                            HomeActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        )
                    }
                    else -> {
                        launchPrivilegeActivity(
                            HomeActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,
                            "",
                            "",
                            "",
                            "P"
                        )
                    }
                }
            },
            negativeClick = {})
        dialog.show()
    }


    //PhonePe STATUS
    private fun phonePeStatus() {
        authViewModel.phonepeStatusLiveDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        if (it.data.output.p != "false") {
                            Constant().printTicket(this@PaymentStatusActivity)
                        } else {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                it.data.msg.toString(),
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        }

                    } else {
                        loader?.dismiss()
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
                    if (!upiLoader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
                }
            }
        }
    }

    //UPI STATUS
    private fun upiStatus() {
        authViewModel.upiStatusResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveStatusUpi(it.data.output)
                    } else {
                        loader?.dismiss()
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
                    if (!upiLoader) {
                        loader = LoaderDialog(R.string.pleaseWait)
                        loader?.show(this.supportFragmentManager, null)
                    }
                }
            }
        }

    }

    private fun retrieveStatusUpi(output: UPIStatusResponse.Output) {
        upiLoader = true
        if (output.p.equals("PAID", ignoreCase = true)) {
            loader?.dismiss()
            Constant().printTicket(this@PaymentStatusActivity)
        } else if (output.p.equals("PENDING", ignoreCase = true)) {
            if (upiCount <= 6) {
                val handler = Handler()
                upiCount += 1
                handler.postDelayed({ // close your dialog
                    authViewModel.upiStatus(
                        BOOKING_ID, BOOK_TYPE
                    )
                }, 10000)
            } else {
                loader?.dismiss()
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "If you want to try again then press retry otherwise press cancel.",
                    positiveBtnText = R.string.retry,
                    negativeBtnText = R.string.cancel,
                    positiveClick = {
                        finish()
                        startActivity(intent)
                    },
                    negativeClick = {
                        onBackPressedDispatcher.onBackPressed()
                    })
                dialog.show()
            }
        } else {
            loader?.dismiss()
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "Transaction Failed!",
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                    onBackPressedDispatcher.onBackPressed()
                },
                negativeClick = {

                })
            dialog.show()
        }
    }


}