package com.net.pvr1.ui.selectCity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySelectCityBinding
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
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SelectCityActivity : AppCompatActivity(), SearchCityAdapter.RecycleViewItemClickListener,
    OtherCityAdapter.RecycleViewItemClickListenerCity,
    SelectCityAdapter.RecycleViewItemClickListenerSelectCity, OnMapReadyCallback,
    PermissionListener {

//    @Inject
//    late in it var preferences: AppPreferences

    private var binding: ActivitySelectCityBinding? = null
    private var loader: LoaderDialog? = null
    private val selectCityViewModel: SelectCityViewModel by viewModels()
    private var otherCityList: ArrayList<SelectCityResponse.Output.Ot> = ArrayList()
    private var otherCityAdapter: SearchCityAdapter? = null
    private var dataList:ArrayList<Any> = ArrayList()

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        const val REQUEST_CHECK_SETTINGS = 43
    }

    var latitude: String = ""
    var longitude: String = ""
    var currentAddress: String = ""

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCityBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)!!
            mapFragment.getMapAsync(this)
//            fusedLocationProviderClient = FusedLocationProviderClient(this)
        } catch (e: ClassNotFoundException) {
            println("ClassNotFoundException------>${e.message}")
        }

        selectCityViewModel.selectCity("28.679079", "77.069710", "0", "no", "no")
        selectCity()
        movedNext()
    }

    private fun movedNext() {
        binding?.imageView35?.setOnClickListener {
            finish()
        }

        binding?.searchCity?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                var cs = cs
                if (cs.toString().isEmpty()) {
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
        binding?.searchCity?.setText(" ")
        binding?.searchCity?.isFocusable = false
        binding?.searchCity?.isClickable = false
        binding?.txtSelectedCity?.text = city[position].name
//        preferences.putString(Constant.CITY_NAME, city[position].name)
        binding?.recyclerViewSearchCity?.hide()
        binding?.consSelectCity?.show()
        binding?.consSelectedLocation?.show()

    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }


    private fun retrieveData(output: SelectCityResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = OtherCityAdapter(output.ot, this, this)
        binding?.recyclerViewOtherCity?.layoutManager = gridLayout2
        binding?.recyclerViewOtherCity?.adapter = otherCityAdapter


        dataList.clear()
        otherCityList = output.ot
        dataList.addAll(output.ot)
        dataList.addAll(output.pc)

        val gridLayout = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val selectCityAdapter = SelectCityAdapter(output.pc, this, this)
        binding?.recyclerCity?.layoutManager = gridLayout
        binding?.recyclerCity?.adapter = selectCityAdapter

        val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter4 = SearchCityAdapter(output.ot, dataList, dataList, this, this)
        binding?.recyclerViewSearchCity?.layoutManager = gridLayout3
        binding?.recyclerViewSearchCity?.adapter = otherCityAdapter4
    }


    override fun onItemClickCityOtherCity(city: ArrayList<SelectCityResponse.Output.Ot>, position: Int) {
        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
//        preferences.putString(Constant.CITY_NAME, city[position].name)
    }

    override fun onItemClickCityImgCity(city: ArrayList<SelectCityResponse.Output.Pc>, position: Int) {
        binding?.consSelectedLocation?.show()
        binding?.txtSelectedCity?.text = city[position].name
//        preferences.putString(Constant.CITY_NAME, city[position].name)

    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map?: return
        if (isPermissionGiven()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            getCurrentLocation()
        } else {
            givePermission()
        }
    }

    private fun isPermissionGiven(): Boolean{
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun givePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        getCurrentLocation()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Permission required for showing location", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onPermissionRationaleShouldBeShown(permission: com.karumi.dexter.listener.PermissionRequest?, token: PermissionToken?) {
        token!!.continuePermissionRequest()
    }

    private fun getCurrentLocation() {

        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val result = LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates!!.isLocationPresent){
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> { }
                }
            }
        }
    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    val mLastLocation = task.result

                    println("lat -----> ${mLastLocation.latitude} , -->${mLastLocation.latitude}")

                    var address = "No known address"

                    val gcd = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address>
                    try {
                        addresses = gcd.getFromLocation(mLastLocation!!.latitude, mLastLocation.longitude, 1)!!
                        if (addresses.isNotEmpty()) {
                            address = addresses[0].getAddressLine(0)

                            val currentAddress2 = addresses[0].getAddressLine(0)
                            currentAddress = addresses[0].locality

                            println("currentAddress ----- -->${currentAddress}---->${currentAddress2}")
                            println("currentAddress1 ----- -->${addresses[0].locality}")

//                            binding?.currentAddressTxt?.text = currentAddress2.toString()

//                            if (addresses[0].getAddressLine(0) != null) {
//                                textGoogleAddress!!.text = addresses[0].getAddressLine(0)
//                            }
//                            if (addresses[0].getAddressLine(1) != null) {
//                                textGoogleAddress!!.text = textGoogleAddress!!.text.toString() + addresses[0].getAddressLine(1)
//                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

//                    val icon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.ic_pickup))
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(mLastLocation!!.latitude, mLastLocation.longitude))
                            .title("Current Location")
                            .snippet(address)
//                            .icon(icon)
                    )

                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                        .zoom(17f)
                        .build()
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                } else {
                    Toast.makeText(this, "No current location found", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CHECK_SETTINGS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    getCurrentLocation()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }


}