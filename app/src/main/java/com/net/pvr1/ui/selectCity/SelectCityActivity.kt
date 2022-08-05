package com.net.pvr1.ui.selectCity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(), SearchCityAdapter.RecycleViewItemClickListener,
    OtherCityAdapter.RecycleViewItemClickListenerCity, SelectCityAdapter.RecycleViewItemClickListenerSelectCity {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()

    private var otherCityList: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()

    private var otherCityAdapter: SearchCityAdapter? = null

    private var dataList: java.util.ArrayList<Any> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()

        selectCity()
        selectCityViewModel.selectCity("28.679079", "77.069710", "0", "no", "no")

//        if (preferences.getString(Constant.CITY_NAME) != null){
//            binding?.consSelectedLocation?.show()
//            binding?.txtSelectedCity?.text = preferences.getString(Constant.CITY_NAME)
//        }else{
//            binding?.consSelectedLocation?.hide()
//        }

        binding?.searchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                var cs = cs
                if (cs.toString().isEmpty()) {
//                    otherCityList.clear()
                    dataList.clear()
                    binding?.recyclerViewSearchCity?.hide()
                    binding?.consSelectCity?.show()
                    binding?.consSelectedLocation?.show()
                    binding?.searchCity?.isFocusable = true
                    binding?.searchCity?.isClickable = true
                } else {
                    binding?.searchCity?.isFocusable = true
                    binding?.searchCity?.isClickable = true
                    binding?.recyclerViewSearchCity?.show()
                    binding?.consSelectCity?.hide()
                    binding?.consSelectedLocation?.hide()
                    val s = cs.toString().trim { it <= ' ' }
                    cs = s
                    this@SelectCityActivity.otherCityAdapter?.filter?.filter(cs)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

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
        otherCityAdapter = SearchCityAdapter(output.ot, dataList, dataList, this, this)
        binding?.recyclerViewSearchCity?.layoutManager = gridLayout2
        binding?.recyclerViewSearchCity?.adapter = otherCityAdapter
    }
    override fun onItemClickCitySearch(city: ArrayList<SelectCityResponse.Output.Ot>, position: Int,) {
        binding?.searchCity?.setText(" ")
        binding?.searchCity?.isFocusable = false
        binding?.searchCity?.isClickable = false
        binding?.txtSelectedCity?.text = city[position].name
        preferences.putString(Constant.CITY_NAME, city[position].name)
        binding?.recyclerViewSearchCity?.hide()
        binding?.consSelectCity?.show()
        binding?.consSelectedLocation?.show()

    }


        private fun retrieveOtherCityData(output: SelectCityResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = OtherCityAdapter(output.ot, this, this)
        binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
        binding?.recyclerViewOtherCity?.adapter = otherCityAdapter
    }

    private fun retrieveData(output: SelectCityResponse.Output) {
        dataList.clear()
        otherCityList = output.ot
        dataList.addAll(output.ot)
        dataList.addAll(output.pc)
        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val selectCityAdapter = SelectCityAdapter(output.pc, this, this)
        binding?.recyclerCity?.layoutManager = gridLayout2
        binding?.recyclerCity?.adapter = selectCityAdapter
    }

    override fun onItemClickCityOtherCity(city: ArrayList<SelectCityResponse.Output.Ot>, position: Int,) {
        binding?.txtSelectedCity?.text = city[position].name
//        binding?.searchCity?.setText(" ")
//        binding?.searchCity?.isFocusable = false
//        binding?.recyclerViewSearchCity?.hide()
//        binding?.consSelectCity?.show()
//        binding?.consSelectedLocation?.show()
        preferences.putString(Constant.CITY_NAME, city[position].name)
    }

    override fun onItemClickCityImgCity(city: ArrayList<SelectCityResponse.Output.Pc>, position: Int, ) {
        binding?.txtSelectedCity?.text = city[position].name
//        binding?.searchCity?.setText(" ")
//        binding?.searchCity?.isFocusable = false
//        binding?.recyclerViewSearchCity?.hide()
//        binding?.consSelectCity?.show()
//        binding?.consSelectedLocation?.show()
        preferences.putString(Constant.CITY_NAME, city[position].name)

    }


}