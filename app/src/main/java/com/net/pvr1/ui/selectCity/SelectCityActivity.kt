package com.net.pvr1.ui.selectCity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity
import com.net.pvr1.ui.selectCity.adapter.OtherCityAdapter
import com.net.pvr1.ui.selectCity.adapter.PopUpCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SearchCityAdapter
import com.net.pvr1.ui.selectCity.adapter.SelectCityAdapter
import com.net.pvr1.ui.selectCity.response.SelectCityResponse
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(), SearchCityAdapter.RecycleViewItemClickListener,
    OtherCityAdapter.RecycleViewItemClickListenerCity,
    SelectCityAdapter.RecycleViewItemClickListenerSelectCity,
    PopUpCityAdapter.RecycleViewItemClickListener1 {

    @Inject
    lateinit var preferences: PreferenceManager

    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()

    private var filterCityList: ArrayList<SelectCityResponse.Output.Ot>? = null
    private var searchCityAdapter: SearchCityAdapter? = null
    private var list: ArrayList<String> = arrayListOf()

    private var recyclerViewDialog: RecyclerView? = null
    private var subCities: TextView? = null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private var subCityName: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        selectCityViewModel.selectCity(
            preferences.getLatData(),
            preferences.getLangData(),
            preferences.getUserId(),
            "no",
            "no"
        )
        selectCity()


        binding?.searchCity?.setOnClickListener {

            binding?.recyclerViewSearchCity?.show()
            binding?.consSelectedLocation?.show()
            binding?.consSelectCity?.hide()

        }
        movedNext()

        binding?.imageView39?.setOnClickListener {
            getLocation()
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Location City Name
        binding?.txtSelectedCity?.text = preferences.getCityName()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result

                    printLog("latLong -----> ${location!!.latitude} , -->${location.latitude}")
                    val geocoder = Geocoder(this, Locale.getDefault())
//                        val addresses: List<Address>
                    try {
                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses?.isNotEmpty()!!) {
                            val currentAddress2 = addresses[0].getAddressLine(0)
                            val currentAddress = addresses[0].locality

                            preferences.cityName(currentAddress)
                            preferences.latData(location.latitude.toString())
                            preferences.langData(location.latitude.toString())

                            printLog("currentAddress ----- -->${currentAddress}---->${currentAddress2}")
                            printLog("currentAddress1 ----- -->${addresses[0].locality}")

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }


    private fun movedNext() {
        binding?.imageView35?.setOnClickListener {
            finish()
        }

        binding?.searchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.recyclerViewSearchCity?.show()
                binding?.consSelectCity?.hide()
                binding?.consSelectedLocation?.show()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.recyclerViewSearchCity?.show()
                binding?.consSelectCity?.hide()

                binding?.searchCity?.text?.chars()
                filter(s.toString())

                binding?.searchCity?.setOnEditorActionListener { _, action, _ ->
                    if (action == EditorInfo.IME_ACTION_SEARCH) {
                        binding?.searchCity?.text?.chars()
                        filter(s.toString())
                        binding?.searchCity?.hideKeyboard()
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener true
                }

            }

            override fun afterTextChanged(s: Editable?) {
                binding?.recyclerViewSearchCity?.show()
                binding?.consSelectCity?.hide()
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

    override fun onItemClickCitySearch(
        city: ArrayList<SelectCityResponse.Output.Ot>,
        position: Int
    ) {
        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        list.add(0, "All")
        binding?.searchCity?.setText("")
        binding?.txtSelectedCity?.text = city[position].name
        preferences.cityName(city[position].name)
        preferences.latData(city[position].lat)
        preferences.langData(city[position].lng)

        if (city[position].subcities.isEmpty()) {
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
        } else {
            cityDialog()
        }


    }


    private fun retrieveData(output: SelectCityResponse.Output) {

        preferences.cityNameCC(output.cc.name)

        filterCityList = output.ot

        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = OtherCityAdapter(output.ot, output.cc, this, this)
        binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
        binding?.recyclerViewOtherCity?.adapter = otherCityAdapter

        val gridLayout = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val selectCityAdapter = SelectCityAdapter(output.pc, output.cc, this, this)
        binding?.recyclerCity?.layoutManager = gridLayout
        binding?.recyclerCity?.adapter = selectCityAdapter

        val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        searchCityAdapter = SearchCityAdapter(output.ot, output.cc, this, this)
        binding?.recyclerViewSearchCity?.layoutManager = gridLayout3
        binding?.recyclerViewSearchCity?.adapter = searchCityAdapter

    }


    override fun onItemClickCityOtherCity(
        city: ArrayList<SelectCityResponse.Output.Ot>,
        position: Int
    ) {

        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        binding?.txtSelectedCity?.text = city[position].name
        preferences.cityName(city[position].name)
        preferences.latData(city[position].lat)
        preferences.langData(city[position].lng)
        list.add(0, "All")

        println("subcities123--------->${city[position].subcities}")
        println("subcitiesList123--------->${list}")


        if (city[position].subcities.isEmpty()) {
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
        } else {
            cityDialog()
        }

    }

    override fun onItemClickCityImgCity(
        city: ArrayList<SelectCityResponse.Output.Pc>,
        position: Int
    ) {

//      binding?.consSelectedLocation?.show()
//        cityDialog()

        list = arrayListOf(*city[position].subcities.split(",").toTypedArray())
        binding?.txtSelectedCity?.text = city[position].name
        preferences.cityName(city[position].name)
        preferences.latData(city[position].lat)
        preferences.langData(city[position].lng)
        list.add(0, "All")
        println("subcities123--------->${city[position].subcities}")
        println("subcitiesList123--------->${list}")


        if (city[position].subcities.isEmpty()) {
            val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
            startActivity(intent)
        } else {
            cityDialog()
        }

    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
        // running a for loop to compare elements.
        for (item in filterCityList!!) {

            // checking if the entered string matched with any item of our recycler view.
            if (item.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {

            binding?.consSelectedLocation?.show()

//            binding?.txtNoRecord?.show()
//            binding?.recyclerView?.hide()
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()


        } else {

            binding?.consSelectedLocation?.show()

//            binding?.txtNoRecord?.hide()
//            binding?.recyclerView?.show()
            // at last we are passing that filtered
            // list to our adapter class.
            searchCityAdapter?.filterList(filteredlist)
        }
    }

    private fun cityDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.city_select_dialog)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()

        recyclerViewDialog = dialog.findViewById(R.id.recyclerViewCityDialog)
        val cancel = dialog.findViewById<ImageView>(R.id.imgCross)
//        val tittle = dialog.findViewById<TextView>(R.id.textView108)


        try {
            val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val dialogCityAdapter = PopUpCityAdapter(list, this, this, subCityName!!)
            recyclerViewDialog?.layoutManager = gridLayout4
            recyclerViewDialog?.adapter = dialogCityAdapter
        } catch (e: Exception) {
            println("subcitiesError-------->${e.message}")
        }

        for (item in list) {
            if (preferences.getCityName() == item) {
                subCityName?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_1))
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onItemClickCityDialog(city: ArrayList<String>, position: Int, subCity: TextView) {
        preferences.cityName(city[position])
        subCities = subCity
//        subCityName = city[position]
//        subCities?.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_1))
        val intent = Intent(this@SelectCityActivity, HomeActivity::class.java)
        startActivity(intent)

    }


}