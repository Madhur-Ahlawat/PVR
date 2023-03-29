package com.net.pvr.ui.location.selectCity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySelectCityBinding
import com.net.pvr.databinding.CitySelectDialogBinding
import com.net.pvr.databinding.LocationDialogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.location.selectCity.adapter.OtherCityAdapter
import com.net.pvr.ui.location.selectCity.adapter.PopUpCityAdapter
import com.net.pvr.ui.location.selectCity.adapter.SearchCityAdapter
import com.net.pvr.ui.location.selectCity.adapter.SelectCityAdapter
import com.net.pvr.ui.location.selectCity.response.SelectCityResponse
import com.net.pvr.ui.location.selectCity.viewModel.SelectCityViewModel
import com.net.pvr.ui.scanner.bookings.SelectBookingsActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(),
    SearchCityAdapter.RecycleViewItemClickListener,
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

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var resultReceiver: ResultReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        resultReceiver = AddressResultReceiver(
            Handler()
        )

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
        if (preferences.getCityName().isEmpty()) {
            binding?.consSelectedLocation?.show()
        } else {
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
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@SelectCityActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    this.LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                getCurrentLocation()
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
//        preferences.saveLatitudeData(city[position].lat)
//        preferences.saveLongitudeData(city[position].lng)

        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
            bundle.putString("var_login_city", city[position].name)
            GoogleAnalytics.hitEvent(this, "login_city", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
                finish()
            } else {
// Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                    bundle.putString("var_login_city", city[position].name)
                    GoogleAnalytics.hitEvent(this, "home_city_name", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                intent.putExtra("from", from)
                startActivity(intent)
                finish()
            }
        } else {
            cityDialog(city[position].name)
        }
    }

    private fun retrieveData(output: SelectCityResponse.Output) {
//        if (preferences.getIsLogin()){
//        preferences.saveCityName(output.cc.name)
//        }

        if (enableLocation == 1) {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                bundle.putString("var_login_city", output.cc.name)
                GoogleAnalytics.hitEvent(this, "home_city_name", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            preferences.saveCityName(output.cc.name)
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)

            intent.putExtra("from", from)
            startActivity(intent)
            finish()
        } else {
            filterCityList = output.ot
            val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = OtherCityAdapter(output.ot, output, this, this)
            binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
            binding?.recyclerViewOtherCity?.adapter = otherCityAdapter

            val gridLayout = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val selectCityAdapter = SelectCityAdapter(output.pc, preferences.getCityName(), this, this)
            binding?.recyclerCity?.layoutManager = gridLayout
            binding?.recyclerCity?.adapter = selectCityAdapter

            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            searchCityAdapter = SearchCityAdapter(filterCityList!!, this, preferences.getCityName())
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
//        preferences.saveLatitudeData(city[position].lat)
//        preferences.saveLongitudeData(city[position].lng)
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
            bundle.putString("var_login_city", city[position].name)
            GoogleAnalytics.hitEvent(this, "login_city", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
                finish()
            } else {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                    bundle.putString("var_login_city", city[position].name)
                    GoogleAnalytics.hitEvent(this, "home_city_name", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                preferences.saveCityName(city[position].name)
                intent.putExtra("from", from)
                startActivity(intent)
                finish()
            }
        } else {
            cityDialog(city[position].name)
        }
    }

    override fun onItemClickCityImgCity(
        city: ArrayList<SelectCityResponse.Output.Pc>,
        position: Int
    ) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
        preferences.saveCityName(city[position].name)
//        preferences.saveLatitudeData(city[position].lat)
//        preferences.saveLongitudeData(city[position].lng)
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
            bundle.putString("var_login_city", city[position].name)
            GoogleAnalytics.hitEvent(this, "login_city", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (city[position].subcities.isEmpty()) {
            if (from == "qr") {
                val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                intent.putExtra("from", "qr")
                intent.putExtra("cid", cid)
                startActivity(intent)
                finish()
            } else {
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                    bundle.putString("var_login_city", city[position].subcities)
                    GoogleAnalytics.hitEvent(this, "home_city_name", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
                intent.putExtra("from", from)
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
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                bundle.putString("var_login_city", cityNameMAin)
                GoogleAnalytics.hitEvent(this, "login_city", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            preferences.saveCityName(city)
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                bundle.putString("var_login_city", city)
                GoogleAnalytics.hitEvent(this, "login_city", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (from == "qr") {
            val intent = Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
            intent.putExtra("from", "qr")
            intent.putExtra("cid", cid)
            startActivity(intent)
            finish()
        } else {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                bundle.putString("var_login_city", city)
                GoogleAnalytics.hitEvent(this, "home_city_name", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            intent.putExtra("from", from)
            startActivity(intent)
            finish()
        }
    }

    //Enable Location Condition

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this@SelectCityActivity)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(applicationContext)
                        .removeLocationUpdates(this)
                    if (locationResult.locations.size > 0) {
                        val latestIndex = locationResult.locations.size - 1
                        val lat = locationResult.locations[latestIndex].latitude
                        val long = locationResult.locations[latestIndex].longitude

                        val location = Location("providerNA")
                        Constant.latitude = lat
                        Constant.longitude = long
                        preferences.saveLatitudeData(lat.toString())
                        preferences.saveLongitudeData(long.toString())

                        if (from == "qr") {
                            val intent =
                                Intent(this@SelectCityActivity, SelectBookingsActivity::class.java)
                            intent.putExtra("from", "qr")
                            intent.putExtra("cid", cid)
                            startActivity(intent)
                            finish()
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
                        location.longitude = long
                        location.latitude = lat
                        fetchLocation(location)
                    } else {
                        printLog("")
                    }
                }
            }, Looper.getMainLooper())
    }

    private class AddressResultReceiver(handler: Handler?) :
        ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == Constant.SUCCESS_RESULT) {
                val address = resultData.getString(Constant.ADDRESS)
                val locaity = resultData.getString(Constant.LOCAITY)
                val state = resultData.getString(Constant.STATE)
                val district = resultData.getString(Constant.DISTRICT)
                val country = resultData.getString(Constant.COUNTRY)
                val postcode = resultData.getString(Constant.POST_CODE)
            }
        }
    }

    private fun fetchLocation(location: Location) {
        val intent = Intent(this, FetchAddressIntentServices::class.java)
        intent.putExtra(Constant.RECEVIER, resultReceiver)
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, location)
        startService(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                enableLocation()
            }
        }
    }

    //    Location Dialog
    private fun enableLocation() {

        if (from == "Homepage") {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login Screen")
                bundle.putString("var_enable_location", "")
                GoogleAnalytics.hitEvent(this, "home_city_name", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val dialog = BottomSheetDialog(this@SelectCityActivity, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val inflater = LayoutInflater.from(this)
        val locationDialog = LocationDialogBinding.inflate(inflater)
        dialog.setContentView(locationDialog.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)

        locationDialog.include39.textView5.text= getString(R.string.enable_location)
//        Dismiss Dialog
        locationDialog.noThanksTextView.setOnClickListener {
            dialog.dismiss()
        }

//        Open Setting
        locationDialog.include39.textView5.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        dialog.show()
    }

}