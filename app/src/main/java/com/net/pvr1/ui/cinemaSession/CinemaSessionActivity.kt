package com.net.pvr1.ui.cinemaSession

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCinemaSessionBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.bookingSession.MovieSessionActivity.Companion.btnc
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionCinParentAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionDaysAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionLanguageAdapter
import com.net.pvr1.ui.cinemaSession.adapter.CinemaSessionNearTheaterAdapter
import com.net.pvr1.ui.cinemaSession.response.CinemaNearTheaterResponse
import com.net.pvr1.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr1.ui.cinemaSession.viewModel.CinemaSessionViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.filter.GenericFilter
import com.net.pvr1.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION", "NAME_SHADOWING")
@AndroidEntryPoint
class CinemaSessionActivity : AppCompatActivity(),
    CinemaSessionDaysAdapter.RecycleViewItemClickListenerCity,
    CinemaSessionLanguageAdapter.RecycleViewItemClickListenerCity,CinemaSessionNearTheaterAdapter.RecycleViewItemClickListenerCity,
    GenericFilter.onButtonSelected {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCinemaSessionBinding? = null
    private val authViewModel: CinemaSessionViewModel by viewModels()
    private var loader: LoaderDialog? = null

    private var cinemaId = "0"
    private var openTime = 0
    private var rowIndex = false

    private var appliedFilterItem = HashMap<String, String>()
    private var appliedFilterType = ""
    private var sessionDate = "NA"
    private var lang = "ALL"

    private var format = "ALL"

    private var price1 = "ALL"

    private var price2 = "ALL"

    private var show1 = "ALL"
    private var hc = "ALL"
    private var ad = "ALL"
    private var cc = "ALL"
    private var show2 = "ALL"

    private var special = "ALL"
    private var cinema_type = "ALL"

    private var lat = "0.0"
    private var lng = "0.0"

    private var cinemaLat = "0.0"
    private var cinemaLng = "0.0"

    private lateinit var mFusedLocationClient: FusedLocationProviderClient



    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCinemaSessionBinding.inflate(layoutInflater, null, false)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val view = binding?.root
        setContentView(view)
        manageFunctions()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
            if (isLocationEnabled() && checkPermissions()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    try {
                        val addresses = location?.longitude?.let {
                            location.latitude.let { it1 ->
                                geocoder.getFromLocation(
                                    it1, it, 1
                                )
                            }
                        }
                        if (addresses?.isNotEmpty() == true) {
//                            val currentAddress: String = addresses[0].locality
//                            preferences.cityNameCinema(currentAddress)
                            lat = location.latitude.toString()
                            lng = location.longitude.toString()

                            if (intent.getStringExtra("addressCinema") == "yes") {
                                authViewModel.cinemaSession(
                                    preferences.getCityName(),
                                    cinemaId,
                                    lat,
                                    lng,
                                    preferences.getUserId(),
                                    sessionDate,
                                    lang,
                                    format,
                                    price1,
                                    price1,
                                    hc,
                                    cc,
                                    ad,
                                    "no",
                                    cinema_type,
                                    ""
                                )
                            } else {
                                authViewModel.cinemaSession(
                                    preferences.getCityName(),
                                    cinemaId,
                                    lat,
                                    lng,
                                    preferences.getUserId(),
                                    sessionDate,
                                    lang,
                                    format,
                                    price1,
                                    price1,
                                    hc,
                                    cc,
                                    ad,
                                    "no",
                                    cinema_type,
                                    ""
                                )
                            }

                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            } else {
                if (intent.getStringExtra("addressCinema") == "yes") {
                    authViewModel.cinemaSession(
                        preferences.getCityName(),
                        cinemaId,
                        lat,
                        lng,
                        preferences.getUserId(),
                        sessionDate,
                        lang,
                        format,
                        price1,
                        price1,
                        hc,
                        cc,
                        ad,
                        "no",
                        cinema_type,
                        ""
                    )
                } else {
                    authViewModel.cinemaSession(
                        preferences.getCityName(),
                        cinemaId,
                        lat,
                        lng,
                        preferences.getUserId(),
                        sessionDate,
                        lang,
                        format,
                        price1,
                        price1,
                        hc,
                        cc,
                        ad,
                        "no",
                        cinema_type,
                        ""
                    )
                }
            }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun manageFunctions() {
        cinemaId = intent.getStringExtra("cid").toString()
        getLocation()

        cinemaSessionDataLoad()
        cinemaNearTheaterLoad()

        //Hide AppBar
        Constant().appBarHide(this)

        //back Press
        binding?.imageView41?.setOnClickListener {
            onBackPressed()
        }
        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()
        getShimmerData()

    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText,binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText,null)
    }

    private fun cinemaSessionDataLoad() {
        authViewModel.cinemaSessionLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                        btnc = it.data.output.btnc
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

    private fun cinemaNearTheaterLoad() {
        authViewModel.cinemaSessionNearTheaterLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveTheaterData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }


    private fun retrieveTheaterData(output: CinemaNearTheaterResponse.Output) {
        //recycler Cinemas
        val snapHelper = PagerSnapHelper()
        binding?.recyclerView18?.onFlingListener = null
        snapHelper.attachToRecyclerView(binding?.recyclerView18)
        val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val cinemaSessionNearTheaterAdapter = CinemaSessionNearTheaterAdapter(output.c, this, this)
        binding?.recyclerView18?.layoutManager = gridLayout4
        binding?.recyclerView18?.adapter = cinemaSessionNearTheaterAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: CinemaSessionResponse.Output) {
        //movie Details
        binding?.nestedScrollView5?.show()
        //shimmer
        binding?.constraintLayout145?.hide()

        cinemaLat = output.lat
        cinemaLng = output.lang

        if (openTime == 0) {
            //title
            binding?.textView84?.text = output.cn
            //address
            binding?.textView81?.text = output.addr
            //Image
            Glide.with(this@CinemaSessionActivity).load(output.imob).error(R.drawable.app_icon)
                .into(binding?.imageView40!!)
            //Direction
            binding?.view64?.setOnClickListener {
                Constant().openMap(this, output.lat, output.lang)
            }
            //Share
            binding?.imageView42?.setOnClickListener {
                Constant().shareData(this, "", "")
            }
            //Distance
            binding?.textView86?.text = output.d
            //Shows
            binding?.textView85?.text = output.msc.toString() + " " + getString(R.string.shows)

            //recycler Cinemas
            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val cinemaSessionCinParentAdapter =
                CinemaSessionCinParentAdapter(output.childs, this, cinemaId)
            binding?.recyclerView15?.layoutManager = gridLayout3
            binding?.recyclerView15?.adapter = cinemaSessionCinParentAdapter
            binding?.textView99?.text = output.cn

            if (output.newCinemaText!= null && output.newCinemaText!= ""){
                binding?.cinemaLocation?.show()
                binding?.cinemaLocation?.text = output.newCinemaText
            } else {
                binding?.cinemaLocation?.hide()
            }

            if (output.like) {
                rowIndex = true
                binding?.imageView43?.setImageResource(R.drawable.like)
            } else {
                rowIndex = false
                binding?.imageView43?.setImageResource(R.drawable.unlike)
            }

            binding?.imageView43?.setOnClickListener {
                if (preferences.getIsLogin()) {
                    if (rowIndex) {
                        rowIndex = false
                        binding?.imageView43?.setImageResource(R.drawable.unlike)
                        authViewModel.cinemaPreference(
                            preferences.getUserId(),
                            cinemaId,
                            rowIndex,
                            "t",
                            Constant().getDeviceId(this)
                        )
                    } else {
                        rowIndex = true
                        binding?.imageView43?.setImageResource(R.drawable.like)
                        authViewModel.cinemaPreference(
                            preferences.getUserId(),
                            cinemaId,
                            rowIndex,
                            "t",
                            Constant().getDeviceId(this)
                        )
                    }

                } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher_foreground,
                        R.string.app_name,
                        this.getString(R.string.loginCinema),
                        positiveBtnText = R.string.yes,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        },
                        negativeClick = {})
                    dialog.show()
                }

            }


            //recycler Days
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val cinemaSessionDaysAdapter = CinemaSessionDaysAdapter(output.bd, this, this)
            binding?.recyclerView13?.layoutManager = gridLayout2
            binding?.recyclerView13?.adapter = cinemaSessionDaysAdapter

            //Promotion
            if (output.phd!=null && output.phd.isNotEmpty()) {
                binding?.constraintLayout16?.show()
                val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                val cinemaSessionLanguageAdapter = PromotionAdapter(this, output.phd)
                binding?.recyclerView14?.layoutManager = gridLayout
                binding?.recyclerView14?.adapter = cinemaSessionLanguageAdapter
            }else{
                binding?.constraintLayout16?.hide()
            }

        } else {
            //recycler Cinemas
            val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val cinemaSessionCinParentAdapter =
                CinemaSessionCinParentAdapter(output.childs, this, cinemaId)
            binding?.recyclerView15?.layoutManager = gridLayout3
            binding?.recyclerView15?.adapter = cinemaSessionCinParentAdapter
            binding?.textView99?.text = output.cn
        }

        binding?.filterFab?.setOnClickListener {
            val gFilter = GenericFilter()
            val filterPoints = HashMap<String, ArrayList<String>>()
            if (output.lngs != null && output.lngs.size > 1) filterPoints[Constant.FilterType.LANG_FILTER] =
                output.lngs
            else filterPoints[Constant.FilterType.LANG_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.GENERE_FILTER] = ArrayList()
            if (output.fmts != null && output.fmts.size > 1) filterPoints[Constant.FilterType.FORMAT_FILTER] =
                output.fmts else filterPoints[Constant.FilterType.FORMAT_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] =
                ArrayList(listOf("Wheelchair Friendly"))
            filterPoints[Constant.FilterType.PRICE_FILTER] = ArrayList(
                listOf("Below ₹300", "₹301 - 500", "₹501 - 1000", "₹1001 - 1500")
            )
            filterPoints[Constant.FilterType.SHOWTIME_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.CINEMA_FORMAT] = ArrayList()
            filterPoints[Constant.FilterType.SPECIAL_SHOW] =
                ArrayList()
            gFilter.openFilters(
                this, "ShowTimeT", this, appliedFilterType, appliedFilterItem, filterPoints
            )
        }
//Theater
        authViewModel.cinemaNearTheater(
            preferences.getCityName(),
            cinemaLat,
            cinemaLng,
            cinemaId
        )
    }

    override fun dateClick(comingSoonItem: CinemaSessionResponse.Output.Bd) {
        openTime = 1
        sessionDate = comingSoonItem.dt
        if (intent.getStringExtra("addressCinema") == "yes") {
            authViewModel.cinemaSession(
                preferences.getCityName(),
                cinemaId,
                lat,
                lng,
                preferences.getUserId(),
                sessionDate,
                lang,
                format,
                price1,
                price1,
                hc,
                cc,
                ad,
                "no",
                cinema_type,
                ""
            )
        } else {
            authViewModel.cinemaSession(
                preferences.getCityName(),
                cinemaId,
                lat,
                lng,
                preferences.getUserId(),
                sessionDate,
                lang,
                format,
                price1,
                price1,
                hc,
                cc,
                ad,
                "no",
                cinema_type,
                ""
            )
        }
    }

    override fun languageClick(comingSoonItem: String) {

    }

//    Theater Shows
    override fun showsClick(comingSoonItem: CinemaNearTheaterResponse.Output.C) {
        cinemaId= comingSoonItem.cId.toString()
    authViewModel.cinemaSession(
        preferences.getCityName(),
        cinemaId,
        lat,
        lng,
        preferences.getUserId(),
        sessionDate,
        lang,
        format,
        price1,
        price1,
        hc,
        cc,
        ad,
        "no",
        cinema_type,
        ""
    )
    }

//Map theater
    override fun nearTheaterDirectionClick(comingSoonItem: CinemaNearTheaterResponse.Output.C) {
        Constant().openMap(this,comingSoonItem.lat,comingSoonItem.lang )
    }

    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onApply(
        type: ArrayList<String>?,
        name: HashMap<String, String>?,
        isSelected: Boolean,
        filterItemSelected: String?
    ) {
        if (type!!.size > 0) {
            println("type--->$type---->$name---->")

            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = name!!
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                val value: String = name[type[index]].toString()
                if (value != null && !value.equals("", ignoreCase = true))
                    lang = value.uppercase(Locale.getDefault()
                ) else lang = "ALL"
            }

            val containTime = type.contains("time")
            if (containTime) {
                val index = type.indexOf("time")
                var value: String? = name[type[index]]
                if (value != null && !value.equals("", ignoreCase = true)) {
                    val pos = value.indexOf("(") + 1
                    value = value.substring(pos)
                    val splitSymbol = value.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                   show1 = splitSymbol[0]
                   show2 = splitSymbol[1]
                    val inputFormat: DateFormat = SimpleDateFormat("h:mma")
                    val outputFormatter: DateFormat = SimpleDateFormat("H:mm")
                    try {
                       show1 =
                            outputFormatter.format(inputFormat.parse(show1))
                       show2 =
                            outputFormatter.format(inputFormat.parse(show2))
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                } else {
                   show1 = "ALL"
                   show2 = "ALL"
                }
            }
            val containFormat = type.contains("format")
            if (containFormat) {
                val index = type.indexOf("format")
                val value: String = name[type[index]].toString()
                if (value != null && !value.equals("", ignoreCase = true))
                    format = value.uppercase(Locale.getDefault()
                ) else format = "ALL"
            }
            val containPrice = type.contains("price")
            if (containPrice) {
                val index = type.indexOf("price")
                var value = name[type[index]]

                if (value != null && !value.equals("", ignoreCase = true)) {
                    val splitSymbol = value.split("₹").toTypedArray()
                    value = splitSymbol[1]
                    if (value.contains("-")) {
                        val split = value.split(" - ").toTypedArray()
                        price1 = split[0]
                        price2 = split[1]
                    } else if (splitSymbol[0].equals("Below ", ignoreCase = true)) {
                        price1 = "0"
                        price2 = value
                    } else {
                        price1 = value
                        price2 = "2500"
                    }

                } else {
                    price1 = "ALL"
                    price2 = "ALL"
                }
            }
            val containAccessability = type.contains("accessability")
            if (containAccessability) {
                val index = type.indexOf("accessability")
                val value: String? = name[type[index]]
                if (value != null && !value.equals("", ignoreCase = true)) {
                    if (value.contains("Wheelchair"))
                        hc = "hc"
                    if (value.contains("Subtitles"))
                        special = "RST"
                } else {
                   special = "ALL"
                   hc = "ALL"
                }
                Log.d("ShowSelection", "onAply filter accessability: $value")
            }

            var show = "ALL"
            var price = "ALL"
            if (price1 != "ALL"){
                price = "$price1-$price2"
            }
            if (show1 != "ALL"){
                show = "$show1-$show2"
            }

            authViewModel.cinemaSession(
                preferences.getCityName(),
                cinemaId,
                lat,
                lng,
                preferences.getUserId(),
                sessionDate,
                lang,
                format,
                price,
                show,
                hc,
                cc,
                ad,
                "no",
                cinema_type,
                ""
            )
            if (!selectedFilter(name)) {
                binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
            }
//            ShowSelectionLayout.getMovieShowDetailMSession(movieId, cityId, PCConstants.IntentKey.DEFAULT_PAGE_NUMBER, date_time, mRecyclerView, context, false, "", latitude, longitude);
        } else binding?.filterFab?.setImageResource(R.drawable.filter_unselect)

    }

    private fun selectedFilter(filterItemSelected: HashMap<String, String>): Boolean {
        var selected = false
        for (key: Map.Entry<String, String> in filterItemSelected) {
            println("selectedFilters---->" + key.key + "----")
            if (!key.value.equals("", ignoreCase = true) && !key.value!!.contains("ALL")) {
                selected = true
            }
        }
        return selected
    }


    override fun onReset() {
        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        lang = "ALL"
        format = "ALL"
        price1 = "ALL"
        show1 = "ALL"
        hc = "ALL"
        cc = "ALL"
        ad = "ALL"
        cinema_type = "ALL"

        appliedFilterItem = HashMap()
        authViewModel.cinemaSession(
            preferences.getCityName(),
            cinemaId,
            lat,
            lng,
            preferences.getUserId(),
            sessionDate,
            lang,
            format,
            price1,
            show1,
            hc,
            cc,
            ad,
            "no",
            cinema_type,
            ""
        )
    }
}