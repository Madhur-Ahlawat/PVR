package com.net.pvr1.ui.bookingSession

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityBookingBinding
import com.net.pvr1.ui.bookingSession.adapter.*
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.bookingSession.viewModel.BookingViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.filter.GenericFilterMsession
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BookingActivity : AppCompatActivity(),
    BookingShowsDaysAdapter.RecycleViewItemClickListenerCity,
    BookingShowsLanguageAdapter.RecycleViewItemClickListenerCity,
    BookingTheatreAdapter.RecycleViewItemClickListener,
    BookingPlaceHolderAdapter.RecycleViewItemClickListenerCity,
    BookingShowsParentAdapter.RecycleViewItemClickListener, GenericFilterMsession.onButtonSelected {

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
    private var ad = "ALL"
    private var cc = "ALL"
    private var show2 = "ALL"
    private var special = "ALL"
    private var cinema_type = "ALL"
    private var appliedFilterItem = HashMap<String?, String?>()
    private var appliedFilterType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        authViewModel.bookingTicket(
            preferences.getCityName(),
            intent.getStringExtra("mid").toString(),
            preferences.getLatitudeData(),
            preferences.getLongitudeData(),
            "NA",
            "no",
            "no",
            preferences.getUserId()
        )

        movedNext()
        bookingTicketDataLoad()
        bookingTheatreDataLoad()
    }

    private fun movedNext() {
        binding?.imageView53?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
                    toast("loading")
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
//                    loader?.dismiss()
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
//                    loader = LoaderDialog(R.string.pleasewait)
//                    loader?.show(supportFragmentManager, null)
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
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding?.recyclerView9)
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
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
        val bookingShowsParentAdapter = BookingShowsParentAdapter(output.cinemas, this, this)
        binding?.recyclerView8?.layoutManager = gridLayout3
        binding?.recyclerView8?.adapter = bookingShowsParentAdapter

        //placeHolder
        printLog("placeHolder--->${output.ph}")
        if (output.ph.isNotEmpty()) {
            binding?.constraintLayout123?.show()
            val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val bookingPlaceHolderAdapter = BookingPlaceHolderAdapter(output.ph, this, this)
            binding?.recyclerView23?.layoutManager = gridLayout4
            binding?.recyclerView23?.adapter = bookingPlaceHolderAdapter
        } else {
            binding?.constraintLayout123?.hide()
        }

        for (data in output.cinemas) {
            cinemaId.add(data.cid.toString())
        }
        val string: String = java.lang.String.join(",", cinemaId)

        authViewModel.bookingTheatre(
            preferences.getCityName(),
            string,
            preferences.getUserId(),
            intent.getStringExtra("mid").toString(),
            "HINDI",
            "no"
        )





        binding?.filterFab?.setOnClickListener {
            val gFilter = GenericFilterMsession()
            val filterPoints = HashMap<String, ArrayList<String>>()
            if (output.lngs != null && output.lngs.size > 1
            ) filterPoints[Constant.FilterType.LANG_FILTER] = output.lngs
                 else filterPoints[Constant.FilterType.LANG_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.GENERE_FILTER] = ArrayList()
            if (output.icn != null && output.icn.size > 1
            ) filterPoints[Constant.FilterType.FORMAT_FILTER] = output.icn else filterPoints[Constant.FilterType.FORMAT_FILTER] = ArrayList()
            filterPoints[Constant.FilterType.ACCESSABILITY_FILTER] =
                ArrayList(Arrays.asList(*arrayOf("Wheelchair Friendly")))
            filterPoints[Constant.FilterType.PRICE_FILTER] = ArrayList(
                listOf(
                    *arrayOf(
                        "Below ₹300", "₹301 - 500", "₹501 - 1000", "₹1001 - 1500"
                    )
                )
            )
            filterPoints[Constant.FilterType.SHOWTIME_FILTER] = ArrayList()
            if (output.ct != null && output.ct.size > 0
            ) filterPoints[Constant.FilterType.CINEMA_FORMAT] = output.ct else filterPoints[Constant.FilterType.CINEMA_FORMAT] = ArrayList()
            if (output.sps != null && output.sps.size > 0
            ) filterPoints[Constant.FilterType.SPECIAL_SHOW] =
                getSpsList() as ArrayList<String> else filterPoints[Constant.FilterType.SPECIAL_SHOW] = ArrayList()
            gFilter.openFilters(
                this, "ShowTime", this, appliedFilterType, appliedFilterItem, filterPoints
            )

        }

    }

    private fun getSpsList(): Any {
        val data = ArrayList<String>()
        for (data1 in daySessionResponse?.sps!!) {
            data.add(data1.na)
        }
        return data
    }


    override fun showsDaysClick(comingSoonItem: BookingResponse.Output.Dy) {
        daysClick = true
        authViewModel.bookingTicket(
            preferences.getCityName(),
            intent.getStringExtra("mid").toString(),
            preferences.getLatitudeData(),
            preferences.getLongitudeData(),
            comingSoonItem.dt,
            "no",
            "no",
            preferences.getUserId()
        )
    }

    override fun languageClick(comingSoonItem: String) {

    }

    override fun theatreClick(comingSoonItem: BookingTheatreResponse.Output.M) {
        val intent = Intent(this, BookingActivity::class.java)
        intent.putExtra("mid", comingSoonItem.m)
        startActivity(intent)
        finish()

    }

    override fun placeHolder(comingSoonItem: BookingResponse.Output.Ph) {

    }

    override fun alertClick(comingSoonItem: BookingResponse.Output.Cinema) {

        bookingAlert(comingSoonItem)
    }

    private fun bookingAlert(comingSoonItem: BookingResponse.Output.Cinema) {
        printLog("$comingSoonItem")
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.booking_alert)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()
        val ola = dialog.findViewById<CardView>(R.id.textView145)
        ola.setOnClickListener {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.olacabs.customer")
            if (launchIntent != null) {
                startActivity(launchIntent) //null pointer check in case package name was not found
            } else {
                val uri = Uri.parse("market://details?id=com.olacabs.customer")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.olacabs.customer")
                        )
                    )
                }
            }
        }
    }

    override fun onApply(
        type: ArrayList<String?>,
        filterItemSelected: HashMap<String?, String?>,
        isSelected: Boolean,
        itemSelected: String?
    ) {
        if (type.size > 0) {
            binding?.filterFab?.setImageResource(R.drawable.filter_selected)
            appliedFilterItem = filterItemSelected
//            binding?.appliedFilter.setVisibility(View.VISIBLE)
            println("typeFilter--->$type---$filterItemSelected")
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                val value = filterItemSelected[type[index]]
                if (value != null && !value.equals("", ignoreCase = true)) {
                    appliedFilterType = "language"
                    lang = value.uppercase(Locale.getDefault())
                } else {
                    lang = "ALL"
                }
            }

            val containTime = type.contains("time")
            if (containTime) {
                val index = type.indexOf("time")
                var value = filterItemSelected[type[index]]
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
                        show1 = outputFormatter.format(inputFormat.parse(show1))
                        show2 = outputFormatter.format(show2)
                        Log.d(
                            "ShowSelection", "onAply filter time: " + show1 + "  " + show2
                        )
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
                val value = filterItemSelected[type[index]]
                format = if (value != null && !value.equals(
                        "", ignoreCase = true
                    )
                ) value else "ALL"
            }
            val containCinemaFormat = type.contains("cinema")
            if (containCinemaFormat) {
                val index = type.indexOf("cinema")
                val value = filterItemSelected[type[index]]
                if (value != null && !value.equals(
                        "", ignoreCase = true
                    )
                ) cinema_type =
                    value else cinema_type =
                    "ALL"
            }
            val containSpecialFormat = type.contains("special")
            if (containSpecialFormat) {
                val index = type.indexOf("special")
                val value = filterItemSelected[type[index]]
                if (value != null && !value.equals(
                        "", ignoreCase = true
                    )
                ) special =
                    value else special =
                    "ALL"
            }
            Log.d(
                "ShowSelection",
                "onAply filter format: " + special
            )
            val containPrice = type.contains("price")
            if (containPrice) {
                val index = type.indexOf("price")
                var value = filterItemSelected[type[index]]
                if (value != null && !value.equals("", ignoreCase = true)) {
                    val splitSymbol = value.split("₹").toTypedArray()
                    value = splitSymbol[1]
                    if (value.contains("-")) {
                        val split = value.split(" - ").toTypedArray()
                        price1 =
                            split[0]
                        price2 =
                            split[1]
                    } else if (splitSymbol[0].equals("Below ", ignoreCase = true)) {
                        price1 =
                            "0"
                        price2 =
                            value
                    } else {
                        price1 =
                            value
                        price2 =
                            "2500"
                    }
                    Log.d(
                        "ShowSelection",
                        "onAply filter price: " + price1 + " - " +price2
                    )
                } else {
                   price1 = "ALL"
                   price2 = "ALL"
                }
            }
            val containAccessibility = type.contains("accessability")
            if (containAccessibility) {
                val index = type.indexOf("accessability")
                val value = filterItemSelected[type[index]]
                if (value != null && !value.equals("", ignoreCase = true)) {
                    if (value.contains("Wheelchair")) hc =
                        "hc"
                    if (value.contains("Subtitles")) special =
                        "RST"
                } else {
                    hc = "ALL"
                   special =
                        "ALL"
                }
                Log.d("ShowSelection", "onAply filter accessability: $value")
            }
//            ShowSelectionLayout.getMovieShowDetailMSession(
//                movieId,
//                cityId,
//                PCConstants.IntentKey.DEFAULT_PAGE_NUMBER,
//                date_time,
//                mRecyclerView,
//                context,
//                false,
//                "",
//                latitude,
//                longitude
//            )
            if (!selectedFilter(filterItemSelected)) {
                binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
            }
        } else {
            //            appliedFilter.setVisibility(View.GONE)

        }
    }

    private fun selectedFilter(filterItemSelected: HashMap<String?, String?>): Boolean {
        var selected = false
//        for ((key): HashMap.Entry<String, String> in filterItemSelected) {
//            println("selectedFilters---->" + filterItemSelected[key] + "----")
//            if (!filterItemSelected[key].equals("", ignoreCase = true) && !filterItemSelected[key]!!
//                    .contains("ALL")
//            ) {
//                selected = true
//            }
//        }
        return selected
    }


    override fun onReset() {
        binding?.filterFab?.setImageResource(R.drawable.filter_unselect)
//        appliedFilter.setVisibility(View.GONE)
        appliedFilterItem = HashMap()

        authViewModel.bookingTicket(
            preferences.getCityName(),
            intent.getStringExtra("mid").toString(),
            preferences.getLatitudeData(),
            preferences.getLongitudeData(),
            "NA",
            "no",
            "no",
            preferences.getUserId()
        )
    }
}