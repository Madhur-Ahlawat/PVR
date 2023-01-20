package com.net.pvr1.ui.location.selectCity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.databinding.CitySelectDialogBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.location.selectCity.adapter.OtherCityAdapter
import com.net.pvr1.ui.location.selectCity.adapter.PopUpCityAdapter
import com.net.pvr1.ui.location.selectCity.adapter.SearchCityAdapter
import com.net.pvr1.ui.location.selectCity.adapter.SelectCityAdapter
import com.net.pvr1.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.location.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.ui.scanner.bookings.SelectBookingsActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(), SearchCityAdapter.RecycleViewItemClickListener,
    OtherCityAdapter.RecycleViewItemClickListenerCity,
    SelectCityAdapter.RecycleViewItemClickListenerSelectCity,
    PopUpCityAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()
    private var filterCityList: ArrayList<SelectCityResponse.Output.Ot>? = null
    private var searchCityAdapter: SearchCityAdapter? = null
    private var list: ArrayList<String> = arrayListOf()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var enableLocation = 0

    private var cityName: String = ""
    private var cityNameMAin: String = ""
    private var from: String = ""
    private var cid: String = ""

    private var dialog: BottomSheetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        selectCityViewModel.selectCity(
            preferences.getLatitudeData(),
            preferences.getLongitudeData(),
            preferences.getUserId(),
            "no",
            "no"
        )
//Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        cityName = preferences.getCityName()
        // Location City Name
        if (preferences.getCityName().isEmpty()){
            binding?.consSelectedLocation?.show()
        }else{
            binding?.txtSelectedCity?.text = preferences.getCityName()
            binding?.consSelectedLocation?.show()
        }
        //Get Intent  Qr Case
        cid = intent.getStringExtra("cid").toString()
        from = intent.getStringExtra("from").toString()
        selectCity()
        movedNext()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun movedNext() {
        // get Location
        binding?.imageView39?.setOnClickListener {
            if (Constant().locationServicesEnabled(this) && Constant.latitude != 0.0 && Constant.longitude != 0.0) {
                preferences.saveLatitudeData(Constant.latitude.toString())
                preferences.saveLatitudeData(Constant.longitude.toString())

                if (from == "qr") {
                    val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                    intent.putExtra("from", "qr")
                    intent.putExtra("cid", cid)
                    startActivity(intent)
                } else {
                    enableLocation = 1
                    selectCityViewModel.selectCity(
                        Constant.latitude.toString(),
                        Constant.longitude.toString(),
                        preferences.getUserId(),
                        "no",
                        "no"
                    )
                }
            } else {
                Constant().enableLocation(this)
            }
        }
        //On Back Press
        binding?.imageView58?.setOnClickListener {
            finish()
        }

        //title
        binding?.textView108?.text = getString(R.string.select_city)

        //Search city
        binding?.searchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding?.searchCity?.text?.chars()
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    binding?.imageView109?.show()
                    binding?.nestedScrollView3?.hide()
                    binding?.consSelectedLocation?.hide()
                    binding?.recyclerViewSearchCity?.show()
                } else {
                    binding?.recyclerViewSearchCity?.hide()
                    binding?.consSelectedLocation?.show()
                    binding?.nestedScrollView3?.show()
                    binding?.consSelectCity?.show()
                    binding?.imageView109?.hide()
                }
            }
        })

        //clear button
        binding?.imageView109?.setOnClickListener {
            binding?.recyclerViewSearchCity?.hide()
            binding?.nestedScrollView3?.show()
            binding?.consSelectCity?.show()
            binding?.searchCity?.text?.clear()
            Constant().hideKeyboard(this)
        }
    }

    private fun selectCity() {
        selectCityViewModel.cityResponseLiveData.observe(this) {
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

    override fun onItemClickCitySearch(
        city: ArrayList<SelectCityResponse.Output.Ot>, position: Int
    ) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.searchCity?.setText("")
        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
        preferences.saveLatitudeData(city[position].lat)
        preferences.saveLongitudeData(city[position].lng)

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            cityDialog(city[position].name)
        }
    }


    private fun retrieveData(output: SelectCityResponse.Output) {
        if (preferences.getIsLogin())
        preferences.saveCityName(output.cc.name)


        if (enableLocation == 1) {
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            filterCityList = output.ot
            val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = OtherCityAdapter(output.ot, output, this, this)
            binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
            binding?.recyclerViewOtherCity?.adapter = otherCityAdapter

            val gridLayout = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val selectCityAdapter = SelectCityAdapter(output.pc, output, this, this)
            binding?.recyclerCity?.layoutManager = gridLayout
            binding?.recyclerCity?.adapter = selectCityAdapter

            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchCityAdapter = SearchCityAdapter(filterCityList!!,  this,preferences.getCityName())
            binding?.recyclerViewSearchCity?.layoutManager = gridLayout3
            binding?.recyclerViewSearchCity?.adapter = searchCityAdapter

        }

    }

    override fun onItemClickCityOtherCity(
        city: ArrayList<SelectCityResponse.Output.Ot>, position: Int
    ) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
        preferences.saveLatitudeData(city[position].lat)
        preferences.saveLongitudeData(city[position].lng)

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            cityDialog(city[position].name)
        }
    }

    override fun onItemClickCityImgCity(city: ArrayList<SelectCityResponse.Output.Pc>, position: Int) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
        preferences.saveLatitudeData(city[position].lat)
        preferences.saveLongitudeData(city[position].lng)

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
            } else {
                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            cityDialog(city[position].name)
        }
    }

    private fun filter(text: String) {
        val filtered: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
        val filtered1: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
        for (item in filterCityList!!) {
            if (item.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }

        if (filtered.isEmpty()) {
            binding?.consSelectedLocation?.hide()
            binding?.nestedScrollView3?.hide()
            binding?.consSelectCity?.hide()
            binding?.recyclerViewSearchCity?.hide()
            binding?.textView124?.show()
            searchCityAdapter?.filterList(filtered1)
        } else {
            binding?.consSelectedLocation?.show()
            binding?.nestedScrollView3?.show()
            binding?.consSelectCity?.show()
            binding?.textView124?.hide()
            binding?.nestedScrollView3?.show()
            searchCityAdapter?.filterList(filtered)
        }
    }

    private fun cityDialog(name: String) {
        cityNameMAin = name
        dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        val inflater = LayoutInflater.from(this)
        val bindingCitySelect = CitySelectDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout>? = dialog?.behavior
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.setContentView(bindingCitySelect.root)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.show()

        bindingCitySelect.textView108.text = preferences.getCityName()

        try {
            val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val dialogCityAdapter = PopUpCityAdapter(list, this, this, cityName)
            bindingCitySelect.recyclerView60.layoutManager = gridLayout4
            bindingCitySelect.recyclerView60.adapter = dialogCityAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemClickCityDialog(city: String) {
        if (city == "All") {
            preferences.saveCityName(cityNameMAin)
        } else {
            preferences.saveCityName(city)
        }
        if (from == "qr") {
            val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
            intent.putExtra("from", "qr")
            intent.putExtra("cid", cid)
            startActivity(intent)
        } else {
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}