package com.net.pvr1.ui.location.enableLocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityEnableLocationBinding
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.location.enableLocation.viewModel.EnableLocationViewModel
import com.net.pvr1.ui.location.selectCity.SelectCityActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.FetchAddressIntentServices
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class EnableLocationActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEnableLocationBinding? = null
    private val authViewModel: EnableLocationViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1
    private var resultReceiver: ResultReceiver? = null
    private var latitude: String = ""
    private var longitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnableLocationBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        resultReceiver = AddressResultReceiver(Handler())
        movedNext()
    }

    private fun movedNext() {
        //not Now
        binding?.noThanksTextView?.setOnClickListener {
            val intent = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
            startActivity(intent)
        }

        binding?.enableLocationButton?.setOnClickListener {
            loadLocation()
        }
        //back press
        binding?.include32?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun loadLocation() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@EnableLocationActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            getLatLong()
        }
    }


    private fun getLatLong() {
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

        LocationServices.getFusedLocationProviderClient(this@EnableLocationActivity)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(applicationContext)
                        .removeLocationUpdates(this)
                    if (locationResult.locations.size > 0) {
                        val latestIndex = locationResult.locations.size - 1
                        latitude = locationResult.locations[latestIndex].latitude.toString()
                        longitude = locationResult.locations[latestIndex].longitude.toString()


                        preferences.saveLatitudeData(latitude)
                        preferences.saveLongitudeData(longitude)

                        val location = Location("providerNA")
                        location.longitude = longitude.toDouble()
                        location.latitude = latitude.toDouble()
                        fetchAddressFromLocation(location)

                    } else {

                    }
                }
            }, Looper.getMainLooper())
    }

    private fun fetchAddressFromLocation(location: Location) {
        val intent = Intent(this, FetchAddressIntentServices::class.java)
        intent.putExtra(Constant.RECEVIER, resultReceiver)
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, location)
        startService(intent)

        val intent2 = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
        startActivity(intent2)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLatLong()
            } else {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please Enable Your Location",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri: Uri = Uri.fromParts("package", this.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    },
                    negativeClick = {
                    })
                dialog.show()
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}