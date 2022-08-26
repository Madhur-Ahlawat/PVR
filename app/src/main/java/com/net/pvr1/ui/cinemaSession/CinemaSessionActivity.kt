package com.net.pvr1.ui.cinemaSession

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCinemaSessionBinding
import com.net.pvr1.databinding.ActivityEnableLocationBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.enableLocation.viewModel.EnableLocationViewModel
import com.net.pvr1.ui.search.searchCinema.viewModel.CinemaSearchViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CinemaSessionActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivityCinemaSessionBinding? = null
    private val authViewModel: CinemaSearchViewModel by viewModels()
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSessionBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        authViewModel.cinemaSession(
            "Delhi-NCR",
            "192",
            "0",
            "0",
            "0",
            "NA",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "ALL",
            "no",
            "ALL",
            "ALL"
        )
        cinemaSessionDataLoad()
    }

    private fun cinemaSessionDataLoad() {
        authViewModel.cinemaSessionLiveData.observe(this) {
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

    private fun retrieveData(output: CinemaSessionResponse.Output) {

    }

}