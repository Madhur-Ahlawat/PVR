package com.net.pvr.ui.cinemaSession.cinemaDetails

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivityCinemaDetailsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.cinemaSession.cinemaDetails.response.CinemaDetailsResponse
import com.net.pvr.ui.cinemaSession.cinemaDetails.viewModel.CinemaDetailsViewModel
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CinemaDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCinemaDetailsBinding? = null
    private val authViewModel: CinemaDetailsViewModel by viewModels()
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)


        manageFunctions()
    }

    private fun manageFunctions() {
        authViewModel.cinemaDetails(
            intent.getStringExtra("cid").toString(),
            preferences.getLatitudeData(),
            preferences.getLongitudeData()
        )

        //title
        binding?.include44?.textView108?.text = getString(R.string.cinemaDetails)

        //back press
        binding?.include44?.imageView58?.setOnClickListener {
            finish()
        }

        cinemaDetails()
    }

    private fun cinemaDetails() {
        authViewModel.authModel.observe(this) {
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: CinemaDetailsResponse.Output) {
        //Cinema Name
        binding?.cinemaName?.text = output.cn

        // address
        binding?.multipleCinema?.text = output.add
        //distance
        binding?.tvDistance?.text = output.dis

        //open Map
        binding?.llLocation?.setOnClickListener {
            Constant().openMap(this, output.lat, output.lng)
        }
    }

}