package com.net.pvr1.ui.bookingSession

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityBookingBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.viewModel.BookingViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityBookingBinding? = null
    private val authViewModel: BookingViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        authViewModel.bookingTicket("Delhi-NCR", "NHO00015379", "0", "0", "NA", "no", "no", "")

        bookingTicketDataLoad()
    }

    private fun bookingTicketDataLoad() {
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

    private fun retrieveData(output: BookingResponse.Output) {
        printLog("${output}")
    }

}