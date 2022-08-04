package com.net.pvr1.ui.selectCity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()

        selectCity()
        selectCityViewModel.selectCity("27.2046","77.4977","0","no","no")

    }

    private fun selectCity() {
        selectCityViewModel.cityResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                        retrieveOtherCityData(it.data.output)
                        retrieveSearchData(it.data.output)
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

    private fun retrieveSearchData(output: SelectCityResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = OtherCityAdapter(output.ot, this)
        binding?.recyclerViewSearchCity?.layoutManager = gridLayout2
        binding?.recyclerViewSearchCity?.adapter = otherCityAdapter
    }

    private fun retrieveOtherCityData(output: SelectCityResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = OtherCityAdapter(output.ot, this)
        binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
        binding?.recyclerViewOtherCity?.adapter = otherCityAdapter
    }

    private fun retrieveData(output: SelectCityResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val selectCityAdapter = SelectCityAdapter(output.pc, this)
        binding?.recyclerCity?.layoutManager = gridLayout2
        binding?.recyclerCity?.adapter = selectCityAdapter
    }


}