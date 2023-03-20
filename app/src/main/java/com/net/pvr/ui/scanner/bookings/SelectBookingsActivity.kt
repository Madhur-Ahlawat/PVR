package com.net.pvr.ui.scanner.bookings

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySelectBookingsBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.FoodActivity
import com.net.pvr.ui.home.fragment.more.prefrence.response.PreferenceResponse
import com.net.pvr.ui.location.enableLocation.EnableLocationActivity
import com.net.pvr.ui.location.selectCity.SelectCityActivity
import com.net.pvr.ui.scanner.bookings.viewModel.SelectBookingViewModel
import com.net.pvr.ui.scanner.response.GetFoodResponse
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SelectBookingsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySelectBookingsBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: SelectBookingViewModel by viewModels()

    private var cinetypeQR = "NORMAL"
    private var cinemaId = ""

    //location Mange
    private val REQUEST_LOCATION = 1
    private var locationManager: LocationManager? = null
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0

    private var click = false
    private var clickF = false

    private var format = "ALL"
    private var price1 = "ALL"
    private var price2 = "ALL"
    private var price = ""
    private var show1 = "ALL"
    private var show = ""
    private var hc = "ALL"
    private var show2 = "ALL"
    private var cinema_type = "ALL"
    private var lang = "ALL"
    private var childcinemaId = ""
    private var shows = ""
    private var childCinemaname = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBookingsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        cinemaId = intent.getStringExtra("cid").toString()

//Manage Location
        ActivityCompat.requestPermissions(
            this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION
        )
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) != true) {
            onGPS()
        } else {
            getLocation()
        }

        movedNext()
        //Api Response
        getTheaterDetailResponse()
        getCodeResponse()
        getOutletsResponse()
        checkUserLocationData()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun enable() {
        click = true
        binding?.tvCinemaName?.setTextColor(getColor(R.color.pvr_dark_black))
        binding?.tvSelectBookDummy?.setTextColor(getColor(R.color.pvr_dark_black))
        binding?.tvTicket?.setTextColor(getColor(R.color.pvr_dark_black))
        binding?.tvFood?.setTextColor(getColor(R.color.pvr_dark_black))
        binding?.llTicketFood?.background = getDrawable(R.drawable.threater_select)
        binding?.onlyFood?.background = getDrawable(R.drawable.food_select)
    }

    private fun enableFood() {
        clickF = true
        click = true
        binding?.tvCinemaName?.setTextColor(getColor(R.color.pvr_dark_black))
        binding?.tvSelectBookDummy?.setTextColor(getColor(R.color.pvr_dark_black))
        // tvTicket_.setTextColor(getResources().getColor(R.color.pvr_dark_black));
        binding?.tvFood?.setTextColor(getColor(R.color.pvr_dark_black))

        // llTicket_Food.setBackground(getResources().getDrawable(R.drawable.threater_select));
        binding?.onlyFood?.background = getDrawable(R.drawable.food_select)
    }

    private fun disable() {
        click = false
        clickF = false
        // tvCinemaName.setText("Please select the correct location!");
        binding?.tvCinemaName?.setTextColor(getColor(R.color.red_color_loc))
        binding?.tvSelectBookDummy?.setTextColor(getColor(R.color.gray))
        binding?.tvTicket?.setTextColor(getColor(R.color.gray))
        binding?.tvFood?.setTextColor(getColor(R.color.gray))
        binding?.llTicketFood?.background = getDrawable(R.drawable.threater_select_gray)
        binding?.onlyFood?.background = getDrawable(R.drawable.food_select_gray)
    }

    private fun movedNext() {
        binding?.textView24?.text = preferences.getCityName()
        binding?.textView24?.setOnClickListener {
            if (!Constant().locationServicesEnabled(this)){
                val intent = Intent(this, EnableLocationActivity::class.java)
                intent.putExtra("from","qr")
                intent.putExtra("cid",cinemaId)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, SelectCityActivity::class.java)
                intent.putExtra("from","qr")
                intent.putExtra("cid",cinemaId)
                startActivity(intent)
                finish()
            }

        }
        if (preferences.getCityName().isNotEmpty()) {
            binding?.tvCinemaName?.hide()
        }
        //title
        binding?.include33?.textView108?.text = "Select Booking"
        //Back
        binding?.include33?.imageView58?.setOnClickListener {
            finish()
        }

        binding?.onlyFood?.setOnClickListener {
            println("onlyFood-->$clickF")
            if (clickF)
            getOutlets()
        }

        cinetypeQR = "NORMAL"
        if (intent.hasExtra("type") && !TextUtils.isEmpty(intent.getStringExtra("type"))) cinetypeQR =
            intent.getStringExtra("type").toString()

        binding?.llTicketFood?.setOnClickListener {
            if (cinemaId.isNotEmpty() && click){
                Constant.QR = "YES"
                val intent = Intent(this, CinemaSessionActivity::class.java)
                intent.putExtra("cid", cinemaId)
                intent.putExtra("lat", latitude.toString())
                intent.putExtra("lang", longitude.toString())
                intent.putExtra("cityName", getCityName())
                intent.putExtra("addressCinema", "yes")
                startActivity(intent)
            }
        }
    }

    private fun getCityName(): String {
        var cityName = ""
        cityName = preferences.getCityName()
        return cityName
    }


    //Cinema Details
    private fun getTheaterDetail() {
        //price
        price = if (price1.equals("ALL", ignoreCase = true) || price2.equals(
                "ALL", ignoreCase = true
            )
        ) {
            "ALL"
        } else {
            "$price1-$price2"
        }
        //date Time
        show =
            if (show1.equals("ALL", ignoreCase = true) || show2.equals("ALL", ignoreCase = true)) {
                "ALL"
            } else {
                "$show1-$show2"
            }


        authViewModel.cinemaSession(
            preferences.getCityName(),
            cinemaId,
            latitude.toString(),
            longitude.toString(),
            preferences.getUserId(),
            "",
            lang,
            format,
            price,
            show,
            "ALL",
            "",
            "",
            "YES",
            cinema_type,
            cinetypeQR
        )
    }

    private fun getTheaterDetailResponse() {
        authViewModel.cinemaSessionLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveDataTheater(it.data.output)
                    } else {
                        disable()

                        if (it.data?.code == 21999) {
                            checkUserLocation()
                            binding?.tvCinemaName?.text = it.data.msg
                        } else {
                            getCode()
                        }

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
                    disable()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }
    }

    private fun checkUserLocation() {
        authViewModel.userLocation(
            preferences.getUserId(),
            latitude.toString(),
            longitude.toString(),
            preferences.getCityName()
        )
    }


    private fun checkUserLocationData(){
        authViewModel.userLocationLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

//                        if (!TextUtils.isEmpty(changeLocation.getOutput().getCity()) &&
//                            !changeLocation.getOutput().getCity().equalsIgnoreCase("No City Found")
//                            && changeLocation.getOutput().getChange().equalsIgnoreCase("true")) {
//                            PCApplication.getPreference().putString(
//                                PCConstants.SharedPreference.SELECTED_CITY_NAME,
//                                changeLocation.getOutput().getCity()
//                            )
//                            PCApplication.getPreference().putString(
//                                PCConstants.SharedPreference.SELECTED_CITY_ID,
//                                changeLocation.getOutput().getCity()
//                            )
//                            binding?.subTitle.setText(getCityName())
                            getTheaterDetail()
//                        }

                    } else {
//                        val dialog = OptionDialog(this,
//                            R.mipmap.ic_launcher,
//                            R.string.app_name,
//                            it.data?.msg.toString(),
//                            positiveBtnText = R.string.ok,
//                            negativeBtnText = R.string.no,
//                            positiveClick = {},
//                            negativeClick = {})
//                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
//                    loader?.dismiss()
//                    val dialog = OptionDialog(this,
//                        R.mipmap.ic_launcher,
//                        R.string.app_name,
//                        it.message.toString(),
//                        positiveBtnText = R.string.ok,
//                        negativeBtnText = R.string.no,
//                        positiveClick = {},
//                        negativeClick = {})
//                    dialog.show()
                }
                is NetworkResult.Loading -> {
//                    loader = LoaderDialog(R.string.pleasewait)
//                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveDataTheater(output: CinemaSessionResponse.Output) {
        enableFood()

        if (output.childs.isNotEmpty()) {
            childcinemaId = output.childs[0].ccid
            childCinemaname = output.cn
            shows = output.s
            binding?.tvCinemaName?.show()
            val cinemaName = "Hi, Welcome to " + "<b>" + output.cn + "</b>"
            binding?.tvCinemaName?.text = Html.fromHtml(cinemaName)
            var distance: String = output.d.replace("km away", "")
            distance = distance.trim { it <= ' ' }

            //new change
            if (!TextUtils.isEmpty(output.gfe.toString()) && output.gfe.equals("true") && !TextUtils.isEmpty(
                    output.gfd
                ) && distance.toDouble() > output.gfd.toDouble() / 1000
            ) {
                disable()
                binding?.tvCinemaName?.text = "This Qr Code can be scanned from cinema only"
            } else {
                enable()
            }
        } else {
            disable()
        }


    }

    //getCode
    private fun getCode() {
        authViewModel.getCode(cinemaId, cinetypeQR)
    }

    private fun getCodeResponse() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveCode(it.data.output)
                    } else {
                        disable()
                        if (it.data?.code == 21999) {
                            checkUserLocation()
                            binding?.tvCinemaName?.text = it.data.msg
                        } else {
//                            PCApiErrorHandler.handleErrorMessage(
//                                data.getCode(),
//                                data.getMsg(),
//                                context as Activity?,
//                                dialog,
//                                errorLayout,
//                                this@SelectBookingActivity,
//                                PCConstants.PaymentType.THEATER
//                            )
                        }
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
                    disable()
//                    loader?.dismiss()
//                    val dialog = OptionDialog(this,
//                        R.mipmap.ic_launcher,
//                        R.string.app_name,
//                        it.message.toString(),
//                        positiveBtnText = R.string.ok,
//                        negativeBtnText = R.string.no,
//                        positiveClick = {},
//                        negativeClick = {})
//                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveCode(output: PreferenceResponse.Output) {
//        val cinemaName = "Hi, Welcome to " + "<b>" + output.cn + "</b>"
//        binding?.tvCinemaName?.text = Html.fromHtml(cinemaName)

        enableFood()
    }


    private fun getOutlets() {
        authViewModel.foodOutlet(
            preferences.getUserId(), childcinemaId, "", "", "", "", "", "", "", "", "", ""
        )
    }

    private fun getOutletsResponse() {
        authViewModel.foodOutletResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveOutlet(it.data.output)
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveOutlet(output: List<GetFoodResponse.Output>) {
        //comment for only show food----------------------------------
        if (output!= null &&output.size > 1) {
//        if (output!= null ) {
            dataToOutletsFood(output)
        } else {
            dataToFood(output)
        }
    }

    private fun dataToOutletsFood(output: List<GetFoodResponse.Output>) {
//        if ((click || clickF) && !TextUtils.isEmpty(childcinemaId)) {
//            EventBus.getDefault().postSticky(output)
//            fromScan = "scan"
//            val intent = Intent(this, Outlets_Activity::class.java)
//            val paymentIntentData = PaymentIntentData()
//            intent.putExtra("from", "pscan")
//            paymentIntentData.setCinemaID(childcinemaId)
//            paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//            paymentIntentData.setName("")
//            paymentIntentData.setFnb(PCConstants.FNB)
//            paymentIntentData.setSessionActive(true)
//            intent.putExtra(PCConstants.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData)
//            intent.putExtra("SEAT", "")
//            intent.putExtra("AUDI", "")
//            startActivity(intent)
//        }
    }

    private fun dataToFood(output: List<GetFoodResponse.Output>) {
        if ((click || clickF) && !TextUtils.isEmpty(childcinemaId)) {
            Constant.CINEMA_ID=childcinemaId
            Constant.QR = "YES"
            Constant.BOOK_TYPE = "FOOD"
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
//            fromScan = "scan"
//            val intent = Intent(this@SelectBookingActivity, GrabABiteActivity::class.java)
//            val paymentIntentData = PaymentIntentData()
//            intent.putExtra("from", "pscan")
//            if (msg.equals("true", ignoreCase = true)) {
//                intent.putExtra("NF", true)
//            } else {
//                intent.putExtra("NF", false)
//            }
//            paymentIntentData.setCinemaID(childcinemaId)
//            paymentIntentData.setPaymentType(PCConstants.PaymentType.INTHEATRE)
//            paymentIntentData.setName("")
//            paymentIntentData.setFnb(PCConstants.FNB)
//            paymentIntentData.setSessionActive(true)
//            intent.putExtra(PCConstants.IntentKey.TICKET_BOOKING_DETAILS, paymentIntentData)
//            intent.putExtra("SEAT", "")
//            intent.putExtra("AUDI", "")
//            if (getIntent().hasExtra("iserv") && !TextUtils.isEmpty(getIntent().getStringExtra("iserv"))) intent.putExtra(
//                "iserv",
//                getIntent().getStringExtra("iserv")
//            )
//            startActivity(intent)
//            //new added
//            finish()
        }
    }

    //location
    private fun onGPS() {
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

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION
            )
        } else {
            val locationGPS = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            printLog("location---->$locationGPS")
            if (locationGPS != null) {
                val lat = locationGPS.latitude
                val long = locationGPS.longitude
                longitude = long
                latitude = lat
                getTheaterDetail()
                printLog("$latitude---->$longitude")

            } else {
//                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}