package com.net.pvr.ui.location.enableLocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.net.pvr.R
import com.net.pvr.databinding.ActivityEnableLocationBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.location.selectCity.SelectCityActivity
import com.net.pvr.utils.Constant
import com.net.pvr.utils.FetchAddressIntentServices
import com.net.pvr.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EnableLocationActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEnableLocationBinding? = null
    private var from: String = ""
    private var cid: String = ""

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var resultReceiver: ResultReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnableLocationBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        resultReceiver = AddressResultReceiver(
            Handler()
        )

        //Get Intent  Qr Case
        cid = intent.getStringExtra("cid").toString()
        from = intent.getStringExtra("from").toString()
        movedNext()
    }

    private fun movedNext() {
        binding?.include39?.textView5?.text = getString(R.string.enable_location)

        //not Now
        binding?.noThanksTextView?.setOnClickListener {
            if (preferences.getCityName() == "") {
                val intent = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@EnableLocationActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        //Enable Location
        binding?.include39?.textView5?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@EnableLocationActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    this.LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                getCurrentLocation()
            }
        }

        //back press
        binding?.include32?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

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
        LocationServices.getFusedLocationProviderClient(this@EnableLocationActivity)
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

                        if (preferences.getCityName() == "") {
                            val intent = Intent(this@EnableLocationActivity, SelectCityActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@EnableLocationActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
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
//                enableLocation()

            }
        }
    }


    private fun enableLocation() {
        val dialog = OptionDialog(this,
            R.mipmap.ic_launcher,
            R.string.app_name,
            "Enable GPS",
            positiveBtnText = R.string.ok,
            negativeBtnText = R.string.no,
            positiveClick = {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            negativeClick = {})
        dialog.show()
    }

}