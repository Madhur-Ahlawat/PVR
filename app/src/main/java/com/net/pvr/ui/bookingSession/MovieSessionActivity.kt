package com.net.pvr.ui.bookingSession

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.ActivityBookingBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.adapter.*
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr.ui.bookingSession.viewModel.BookingViewModel
import com.net.pvr.ui.cinemaSession.cinemaDetails.CinemaDetailsActivity
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.filter.GenericFilterSession
import com.net.pvr.ui.home.fragment.home.adapter.PromotionAdapter
import com.net.pvr.ui.home.fragment.home.response.HomeResponse
import com.net.pvr.utils.*
import com.net.pvr.utils.ga.GoogleAnalytics
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
@AndroidEntryPoint
class MovieSessionActivity : AppCompatActivity(),
    BookingShowsDaysAdapter.RecycleViewItemClickListenerCity,
    BookingShowsLanguageAdapter.RecycleViewItemClickListenerCity,
    BookingTheatreAdapter.RecycleViewItemClickListener,
    BookingPlaceHolderAdapter.RecycleViewItemClickListenerCity,
    BookingShowsParentAdapter.RecycleViewItemClickListener, GenericFilterSession.onButtonSelected,
    StoriesProgressView.StoriesListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityBookingBinding? = null
    private val authViewModel: BookingViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var daySessionResponse: BookingResponse.Output? = null
    private var daysClick: Boolean = false
    private var cinemaId: ArrayList<String> = ArrayList()

    private var lang = "ALL"
    private var format = "ALL"
    private var price1 = "ALL"
    private var price2 = "ALL"
    private var show1 = "ALL"
    private var hc = "ALL"
    private var show2 = "ALL"
    private var special = "ALL"
    private var cinemaType = "ALL"
    private var movieId = ""
    private var appliedFilterItem = HashMap<String?, String?>()
    private var appliedFilterType = ""

    private var bookingResponse: BookingResponse.Output? = null
    private var su = ""
    private var sm = ""
    private var bookingShowsParentAdapter: BookingShowsParentAdapter? = null


    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    // story board
    private var bannerShow = 0
    private var pressTime = 0L
    private var limit = 500L
    private var counterStory = 0
    private var currentPage = 1
    private var bannerModelsMain: ArrayList<BookingResponse.Output.Pu> =
        ArrayList<BookingResponse.Output.Pu>()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var lat = "0.0"
    private var lng = "0.0"

    companion object {
        var btnc = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        manageFunctions()
    }

    private fun manageFunctions() {
        //Poster
        binding?.include43?.editTextTextPersonName?.hint = "Search cinema"
        getLocation()


        binding?.textView103?.isSelected = true

        //internet Check
        broadcastReceiver = NetworkReceiver()

        binding?.imageView54?.setOnClickListener {
            Constant.onShareClick(this, su, sm)
        }

        if (intent.hasExtra("mid")) movieId = intent.getStringExtra("mid").toString()
        val intent = intent
        val action = intent.action
        val data = intent.data
        if (data != null) {
            val path = data.path

            val parts = path!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size == 5) {
                preferences.saveString(Constant.SharedPreference.SELECTED_CITY_NAME, parts[2])
                preferences.saveString(Constant.SharedPreference.SELECTED_CITY_ID, parts[2])
                movieId = parts[4]

            } else if (parts.size == 4) {
                movieId = parts[3]
            }
        }

        movedNext()
        broadcastIntent()
        getShimmerData()
        bookingTicketDataLoad()
        bookingTheatreDataLoad()
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
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
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

                        authViewModel.bookingTicket(
                            preferences.getCityName(),
                            movieId,
                            lat,
                            lng,
                            "NA",
                            "no",
                            "no",
                            preferences.getUserId(),
                            lang,
                            format,
                            price1,
                            hc,
                            show1,
                            cinemaType,
                            special
                        )

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } else {
            authViewModel.bookingTicket(
                preferences.getCityName(),
                movieId,
                lat,
                lng,
                "NA",
                "no",
                "no",
                preferences.getUserId(),
                lang,
                format,
                price1,
                hc,
                show1,
                cinemaType,
                special
            )
        }

    }


    private fun movedNext() {
        //back Click
        binding?.imageView53?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // search Cancel
        binding?.include43?.cancelBtn?.setOnClickListener {
            binding?.constraintLayout19?.show()
            binding?.constraintLayout151?.hide()

        }

        // search Click
        binding?.imageView55?.setOnClickListener {
            binding?.constraintLayout19?.invisible()
            binding?.constraintLayout151?.show()
        }

        //search
        binding?.include43?.editTextTextPersonName?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {

                if (bookingResponse != null) {
                    try {
                        filter(s.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })


        binding?.include43?.voiceBtn?.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                resultLauncher.launch(intent)
            } catch (e: Exception) {
                toast(e.message)
            }
        }
    }

    //Shimmer text
    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filteredNames: ArrayList<BookingResponse.Output.Cinema> =
            ArrayList<BookingResponse.Output.Cinema>()

        //looping through existing elements
        for (list in bookingResponse?.cinemas!!) {

            //if the existing elements contains the search input
            if (list.cn.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {

                //adding the element to filtered list
                filteredNames.add(list)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        if (filteredNames.size > 0) {
            bookingShowsParentAdapter?.filterList(
                filteredNames
            )
        } else {
            bookingShowsParentAdapter?.filterList(
                filteredNames
            )
        }


        //calling a method of the adapter class and passing the filtered list
        if (filteredNames.size > 0) {
            bookingShowsParentAdapter?.filterList(
                filteredNames
            )
        } else {
            bookingShowsParentAdapter?.filterList(
                filteredNames
            )
        }
    }


    private fun bookingTicketDataLoad() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        if (!daysClick) {
                            daySessionResponse = it.data.output
                        }
                        bookingResponse = it.data.output
                        su = it.data.output.su
                        sm = it.data.output.sm
                        btnc = it.data.output.btnc
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

    private fun bookingTheatreDataLoad() {
        authViewModel.userResponseTheatreLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveTheatreData(it.data.output)
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
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun retrieveTheatreData(output: BookingTheatreResponse.Output) {
        if (output.m.isEmpty()) {
            binding?.constraintLayout83?.hide()
        } else {
            binding?.constraintLayout83?.show()
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val bookingTheatreAdapter = BookingTheatreAdapter(output.m, this, this)
            binding?.recyclerView12?.layoutManager = gridLayout2
            binding?.recyclerView12?.adapter = bookingTheatreAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: BookingResponse.Output) {
        //Layout
        binding?.constraintLayout152?.show()

        //Shimmer
        binding?.constraintLayout145?.hide()

        if (!daysClick) {
            Constant.OfferDialogImage = output.mih
            //MovieName
            binding?.textView103?.text = daySessionResponse?.nm

            //language
            val language: String = java.lang.String.join(",", output.lngs)

            //genre
            binding?.textView104?.text =
                daySessionResponse?.gnr + " " + getString(R.string.dots) + " " + language

            //recycler Days
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            binding?.recyclerView9?.layoutManager = gridLayout2
            binding?.recyclerView9?.onFlingListener = null
            val snapHelper = PagerSnapHelper()

            snapHelper.attachToRecyclerView(binding?.recyclerView9)
            val bookingShowsDaysAdapter =
                BookingShowsDaysAdapter(daySessionResponse?.dys!!, this, this)
            binding?.recyclerView9?.layoutManager = gridLayout2
            binding?.recyclerView9?.adapter = bookingShowsDaysAdapter

            //recycler Language
            binding?.recyclerView10?.hide()
            val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            val bookingShowsLanguageAdapter =
                BookingShowsLanguageAdapter(daySessionResponse?.lngs!!, this, this)
            binding?.recyclerView10?.layoutManager = gridLayout
            binding?.recyclerView10?.adapter = bookingShowsLanguageAdapter
        }

        //Shows
        val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        bookingShowsParentAdapter =
            BookingShowsParentAdapter(output.cinemas, this, this, output.adlt)
        binding?.recyclerView8?.layoutManager = gridLayout3
        binding?.recyclerView8?.adapter = bookingShowsParentAdapter

        for (data in output.cinemas) {
            cinemaId.add(data.cid.toString())
        }

        val string: String = java.lang.String.join(",", cinemaId)
        authViewModel.bookingTheatre(
            preferences.getCityName(), string, preferences.getUserId(), movieId, "HINDI", "no"
        )


        binding?.filterFab?.setOnClickListener {
            val gFilter = GenericFilterSession()
            val filterPoints = HashMap<String, ArrayList<String>>()
            if (output.lngs != null && output.lngs.size > 1) filterPoints[Constant.FilterType.LANG_FILTER] =
                output.lngs
            else filterPoints[Constant.FilterType.LANG_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.GENERE_FILTER] = ArrayList()
            if (output.icn != null && output.icn.size > 1) filterPoints[Constant.FilterType.FORMAT_FILTER] =
                output.icn else filterPoints[Constant.FilterType.FORMAT_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] =
                ArrayList(listOf("Wheelchair Friendly"))
            filterPoints[Constant.FilterType.PRICE_FILTER] = ArrayList(
                listOf("Below ₹300", "₹301 - 500", "₹501 - 1000", "₹1001 - 1500")
            )
            filterPoints[Constant.FilterType.SHOWTIME_FILTER] = ArrayList()
            if (output.ct != null && output.ct.size > 0) filterPoints[Constant.FilterType.CINEMA_FORMAT] =
                output.ct else filterPoints[Constant.FilterType.CINEMA_FORMAT] = ArrayList()
            if (output.sps != null && output.sps.size > 0) filterPoints[Constant.FilterType.SPECIAL_SHOW] =
                getSpsList() as ArrayList<String> else filterPoints[Constant.FilterType.SPECIAL_SHOW] =
                ArrayList()
            gFilter.openFilters(
                this, "ShowTime", this, appliedFilterType, appliedFilterItem, filterPoints
            )
        }

        //placeHolder
        if (output.ph.isNotEmpty()) {
            binding?.constraintLayout123?.show()
            updatePH(output.ph)
        } else {
            binding?.constraintLayout123?.hide()
        }

        if (bannerShow == 0 && output.pu.isNotEmpty()) {
            initBanner(output.pu)
        }

    }

    private fun updatePH(phd: ArrayList<HomeResponse.Ph>) {
        if (phd != null && phd.size > 0) {
            binding?.include41?.placeHolderView?.show()
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            binding?.include41?.recyclerPromotion?.layoutManager = layoutManager
            binding?.include41?.recyclerPromotion?.onFlingListener = null
            snapHelper.attachToRecyclerView(binding?.include41?.recyclerPromotion!!)
            binding?.include41?.recyclerPromotion?.layoutManager = layoutManager
            val adapter = PromotionAdapter(this, phd)
            binding?.include41?.recyclerPromotion?.adapter = adapter
            if (phd.size > 1) {
                val speedScroll = 5000
                val handler = Handler()
                val runnable: Runnable = object : Runnable {
                    var count = 0
                    var flag = true
                    override fun run() {
                        if (count < adapter.itemCount) {
                            if (count == adapter.itemCount - 1) {
                                flag = false
                            } else if (count == 0) {
                                flag = true
                            }
                            if (flag) count++ else count--
                            binding?.include41?.recyclerPromotion?.smoothScrollToPosition(
                                count
                            )
                            handler.postDelayed(this, speedScroll.toLong())
                        }
                    }
                }
                handler.postDelayed(runnable, speedScroll.toLong())
            }
        } else {
            binding?.include41?.placeHolderView?.hide()
        }
    }


    private fun getSpsList(): Any {
        val data = ArrayList<String>()
        for (data1 in daySessionResponse?.sps!!) {
            data.add(data1.na)
        }
        return data
    }


    override fun showsDaysClick(comingSoonItem: BookingResponse.Output.Dy, itemView: View) {
        daysClick = true
        authViewModel.bookingTicket(
            preferences.getCityName(),
            movieId,
            lat,
            lng,
            comingSoonItem.dt,
            "no",
            "no",
            preferences.getUserId(),
            lang,
            format,
            price1,
            hc,
            show1,
            cinemaType,
            special
        )
        Constant.focusOnView(itemView, binding?.recyclerView9!!)
    }

    override fun languageClick(comingSoonItem: String) {

    }

    override fun theatreClick(comingSoonItem: BookingTheatreResponse.Output.M) {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Movie Showtimes page")
            GoogleAnalytics.hitEvent(this, "movie_also_playing_list", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(this, MovieSessionActivity::class.java)
        intent.putExtra("mid", comingSoonItem.m)
        startActivity(intent)
        finish()

    }

    override fun placeHolder(comingSoonItem: BookingResponse.Output.Ph) {

    }

    override fun alertClick(comingSoonItem: BookingResponse.Output.Cinema) {
        val intent = Intent(this@MovieSessionActivity, CinemaDetailsActivity::class.java)
        intent.putExtra("cid", comingSoonItem.cid.toString())
        startActivity(intent)
    }

    private fun selectedFilter(filterItemSelected: HashMap<String?, String?>?): Boolean {
        var selected = false
        if (filterItemSelected != null) {
            for (key: Map.Entry<String?, String?> in filterItemSelected) {
                println("selectedFilters---->" + key.value + "----")
                if (!key.value.equals("", ignoreCase = true) && !key.value!!.contains("ALL")) {
                    selected = true
                }
            }
        }
        return selected
    }


    override fun onApply(
        type: ArrayList<String>?,
        filterItemSelected: HashMap<String?, String?>?,
        isSelected: Boolean,
        itemSelected: String?
    ) {
        if (type?.size!! > 0) {
            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            if (filterItemSelected != null) {
                appliedFilterItem = filterItemSelected
            }
            val containLanguage = type.contains("language")
            if (containLanguage == true) {
                val index = type.indexOf("language")
                val value = filterItemSelected?.get(type[index])
                if (value != null && !value.equals("", ignoreCase = true)) {
                    appliedFilterType = "language"
                    lang = value.uppercase(Locale.getDefault())
                } else {
                    lang = "ALL"
                }
            }

            val containTime = type.contains("time")
            if (containTime == true) {
                val index = type.indexOf("time")
                var value = filterItemSelected?.get(type[index])
                if (value != null && !value.equals("", ignoreCase = true)) {
                    appliedFilterType = "time"
                    val pos = value.indexOf("(") + 1
                    value = value.substring(pos)
                    val splitSymbol = value.split("-").toTypedArray()
                    show1 = splitSymbol[0]
                    show2 = splitSymbol[1]
                    val inputFormat: DateFormat = SimpleDateFormat("h:mma")
                    val outputFormatter: DateFormat = SimpleDateFormat("H:mm")
                    try {
                        if (show1 != "ALL") {
                            show1 = inputFormat.parse(show1)?.let { outputFormatter.format(it) }
                                .toString()
                            show2 = outputFormatter.format(show2)
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                } else {
                    show1 = "ALL"
                    show2 = "ALL"
                }
            }
            val containFormat = type.contains("format")
            if (containFormat == true) {
                val index = type.indexOf("format")
                val value = filterItemSelected?.get(type[index])
                format = if (value != null && !value.equals(
                        "", ignoreCase = true
                    )
                ) value else "ALL"
            }

            val containCinemaFormat = type.contains("cinema")
            if (containCinemaFormat == true) {
                val index = type.indexOf("cinema")
                val value = filterItemSelected?.get(type[index])
                cinemaType = if (value != null && !value.equals(
                        "", ignoreCase = true
                    )
                ) value else "ALL"

                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                    bundle.putString("var_cinema_format", cinemaType)
                    GoogleAnalytics.hitEvent(this, "book_cinema_format", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val containSpecialFormat = type.contains("special")
            if (containSpecialFormat == true) {
                val index = type.indexOf("special")
                val value = filterItemSelected?.get(type[index])
                special = if (value != null && !value.equals(
                        "", ignoreCase = true
                    )
                ) value else "ALL"
            }

            val containPrice = type.contains("price")
            if (containPrice == true) {
                val index = type.indexOf("price")
                var value = filterItemSelected?.get(type[index])
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
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
                    bundle.putString("var_price_range", price2)
                    GoogleAnalytics.hitEvent(this, "book_price_range", bundle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val containAccessibility = type.contains("accessability")
            if (containAccessibility == true) {
                val index = type.indexOf("accessability")
                val value = filterItemSelected?.get(type[index])
                if (value != null && !value.equals("", ignoreCase = true)) {
                    if (value.contains("Wheelchair")) hc = "hc"
                    if (value.contains("Subtitles")) special = "RST"
                } else {
                    hc = "ALL"
                    special = "ALL"
                }
                // Hit Event
                try {
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                    bundle.putString("var_price_range", price2)
                    GoogleAnalytics.hitEvent(this, "book_accessibility", bundle)
                } catch (e: Exception) {

                }
                var show = "ALL"
                var price = "ALL"
                price = if (price1 != "ALL") {
                    "$price1-$price2"
                } else {
                    "ALL"
                }

                if (show1 != "ALL") {
                    show = "$show1-$show2"
                }

                authViewModel.bookingTicket(
                    preferences.getCityName(),
                    movieId,
                    lat,
                    lng,
                    "NA",
                    "no",
                    "no",
                    preferences.getUserId(),
                    lang,
                    format,
                    price,
                    hc,
                    show,
                    cinemaType,
                    special
                )

                if (!selectedFilter(filterItemSelected)) {
                    binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
                }

            }
        }

    }


    override fun onReset() {
        // Hit Event
        try {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Booking")
//                    bundle.putString("var_price_range", price2)
            GoogleAnalytics.hitEvent(this, "book_reset_filter", bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
        appliedFilterItem = HashMap()
        lang = "ALL"
        format = "ALL"
        price1 = "ALL"
        price2 = "ALL"
        show1 = "ALL"
        hc = "ALL"
        show2 = "ALL"
        special = "ALL"
        cinemaType = "ALL"
        authViewModel.bookingTicket(
            preferences.getCityName(),
            movieId,
            lat,
            lng,
            "NA",
            "no",
            "no",
            preferences.getUserId(),
            lang,
            format,
            price1,
            hc,
            show1,
            cinemaType,
            special
        )
    }


    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    //Voice search
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val result: ArrayList<String>? = data?.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    binding?.include43?.editTextTextPersonName?.setText(
                        Objects.requireNonNull(result)?.get(0)
                    )
                }
            }
        }

    //Banner
    private fun initBanner(bannerModels: ArrayList<BookingResponse.Output.Pu>) {
        bannerShow += 1
        bannerModelsMain = bannerModels
        if (bannerModels.isNotEmpty()) {
            binding?.rlBanner?.show()
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoriesCount(bannerModels.size) // <- set stories
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoryDuration(5000L) // <- set a story duration
            binding?.bannerLayout?.includeStoryLayout?.stories?.setStoriesListener(this) // <- set listener
            binding?.bannerLayout?.includeStoryLayout?.stories?.startStories() // <- start progress
            counterStory = 0
            if (!TextUtils.isEmpty(bannerModels[counterStory].i)) {
                Picasso.get().load(bannerModels[counterStory].i)
                    .into(binding?.bannerLayout?.includeStoryLayout?.ivBanner!!, object : Callback {
                        override fun onSuccess() {
                            binding?.rlBanner?.show()
                            //  storiesProgressView.startStories(); // <- start progress
                        }

                        override fun onError(e: Exception?) {}
                    })
            }

            binding?.bannerLayout?.includeStoryLayout?.reverse?.setOnClickListener { binding?.bannerLayout?.includeStoryLayout?.stories?.reverse() }
            binding?.bannerLayout?.includeStoryLayout?.reverse?.setOnTouchListener(onTouchListener)
            showButton(bannerModels[0])
            binding?.bannerLayout?.includeStoryLayout?.skip?.setOnClickListener { binding?.bannerLayout?.includeStoryLayout?.stories?.skip() }
            binding?.bannerLayout?.includeStoryLayout?.skip?.setOnTouchListener(onTouchListener)
            binding?.bannerLayout?.tvButton?.setOnClickListener {
                binding?.rlBanner?.hide()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "image", ignoreCase = true
                    )
//                        .equalsIgnoreCase("image")
                ) {
                    if (bannerModels[counterStory].redirectView.equals(
                            "DEEPLINK", ignoreCase = true
                        )
//                        equalsIgnoreCase("DEEPLINK")
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)) {
                            if (bannerModels[counterStory].redirect_url.lowercase(Locale.ROOT)
                                    .contains("/loyalty/home")
                            ) {
//                                if (context is PCLandingActivity) (context as PCLandingActivity).PriviegeFragment(
//                                    "C"
//                                )
                            } else {
//                                .replaceAll("https", "app").replaceAll("http", "app")
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        bannerModels[counterStory].redirect_url.replace(
                                            "https", "app"
                                        )
                                    )
                                )
                                startActivity(intent)
                            }
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals(
                            "INAPP", ignoreCase = true
                        )
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)

                        ) {
//                            val intent = Intent(context, PrivacyActivity::class.java)
//                            intent.putExtra("url", bannerModels[counterStory].getRedirect_url())
//                            intent.putExtra(PCConstants.IS_FROM, 2000)
//                            intent.putExtra("title", bannerModels[counterStory].getName())
//                            startActivity(intent)
                        }
                    } else if (bannerModels[counterStory].redirect_url.equals(
                            "WEB", ignoreCase = true
                        )
//                            .equalsIgnoreCase("WEB")
                    ) {
                        if (bannerModels[counterStory].redirect_url.equals("", ignoreCase = true)
//                                .equalsIgnoreCase("")
                        ) {
//                            val intent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse(bannerModels[counterStory].redirect_url)
//                                        activity.startActivity(intent)
                        }
                    }
                }
            }
            (this.findViewById(R.id.bannerLayout) as RelativeLayout).show()

            binding?.bannerLayout?.ivPlay?.setOnClickListener {
                binding?.rlBanner?.hide()
                if (bannerModels.size > 0 && bannerModels[counterStory].type.equals(
                        "video", ignoreCase = true
                    )
                ) {

                }
            }

            binding?.bannerLayout?.ivCross?.setOnClickListener {
                binding?.rlBanner?.hide()
            }

        } else {
            binding?.rlBanner?.hide()
        }
    }

    private fun showButton(bannerModel: BookingResponse.Output.Pu) {
        if (bannerModel.type.uppercase(Locale.getDefault()) == "VIDEO" && bannerModel.trailerUrl != "") {
            binding?.bannerLayout?.ivPlay?.show()
            binding?.bannerLayout?.tvButton?.hide()
        } else if (bannerModel.type.uppercase(Locale.getDefault()) == "IMAGE" && bannerModel.redirect_url != "") {
            binding?.bannerLayout?.ivPlay?.hide()
            binding?.bannerLayout?.tvButton?.text = bannerModel.buttonText
            binding?.bannerLayout?.tvButton?.show()
        } else {

            binding?.bannerLayout?.ivPlay?.hide()
            binding?.bannerLayout?.tvButton?.hide()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                binding?.bannerLayout?.includeStoryLayout?.stories?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                binding?.bannerLayout?.includeStoryLayout?.stories?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onNext() {
        try {
            if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
                ++counterStory
                showButton(bannerModelsMain[counterStory])
                binding?.bannerLayout?.includeStoryLayout?.ivBanner?.let {
                    Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPrev() {
        if (counterStory - 1 < 0) return
        if (!TextUtils.isEmpty(bannerModelsMain[counterStory].i)) {
            --counterStory
            showButton(bannerModelsMain[counterStory])
            binding?.bannerLayout?.includeStoryLayout?.ivBanner?.let {
                Glide.with(this).load(bannerModelsMain[counterStory].i).into(it)
            }
        }
    }

    override fun onComplete() {
        binding?.bannerLayout?.includeStoryLayout?.stories?.destroy()
        binding?.bannerLayout?.includeStoryLayout?.stories?.startStories()
        currentPage = 0
        binding?.rlBanner?.hide()
    }
}