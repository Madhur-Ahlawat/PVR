package com.net.pvr.ui.seatLayout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.net.pvr.R
import com.net.pvr.databinding.ActivitySeatLayoutBinding
import com.net.pvr.databinding.SeatLayoutDilogBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.response.BookingResponse.Output.Cinema.*
import com.net.pvr.ui.cinemaSession.response.CinemaSessionResponse
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.food.FoodActivity
import com.net.pvr.ui.seatLayout.adapter.CinemaShowsAdapter
import com.net.pvr.ui.seatLayout.adapter.ShowsAdapter
import com.net.pvr.ui.seatLayout.request.ReserveSeatRequest
import com.net.pvr.ui.seatLayout.response.*
import com.net.pvr.ui.seatLayout.viewModel.SeatLayoutViewModel
import com.net.pvr.ui.summery.SummeryActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.AVAILABETIME
import com.net.pvr.utils.Constant.Companion.BOOKING_ID
import com.net.pvr.utils.Constant.Companion.CINEMA_ID
import com.net.pvr.utils.Constant.Companion.EXTANDTIME
import com.net.pvr.utils.Constant.Companion.FOODENABLE
import com.net.pvr.utils.Constant.Companion.SELECTED_SEAT
import com.net.pvr.utils.Constant.Companion.SeatBack
import com.net.pvr.utils.Constant.Companion.TRANSACTION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject


@Suppress("DEPRECATION", "NAME_SHADOWING")
@AndroidEntryPoint
class SeatLayoutActivity : AppCompatActivity(), ShowsAdapter.RecycleViewItemClickListenerCity,
    CinemaShowsAdapter.RecycleViewItemClickListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySeatLayoutBinding? = null
    private val authViewModel: SeatLayoutViewModel by viewModels()
    private var loader: LoaderDialog? = null

    private var selectBuddy = false
    private var hcSeat = false
    private var hcSeat1 = false
    private var caSeat = false
    private var flagHc = false
    private var flagHc1 = false
    private var priceVal = 0.0
    private var hcCount1 = 0
    private var caCount1 = 0
    private var hcCount = 0
    private var caCount = 0

    private var keyData = ""
    private var seatsN: ArrayList<String>? = null
    private var coupleSeat = 0
    private var messageText = ""
    private var sessionId = ""
    private var isDit = false

    private var noOfRowsSmall: ArrayList<SeatResponse.Output.Row>? = null
    private var priceMap: Map<String, SeatResponse.Output.PriceList.Price>? = null
    private var selectedSeats = ArrayList<Seat>()
    private var noOfSeatsSelected = ArrayList<SeatTagData>()
    private var selectedSeatsBox: ArrayList<SeatHC>? = null
    private var selectedSeats1 = ArrayList<SeatHC>()

    //Shows
    private var showsArray = ArrayList<Child.Sw.S>()
    private var selectSeatPriceCode = ArrayList<ReserveSeatRequest.Seat>()

    //Cinema Session
    private var cinemaSessionShows = ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>()
    private var textTermsAndCondition: TextView? = null
    private var tncValue = 1
    private var offerEnable = false

    //Bottom Dialog
    private var dialog: BottomSheetDialog? = null

    //internet Check
    private var broadcastReceiver: BroadcastReceiver? = null

    companion object{
        var position = "0"

    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        sessionId = Constant.SESSION_ID

        manageFunctions()
    }

    @SuppressLint("SetTextI18n")
    private fun manageFunctions() {
        //from Movie
        if (intent.getStringExtra("from") == "cinema") {
            cinemaSessionShows =
                intent.getSerializableExtra("shows") as ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>
            position = intent.getStringExtra("clickPosition").toString()
            printLog("shows---->${cinemaSessionShows}")
            cinemaShows()
        } else {
            position = intent.getStringExtra("clickPosition").toString()
            showsArray = intent.getSerializableExtra("shows") as ArrayList<Child.Sw.S>
            printLog("shows---->${position}")

            shows()
        }


        // manage offer
        if (intent.getStringExtra("skip").toString() == "false") {
            binding?.constraintLayout60?.show()
            binding?.textView202?.text = "(You’ll save ${getString(R.string.currency)}  ${
                intent.getStringExtra("discountPrice").toString()
            })"
            offerEnable = true
        } else {
            binding?.constraintLayout60?.hide()
            offerEnable = false
        }

        //Remove Offer
        binding?.textView203?.setOnClickListener {
            removeBundle()
        }

        //from Shows

        authViewModel.seatLayout(
            CINEMA_ID, sessionId, "", "", "", offerEnable, ""
        )



        //Show  end time
        if (intent.getStringExtra("from") == "cinema") {
            binding?.textView393?.text = cinemaSessionShows[position.toInt()].et
        } else {
            binding?.textView393?.text = showsArray[position.toInt()].et
        }


        seatLayout()
        reserveSeat()
        initTrans()
        movedNext()
        getShimmerData()
    }

    private fun getShimmerData() {
        Constant().getData(binding?.include38?.tvFirstText, binding?.include38?.tvSecondText)
        Constant().getData(binding?.include38?.tvSecondText, null)
    }

    private fun movedNext() {
        // back btn
        binding?.imageView95?.setOnClickListener {
            onBackPressed()
        }

        // Share data
        binding?.imageView96?.setOnClickListener {
            Constant().shareData(this, "", "")
        }

        //Alert Dialog
        binding?.imageView97?.setOnClickListener {

            dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

            //profileDialog
            val inflater = LayoutInflater.from(this)
            val bindingSeat = SeatLayoutDilogBinding.inflate(inflater)
            val behavior: BottomSheetBehavior<FrameLayout>? = dialog?.behavior

            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            dialog?.setContentView(bindingSeat.root)
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog?.window?.setGravity(Gravity.BOTTOM)
            dialog?.show()

            dialog?.window?.setGravity(Gravity.BOTTOM)
            // under line
            bindingSeat.textView304.paintFlags =
                bindingSeat.textView304.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            //click
            bindingSeat.textView304.setOnClickListener {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("from", "more")
                intent.putExtra("title", getString(R.string.terms_condition_text))
                intent.putExtra("getUrl", Constant.privacy)
                startActivity(intent)
            }

            bindingSeat.include23.textView5.text = getString(R.string.okay)

            bindingSeat.include23.textView5.setOnClickListener {
                dialog?.dismiss()
            }
        }

        //internet Check
        broadcastReceiver = NetworkReceiver()
        broadcastIntent()
    }

    //Shows
    private fun shows() {
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView27?.layoutManager = LinearLayoutManager(this)
        val adapter =
            ShowsAdapter(getMovieShows(showsArray), this, this, position.toInt(), binding?.recyclerView27!!)
        binding?.recyclerView27?.layoutManager = gridLayout
        binding?.recyclerView27?.adapter = adapter


    }

    private fun getMovieShows(showsArray: ArrayList<Child.Sw.S>): ArrayList<Child.Sw.S> {

        var list = ArrayList<Child.Sw.S>()
        var st = showsArray[position.toInt()].sid

        for (data in showsArray.indices){
            if (showsArray[data].ss != 0){
                list.add(showsArray[data])
            }
        }
        for (data in list.indices){
            if (list[data].sid == st){
                sessionId = list[data].sid.toString()
                println("itemCount--->$sessionId")

                position = data.toString()
            }
        }


        return list
    }

    //From Cinema
    private fun cinemaShows() {
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView27?.layoutManager = LinearLayoutManager(this)
        val adapter = CinemaShowsAdapter(getCinemaShows(cinemaSessionShows), this, this, position)
        binding?.recyclerView27?.layoutManager = gridLayout
        binding?.recyclerView27?.adapter = adapter

    }

    private fun getCinemaShows(cinemaSessionShows: ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>): ArrayList<CinemaSessionResponse.Child.Mv.Ml.S> {
        var list = ArrayList<CinemaSessionResponse.Child.Mv.Ml.S>()
        var st = cinemaSessionShows[position.toInt()].sid
        for (data in cinemaSessionShows.indices){
            if (cinemaSessionShows[data].ss != 0){
                list.add(cinemaSessionShows[data])
            }
        }
        for (data in list.indices){
            if (list[data].sid == st){
                sessionId = list[data].sid.toString()
                position = data.toString()
            }
        }
        return list
    }


    //SeatLayout
    private fun seatLayout() {
        authViewModel.userResponseLiveData.observe(this) {
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
                                            if (it.data?.code == 12008){
                                                binding?.llRowName?.removeAllViews()
                                                //clear List
                                                selectedSeats.clear()
                                                selectSeatPriceCode.clear()
                                                authViewModel.seatLayout(
                                                    CINEMA_ID, sessionId, "", "", "", offerEnable, ""
                                                )
                                            }else{
                                                dialog?.dismiss()
                                            }
                            },
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

    //reserveSeat
    private fun reserveSeat() {
        authViewModel.reserveSeatResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieverReserveSeatData(it.data.output)
                    } else {
                        selectSeatPriceCode.clear()
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

    private fun retrieverReserveSeatData(output: ReserveSeatResponse.Output) {
        BOOKING_ID = output.bookingid
        SELECTED_SEAT = selectedSeats.size
        Constant.BOOK_TYPE = "BOOKING"

        if (output.fc == "true") {
            FOODENABLE = 0
            startActivity(Intent(this, FoodActivity::class.java))
        } else {
            FOODENABLE = 1
            startActivity(Intent(this, SummeryActivity::class.java))
        }

//        when (output.nf) {
//            "true" -> {
//                FOODENABLE = 0
//                startActivity(Intent(this, FoodActivity::class.java))
//            }
//            "false" -> {
//                FOODENABLE = 0
////                startActivity(Intent(this, FoodActivity::class.java))
//            }
//            else -> {
//                FOODENABLE = 1
//                startActivity(Intent(this, SummeryActivity::class.java))
//            }
//        }
    }

    //initTrans
    private fun initTrans() {
        authViewModel.initTransResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieverInitData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                finish()
                            },
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

    //InitResponse
    private fun retrieverInitData(output: InitResponse.Output) {
        //extandTime
        EXTANDTIME = Constant().convertTime(output.et.toInt())

        //AVAIL TIME
        AVAILABETIME = Constant().convertTime(output.at.toInt())

//        //extandTime
//        EXTANDTIME = Constant().convertTime(1)
//
//        //AVAIL TIME
//        AVAILABETIME = Constant().convertTime(1)

        TRANSACTION_ID = output.transid
        println("SeatTagData--->${selectSeatPriceCode.size}---->${selectedSeats.size}---->${selectedSeatsBox?.size}")

        for (item in selectedSeats) {
            val price = item.priceCode
            val seatId = item.seatBookingId
            selectSeatPriceCode.add(ReserveSeatRequest.Seat(price.toString(), seatId.toString()))
        }

        val reserve = ReserveSeatRequest(CINEMA_ID, selectSeatPriceCode, sessionId, output.transid)
        val gson = Gson()
        val mMineUserEntity = gson.toJson(reserve, ReserveSeatRequest::class.java)
        println("request--->${mMineUserEntity}")
        authViewModel.reserveSeat(mMineUserEntity)

    }

    private fun retrieveData(data: SeatResponse.Output) {

        //shimmer
        binding?.constraintLayout145?.hide()
        //design
        binding?.constraintLayout153?.show()

        if (data.ca_a) {
            binding?.imageView97?.show()
        } else {
            binding?.imageView97?.hide()
        }

        //title
        binding?.textView197?.text = data.mn
        binding?.textView197?.isSelected = true

        //location
        binding?.textView198?.text = data.cn



        if (tncValue == 1) {
            if (data.tnc == "") {
                tncValue = 1
            } else {
                seatTermsDialog()
                textTermsAndCondition?.text = Html.fromHtml(data.tnc.replace("\\|".toRegex(),"<br></br><br></br>"))
                tncValue = 2
            }
        }

        priceMap = data.priceList
        noOfRowsSmall = data.rows
        drawColumn(data.rows)
    }

    private fun drawColumn(noOfRows: List<SeatResponse.Output.Row>) {
        binding?.llSeatLayout?.removeAllViews()
        var areaName = ""
        for (i in noOfRows.indices) {
            val row: SeatResponse.Output.Row = noOfRows[i]
            val noSeats: List<SeatResponse.Output.Row.S> = row.s
            if (noSeats.isNotEmpty()) {
                //draw extras space for row name
                addRowName(row.n, false)
                //Draw seats ============
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = layoutParams
                binding?.llSeatLayout?.addView(linearLayout)
                drawRow(linearLayout, noSeats, areaName, i)
            } else {

                //draw extras space for row name
                addRowName("", true)
                //update area
                areaName = row.n

                //Draw Area============
                val rlLayout = RelativeLayout(this)
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, Constant().convertDpToPixel(50F, this)
                )
                rlLayout.layoutParams = layoutParams
                try {
                    val colorCodes: String = if (row.c.contains("#")) row.c else "#" + row.c
                    rlLayout.setBackgroundColor(Color.parseColor(colorCodes))

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val centerLayout = LinearLayout(this)
                val centerLayoutParameter = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                centerLayoutParameter.addRule(RelativeLayout.CENTER_IN_PARENT)
                centerLayout.gravity = Gravity.CENTER_VERTICAL
                centerLayout.orientation = LinearLayout.HORIZONTAL
                centerLayout.layoutParams = centerLayoutParameter
                val padding: Int = Constant().convertDpToPixel(2F, this)
                val layoutParameter1 = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val textView = TextView(this)
                textView.layoutParams = layoutParameter1
                textView.gravity = Gravity.CENTER
                textView.text = row.n
                textView.setPadding(padding, 0, padding, 0)
                textView.setTextAppearance(this, R.style.H1Size)
                centerLayout.addView(textView)
                rlLayout.addView(centerLayout)
                binding?.llSeatLayout?.addView(rlLayout)
            }
        }
    }

    private fun seatTermsDialog() {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.seat_t_c_dialog_layout)
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        val btnName = dialog.findViewById<TextView>(R.id.textView5)
        textTermsAndCondition = dialog.findViewById(R.id.textTermsAndCondition)
        btnName?.text = getString(R.string.accept)

        btnName?.setOnClickListener {
            dialog.dismiss()
        }


    }

    private fun drawRow(
        llDrawRow: LinearLayout,
        noSeats: List<SeatResponse.Output.Row.S>,
        areaName: String,
        num: Int
    ) {
        for (i in noSeats.indices) {
            val seat: SeatResponse.Output.Row.S = noSeats[i]
            if (seat.b != "") {
                val seatView = TextView(this)
                seatView.setBackgroundColor(Color.TRANSPARENT)
                seatView.gravity = Gravity.CENTER
                seatView.textSize = 10f
                seatView.id = i
                var layoutParams: LinearLayout.LayoutParams
                if (!isDit) layoutParams = LinearLayout.LayoutParams(
                    Constant().convertDpToPixel(20F, this), Constant().convertDpToPixel(20F, this)
                ) else {
                    layoutParams =
                        if (seat.s == Constant.BIKE || seat.s == Constant.BIKE_SEAT_BOOKED || seat.s == Constant.SEAT_SELECTED_BIKE) {
                            seatView.setPadding(0, Constant().convertDpToPixel(8F, this), 0, 0)
                            LinearLayout.LayoutParams(
                                Constant().convertDpToPixel(20F, this),
                                Constant().convertDpToPixel(30F, this)
                            )
                        } else {
                            seatView.setPadding(0, Constant().convertDpToPixel(8F, this), 0, 0)
                            LinearLayout.LayoutParams(
                                Constant().convertDpToPixel(20F, this),
                                Constant().convertDpToPixel(40F, this)
                            )
                        }
                }
                val margin: Int = Constant().convertDpToPixel(4F, this)
                layoutParams.setMargins(margin, margin, margin, margin)
                seatView.layoutParams = layoutParams
                if (seat.s == Constant.SEAT_AVAILABEL) {
                    when (seat.st) {
                        1 -> {
                            hcCount1 += 1
                            hcSeat = false
                            hcSeat1 = true
                            flagHc1 = true
                            seatView.text = ""
                            seatView.setBackgroundResource(R.drawable.ic_hcseat)
                        }
                        2 -> {
                            caCount1 += 1
                            seatView.text = ""
                            seatView.setBackgroundResource(R.drawable.ic_camp)
                        }
                        3 -> {
                            seatView.text = ""
                            seatView.setBackgroundResource(R.drawable.buddy)
                        }
                        else -> {
                            val typeface = resources.getFont(R.font.sf_pro_text_medium)
                            seatView.typeface = typeface
                            seatView.setBackgroundResource(R.drawable.ic_vacant)
                            seatView.text = seat.sn.replace("[^\\d.]".toRegex(), "")
                        }
                    }
                    seatView.setTextColor(
                        ContextCompat.getColor(
                            this, R.color.black_with_fifteen_opacity
                        )
                    )
                } else if (seat.s == Constant.SEAT_SELECTED) {

                    //Add selected seat to list
                    seatView.setTextColor(ContextCompat.getColor(this, R.color.black))
                    val seatTagData = SeatTagData()
                    seatTagData.b = seat.b
//                    seatTagData?.c=seat.c
                    seatTagData.s = seat.s
                    seatTagData.sn = seat.sn
                    seatTagData.st = seat.st
                    seatTagData.area = areaName
                    addSelectedSeats(seatTagData, seatView, num, i)

                    seatView.setBackgroundResource(R.drawable.ic_selected)
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_selected_car)
                } else if (seat.s == Constant.SEAT_BOOKED) {
                    if (seatsN != null && seatsN!!.size > 0 && seatsN!!.contains(seat.sn)) {
                        when (seat.st) {
                            1 -> {
                                if (!hcSeat1) hcSeat = true
                                seatView.setBackgroundResource(R.drawable.ic_hco_green)
                            }
                            2 -> {
                                seatView.setBackgroundResource(R.drawable.ic_cao_green)
                            }
                            else -> {
                                seatView.setTextColor(ContextCompat.getColor(this, R.color.black))
                                seatView.setBackgroundResource(R.drawable.ic_previous_selected)
                            }
                        }
                    } else {
                        when (seat.st) {
                            1 -> {
                                if (!hcSeat1) hcSeat = true
                                seatView.setBackgroundResource(R.drawable.ic_hco)
                            }
                            2 -> {
                                seatView.setBackgroundResource(R.drawable.ic_cao)
                            }
                            else -> {
                                seatView.setTextColor(ContextCompat.getColor(this, R.color.white))
                                seatView.setBackgroundResource(R.drawable.ic_fill)
                            }
                        }
                    }
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_occupied_car)
                } else if (seat.s == Constant.SEAT_SOCIAL_DISTANCING) {
                    seatView.setBackgroundResource(R.drawable.ic_social_distancing)
                } else if (seat.s == Constant.HATCHBACK) {
                    seatView.text = seat.sn.replace("[^\\d.]", "")
                    seatView.setTextColor(ContextCompat.getColor(this, R.color.red_data))
                    seatView.setBackgroundResource(R.drawable.ic_red_sedan)
                } else if (seat.s == Constant.SUV) {
                    seatView.text = seat.sn.replace("[^\\d.]", "")
                    seatView.setTextColor(ContextCompat.getColor(this, R.color.blue_data))
                    seatView.setBackgroundResource(R.drawable.ic_blue_suv)
                } else if (seat.s == Constant.BIKE) {
                    // seatView.setText(seat.getSn().replaceAll("[^\\d.]", ""));
                    //seatView.setTextColor(getResources().getColor(R.color.blue_data));
                    seatView.setBackgroundResource(R.drawable.ic_bike_normal)
                } else if (seat.s == Constant.BIKE_SEAT_BOOKED) {
                    // seatView.setText(seat.getSn().replaceAll("[^\\d.]", ""));
                    //seatView.setTextColor(getResources().getColor(R.color.blue_data));
                    seatView.setBackgroundResource(R.drawable.ic_bike_booked)
                } else if (seat.s == Constant.SEAT_SELECTED_HATCHBACK || seat.s == Constant.SEAT_SELECTED_SUV) {

                    //Add selected seat to list
                    seatView.setTextColor(ContextCompat.getColor(this, R.color.black))
                    val seatTagData = SeatTagData()
                    seatTagData.b = seat.b
                    seatTagData.c = seat.c
                    seatTagData.s = seat.s
                    seatTagData.sn = seat.sn
                    seatTagData.st = seat.st
                    seatTagData.area = areaName
                    addSelectedSeats(seatTagData, seatView, num, i)


                    //set des for selected seat
                    seatView.setBackgroundResource(R.drawable.ic_selected)
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_selected_car)
                } else if (seat.s == Constant.SEAT_SELECTED_BIKE) {

                    //Add selected seat to list
                    seatView.setTextColor(ContextCompat.getColor(this, R.color.black))
                    val seatTagData = SeatTagData()
                    seatTagData.b = seat.b
                    seatTagData.c = seat.c
                    seatTagData.s = seat.s
                    seatTagData.sn = seat.sn
                    seatTagData.st = seat.st
                    seatTagData.area = areaName
                    addSelectedSeats(seatTagData, seatView, num, i)


                    //set des for selected seat
                    seatView.setBackgroundResource(R.drawable.ic_selected)
                    seatView.text = ""
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_bike_selected)
                } else {
                    seatView.setBackgroundColor(Color.TRANSPARENT)
                }
                //create tag for seat partial_layout
                val seatTagData = SeatTagData()
                seatTagData.b = seat.b
                seatTagData.c = seat.c
                seatTagData.s = seat.s
                seatTagData.sn = seat.sn
                seatTagData.area = areaName
                seatTagData.hc = seat.hc
                seatTagData.bu = seat.bu
                seatTagData.st = seat.st
                seatTagData.cos = seat.cos
                seatView.tag = seatTagData

                setClick(seatView, num, i)
                llDrawRow.addView(seatView)
            } else {
                val seatView = TextView(this)
                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    Constant().convertDpToPixel(20F, this), Constant().convertDpToPixel(20F, this)
                )
                seatView.layoutParams = layoutParams
                val margin: Int = Constant().convertDpToPixel(2F, this)
                layoutParams.setMargins(margin, margin, margin, margin)
                seatView.setBackgroundColor(Color.TRANSPARENT)
                llDrawRow.addView(seatView)
            }
        }
    }

    private fun addRowName(rowName: String, space: Boolean) {
        //  System.out.println("rowName======"+rowName+"===="+space);
        if (space) {
            val txtRowName = TextView(this)
            txtRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            val layoutParams: LinearLayout.LayoutParams = if (isDit) LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Constant().convertDpToPixel(40F, this)
            ) else LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Constant().convertDpToPixel(45F, this)
            )
            txtRowName.gravity = Gravity.CENTER
            val margin: Int = Constant().convertDpToPixel(2F, this)
            layoutParams.setMargins(0, Constant().convertDpToPixel(2F, this), 0, margin)
            txtRowName.layoutParams = layoutParams
            binding?.llRowName?.addView(txtRowName)
        } else {

            //Area partial_layout design ==================
            val txtRowName = TextView(this)
            txtRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            txtRowName.textSize = Color.parseColor("#767373").toFloat()
            val layoutParams: LinearLayout.LayoutParams = if (isDit) LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Constant().convertDpToPixel(40F, this)
            ) else LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Constant().convertDpToPixel(20F, this)
            )
            val margin: Int = Constant().convertDpToPixel(4F, this)
            txtRowName.gravity = Gravity.CENTER
            layoutParams.setMargins(0, Constant().convertDpToPixel(4F, this), 0, margin)
            txtRowName.layoutParams = layoutParams
            txtRowName.text = rowName
            binding?.llRowName?.addView(txtRowName)
        }
    }

    private fun setClick(seatView: TextView, num1: Int, num2: Int) {
        seatView.setOnClickListener {
            try {
                val seat = seatView.tag as SeatTagData

                if (seat.s == Constant.SEAT_AVAILABEL) {
                    if (seat.st == 1) {
                        printLog("printSeat--->${seat.st}")

                        buttonClicked(seatView, num1, num2)
                    } else {
                        if (seat.st == 2) {
                            hcSeat = caCount1 > hcCount1
                        }
                        if (seat.st == 2 && !hcSeat && caCount >= hcCount) {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                "Companion seat is only available with a wheelchair seat.",
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()

                        } else {
                            if (flagHc1) hcSeat = false
                            if (seat.cos && coupleSeat == 0) {
                                coupleSeat = 1
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "These are couple recliners.",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()

                            }
                            println("select_buddy=========$selectBuddy")
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            } else {
                                if (selectedSeats.size < 10) {
                                    printLog("EnterInSeat--->${selectedSeats.size}")
                                    if (selectBuddy && seat.bu) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            messageText,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {},
                                                negativeClick = {})
                                            dialog.show()
                                        } else {
                                            seat.s = Constant.SEAT_SELECTED
                                            printLog("EnterInSeat-- seat.s->${seat.st}size--->${noOfRowsSmall?.size}")

                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                                                Constant.SEAT_SELECTED
                                            when (seat.st) {
                                                1 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                }
                                                2 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                }
                                                3 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                }
                                                else -> {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(
                                                        ContextCompat.getColor(
                                                            this, R.color.black
                                                        )
                                                    )
                                                }
                                            }
                                            addSelectedSeats(seat, seatView, num1, num2)
                                        }
                                    }
                                } else {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        getString(R.string.max_seat_msz1),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()

                                }
                            }
                        }
                    }

                } else if (seat.s == Constant.SEAT_SELECTED) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this, seat, seatView)
                    } else {
                        seat.s = Constant.SEAT_AVAILABEL
                        when (seat.st) {
                            1 -> {
                                hcSeat = false
                            }
                            2 -> {
                                caSeat = false
                            }
                            3 -> {
                                selectBuddy = false
                            }
                        }
                        when (seat.st) {
                            1 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            }
                            2 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            }
                            3 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            }
                            else -> {
                                val typeface = resources.getFont(R.font.sf_pro_text_medium)
                                seatView.typeface = typeface
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(
                                    ContextCompat.getColor(
                                        this, R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                            Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                    }
                } else if (seat.s == Constant.HATCHBACK) {
                    if (seat.st == 1) {
                        buttonClicked(seatView, num1, num2)
                    } else {
                        if (seat.st == 2) {
                            hcSeat = caCount1 > hcCount1
                        }
                        if (seat.st == 2 && !hcSeat && caCount >= hcCount) {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                "Companion seat is only available with a wheelchair seat.",
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()

                            if (flagHc1) hcSeat = false
                            if (seat.cos && coupleSeat == 0) {
                                coupleSeat = 1
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "These are couple recliners.",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()

                            }
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()

                            } else {
                                if (selectedSeats.size < 10) {
                                    if (selectBuddy && seat.bu) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            messageText,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()

                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {

                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {},
                                                negativeClick = {})
                                            dialog.show()
                                        } else {
                                            seat.s = Constant.SEAT_SELECTED_HATCHBACK
                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                                                Constant.SEAT_SELECTED
                                            when (seat.st) {
                                                1 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                }
                                                2 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                }
                                                3 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                }
                                                else -> {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(
                                                        ContextCompat.getColor(
                                                            this, R.color.black
                                                        )
                                                    )
                                                }
                                            }
                                            addSelectedSeats(seat, seatView, num1, num2)
                                            if (isDit) seatView.setBackgroundResource(R.drawable.ic_selected_car)
                                        }
                                    }
                                } else {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        getString(R.string.max_seat_msz),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()
                                }
                            }
                        }
                    }
                } else if (seat.s == Constant.BIKE) {
                    if (seat.st == 1) {
                        buttonClicked(seatView, num1, num2)
                    } else {
                        if (seat.st == 2) {
                            hcSeat = caCount1 > hcCount1
                        }
                        if (seat.st == 2 && !hcSeat && caCount >= hcCount) {

                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                "Companion seat is only available with a wheelchair seat.",
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        } else {
                            if (flagHc1) hcSeat = false
                            if (seat.cos && coupleSeat == 0) {
                                coupleSeat = 1
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "These are couple recliners.",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()

                            }
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            } else {
                                if (selectedSeats.size < 10) {
                                    if (selectBuddy && seat.bu) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            messageText,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {},
                                                negativeClick = {})
                                            dialog.show()
                                        } else {
                                            seat.s = Constant.SEAT_SELECTED_BIKE
                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                                                Constant.SEAT_SELECTED
                                            when (seat.st) {
                                                1 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                }
                                                2 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                }
                                                3 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                }
                                                else -> {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(
                                                        ContextCompat.getColor(
                                                            this, R.color.black
                                                        )
                                                    )
                                                }
                                            }
                                            seatView.text = ""
                                            addSelectedSeats(seat, seatView, num1, num2)
                                            if (isDit) seatView.setBackgroundResource(R.drawable.ic_bike_selected)
                                        }
                                    }
                                } else {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        getString(R.string.max_seat_msz),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()
                                }
                            }
                        }
                    }
                } else if (seat.s == Constant.SUV) {
                    if (seat.st == 1) {
                        buttonClicked(seatView, num1, num2)
                    } else {
                        if (seat.st == 2) {
                            hcSeat = caCount1 > hcCount1
                        }
                        if (seat.st == 2 && !hcSeat && caCount >= hcCount) {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                "Companion seat is only available with a wheelchair seat.",
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()

                        } else {
                            if (flagHc1) hcSeat = false
                            if (seat.cos && coupleSeat == 0) {
                                coupleSeat = 1
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    "These are couple recliners.",
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()

                            }
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {},
                                    negativeClick = {})
                                dialog.show()
                            } else {
                                if (selectedSeats.size < 10) {
                                    if (selectBuddy && seat.bu) {
                                        val dialog = OptionDialog(this,
                                            R.mipmap.ic_launcher,
                                            R.string.app_name,
                                            messageText,
                                            positiveBtnText = R.string.ok,
                                            negativeBtnText = R.string.no,
                                            positiveClick = {},
                                            negativeClick = {})
                                        dialog.show()
                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {},
                                                negativeClick = {})
                                            dialog.show()

                                        } else {
                                            seat.s = Constant.SEAT_SELECTED_SUV
                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                                                Constant.SEAT_SELECTED
                                            when (seat.st) {
                                                1 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                }
                                                2 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                }
                                                3 -> {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                }
                                                else -> {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(
                                                        ContextCompat.getColor(
                                                            this, R.color.black
                                                        )
                                                    )
                                                }
                                            }
                                            addSelectedSeats(seat, seatView, num1, num2)
                                            if (isDit) seatView.setBackgroundResource(R.drawable.ic_selected_car)
                                        }
                                    }
                                } else {

                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        getString(R.string.max_seat_msz),
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {},
                                        negativeClick = {})
                                    dialog.show()

                                }
                            }
                        }
                    }
                } else if (seat.s == Constant.SEAT_SELECTED_HATCHBACK) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this, seat, seatView)
                    } else {
                        seat.s = Constant.HATCHBACK
                        when (seat.st) {
                            1 -> {
                                hcSeat = false
                            }
                            2 -> {
                                caSeat = false
                            }
                            3 -> {
                                selectBuddy = false
                            }
                        }
                        when (seat.st) {
                            1 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            }
                            2 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            }
                            3 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            }
                            else -> {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(
                                    ContextCompat.getColor(
                                        this, R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                            Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                        seatView.setBackgroundResource(R.drawable.ic_red_sedan)
                        seatView.setTextColor(ContextCompat.getColor(this, R.color.red_data))
                    }
                } else if (seat.s == Constant.SEAT_SELECTED_BIKE) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this, seat, seatView)
                    } else {
                        seat.s = Constant.BIKE
                        when (seat.st) {
                            1 -> {
                                hcSeat = false
                            }
                            2 -> {
                                caSeat = false
                            }
                            3 -> {
                                selectBuddy = false
                            }
                        }
                        when (seat.st) {
                            1 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            }
                            2 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            }
                            3 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            }
                            else -> {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(
                                    ContextCompat.getColor(
                                        this, R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                            Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                        seatView.text = ""
                        seatView.setBackgroundResource(R.drawable.ic_bike_normal)
                    }
                } else if (seat.s == Constant.SEAT_SELECTED_SUV) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this, seat, seatView)
                    } else {
                        seat.s = Constant.SUV
                        when (seat.st) {
                            1 -> {
                                hcSeat = false
                            }
                            2 -> {
                                caSeat = false
                            }
                            3 -> {
                                selectBuddy = false
                            }
                        }
                        when (seat.st) {
                            1 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            }
                            2 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            }
                            3 -> {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            }
                            else -> {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(
                                    ContextCompat.getColor(
                                        this, R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                            Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                        seatView.setBackgroundResource(R.drawable.ic_blue_suv)
                        seatView.setTextColor(ContextCompat.getColor(this, R.color.blue_data))
                    }
                }
            } catch (e: Exception) {
                printLog("exception---${e.message}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun removeSelectedSeats(seat: SeatTagData) {
        for (i in selectedSeats.indices) {
            if (seat.b.equals(selectedSeats[i].seatBookingId)) {
                if (seat.st == 1) {
                    hcCount--
                    hcCount1 += 1
                } else if (seat.st == 2) {
                    caCount--
                    caCount1 += 1
                }
                noOfSeatsSelected.removeAt(i)
                selectedSeats.removeAt(i)
                selectedSeatsBox?.removeAt(i)

                break
            }
        }
        if (noOfSeatsSelected.size == 0) {
            binding?.textView195?.hide()
            binding?.textView196?.hide()
            binding?.textView200?.show()

            binding?.textView195?.isClickable = false
            binding?.textView196?.isClickable = false
            binding?.constraintLayout56?.isClickable = false

            binding?.constraintLayout56?.setBackgroundResource(R.drawable.grey_seat_curve)
//            binding?.constraintLayout56?.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.unSelectBg
//                )
//            )

            if (!isDit) binding?.textView195?.text = "No Seats Selected"
            else binding?.textView195?.text = "No Vehicle Slots Selected"
        } else {
            binding?.textView195?.show()
            binding?.textView196?.show()
            binding?.textView200?.hide()

            binding?.textView195?.isClickable = true
            binding?.textView196?.isClickable = true
            binding?.constraintLayout56?.isClickable = true

            binding?.textView196?.setOnClickListener {
                authViewModel.initTransSeat(CINEMA_ID, sessionId)
            }
            binding?.constraintLayout56?.setBackgroundResource(R.drawable.btn_yellow_curve)
//            binding?.constraintLayout56?.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.yellow
//                )
//            )
        }

        calculatePrice()
    }

    @SuppressLint("SetTextI18n")
    private fun calculatePrice() {
        binding?.textView195?.text = ""
        if (noOfSeatsSelected.size > 0) {
            binding?.textView195?.isClickable = true
            binding?.textView195?.setTextColor(Color.parseColor("#000000"))

            binding?.textView196?.text = ""
            if (noOfSeatsSelected.size == 1) {
                binding?.textView196?.text = noOfSeatsSelected.size.toString() + " Seat Selected"
                if (isDit) binding?.textView196?.text =
                    noOfSeatsSelected.size.toString() + " Vehicle Slot Selected"
            } else {
                binding?.textView196?.text = noOfSeatsSelected.size.toString() + " Seats Selected"
                if (isDit) binding?.textView196?.text =
                    noOfSeatsSelected.size.toString() + " Vehicle Slots Selected"
            }
            binding?.textView195?.show()
            binding?.textView196?.show()
            binding?.textView200?.hide()

            binding?.textView196?.setOnClickListener {
                authViewModel.initTransSeat(CINEMA_ID, sessionId)
            }
            binding?.textView195?.isClickable = true
            binding?.textView196?.isClickable = true
            binding?.constraintLayout56?.isClickable = true
            binding?.constraintLayout56?.setBackgroundResource(R.drawable.btn_yellow_curve)

//            binding?.constraintLayout56?.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.yellow
//                )
//            )
        } else {
            binding?.textView195?.hide()
            binding?.textView196?.hide()
            binding?.textView200?.show()

            binding?.textView195?.isClickable = false
            binding?.textView196?.isClickable = false
            binding?.constraintLayout56?.isClickable = false
            binding?.constraintLayout56?.setBackgroundResource(R.drawable.grey_seat_curve)

//            binding?.constraintLayout56?.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.unSelectBg
//                )
//            )
            if (!isDit) binding?.textView195?.text =
                "No Seats Selected" else binding?.textView196?.text = "No Vehicle Slots Selected"
        }
        var totalPrice = 0f
        val selectSeat = ArrayList<Spannable>()
        var bigDecimal = BigDecimal(0)
        for (i in noOfSeatsSelected.indices) {
            try {
                val seatTagData = noOfSeatsSelected[i]
                val price = priceMap!![seatTagData.c]
                var wordToSpan: Spannable
                var seatNo: String
                when (seatTagData.st) {
                    1 -> {
                        seatNo = if (binding?.textView195?.text.toString()
                                .equals("", ignoreCase = true)
                        ) {
                            "\uF101 " + seatTagData.sn
                        } else {
                            "," + "\uF101 " + seatTagData.sn
                        }
                        wordToSpan = SpannableString(seatNo)
                        wordToSpan.setSpan(
                            ForegroundColorSpan(Color.parseColor("#800080")),
                            0,
                            seatNo.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding?.textView195?.text = noOfSeatsSelected.size.toString() + " Seats |"
//                        binding?.textView195?.append(wordToSpan)
                    }
                    2 -> {
                        seatNo = if (binding?.textView195?.text.toString()
                                .equals("", ignoreCase = true)
                        ) {
                            "\uF102 " + seatTagData.sn
                        } else {
                            "," + "\uF102 " + seatTagData.sn
                        }
                        wordToSpan = SpannableString(seatNo)
                        wordToSpan.setSpan(
                            ForegroundColorSpan(Color.parseColor("#800080")),
                            0,
                            seatNo.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding?.textView195?.text = noOfSeatsSelected.size.toString() + " Seats |"
//                        binding?.textView195?.append(wordToSpan)
                    }
                    else -> {
                        seatNo = if (binding?.textView195?.text.toString()
                                .equals("", ignoreCase = true)
                        ) {
                            seatTagData.sn.toString()
                        } else {
                            "," + seatTagData.sn
                        }
                        wordToSpan = SpannableString(seatNo)
                        wordToSpan.setSpan(
                            ForegroundColorSpan(Color.parseColor("#333333")),
                            0,
                            seatNo.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        binding?.textView195?.text = noOfSeatsSelected.size.toString() + " Seats |"
//                        binding?.textView195?.append(wordToSpan)
                    }
                }
                selectSeat.add(wordToSpan)
//                binding?.txtArea?.setText(price.)
                bigDecimal = BigDecimal(totalPrice.toString()).add(BigDecimal(price?.price))
                totalPrice = bigDecimal.toFloat()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding?.textView196?.text =
            "PAY " + getString(R.string.currency) + " " + Constant().removeTrailingZeroFormatter(
                bigDecimal.toFloat()
            )
//        binding?.seatCounterLayout?.seatCounter?.text = selectSeat.size.toString()
        priceVal = bigDecimal.toFloat().toDouble()
    }

    private fun addSelectedSeats(seat: SeatTagData, seatView: TextView, num1: Int, num2: Int) {
        when (seat.st) {
            1 -> {
                hcSeat = true
                hcCount += 1
                hcCount1--
            }
            2 -> {
                caSeat = true
                caCount += 1
                caCount1--
            }
            3 -> {
                selectBuddy = true
            }
        }
        try {
            keyData = seat.c as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val selectedSeat = Seat()
        val selectedSeat1 = SeatHC()
        selectedSeat.seatBookingId = seat.b
        val price = priceMap!![seat.c]
        selectedSeat.priceCode = price?.priceCode
        noOfSeatsSelected.add(seat)
        selectedSeats.add(selectedSeat)
        selectedSeat1.num1 = num1
        selectedSeat1.num2 = num2
        selectedSeat1.seatView = seatView
        selectedSeat1.st = seat.st
        selectedSeat1.priceCode = price?.priceCode
        selectedSeat1.seatBookingId = seat.b
        selectedSeats1.add(selectedSeat1)
        selectedSeatsBox?.add(selectedSeat1)
        calculatePrice()
        printLog("SeatClick--->${selectedSeat1}")

    }

    @SuppressLint("SetTextI18n")
    private fun buttonClicked(seatView: TextView, num1: Int, num2: Int) {
        // custom dialog
        val messagePcTextView: TextView
        val titleText: TextView
        val cancel: TextView
        val delete: TextView
        val icon: ImageView
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.ticket_cancel)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setTitle("")
        val seat = seatView.tag as SeatTagData
        messagePcTextView = dialog.findViewById<View>(R.id.message) as TextView
        titleText = dialog.findViewById<View>(R.id.titleText) as TextView
        icon = dialog.findViewById<View>(R.id.icon) as ImageView
        icon.visibility = View.VISIBLE
        delete = dialog.findViewById<View>(R.id.no) as TextView
        if (seat.st == 1) {
            titleText.text = "Are you sure you want to book a Wheelchair-friendly seat?"
            messagePcTextView.text =
                "Please do not book it if you are not on a wheelchair. You may be requested to move to accommodate."
            titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            icon.setImageResource(R.drawable.hc_icon)
        } else {
            titleText.text = "Are you sure you want to book a Companion seat?"
            messagePcTextView.text =
                "You have selected a Wheelchair Companion Seat. You may be requested to move to accommodate."
            titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            icon.setImageResource(R.drawable.ic_caf)
        }
        delete.text = "Change Seat"
        delete.setOnClickListener { dialog.dismiss() }
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.text = "Confirm Seat"
        cancel.setOnClickListener {
            hcSeat = true
            if (seat.cos && coupleSeat == 0) {
                coupleSeat = 1
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "These are couple recliners.",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            }
            printLog("select_buddy=1==$selectBuddy")
            if (selectBuddy) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    messageText,
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {},
                    negativeClick = {})
                dialog.show()
            } else {
                if (selectedSeats.size < 10) {
                    if (selectBuddy && seat.bu) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            messageText,
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                    } else {
                        if (selectedSeats.size > 0 && seat.bu) {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                messageText,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {},
                                negativeClick = {})
                            dialog.show()
                        } else {
                            seat.s = Constant.SEAT_SELECTED
                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2].s =
                                Constant.SEAT_SELECTED
                            when (seat.st) {
                                1 -> {
                                    seatView.text = ""
                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                }
                                2 -> {
                                    seatView.text = ""
                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                }
                                3 -> {
                                    seatView.text = ""
                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                }
                                else -> {
                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                    seatView.setTextColor(
                                        ContextCompat.getColor(
                                            this, R.color.black
                                        )
                                    )
                                }
                            }
                            addSelectedSeats(seat, seatView, num1, num2)
                        }
                    }
                } else {

                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(R.string.max_seat_msz),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun removeHC(context: Activity, seat: SeatTagData, seatView: TextView) {
        // custom dialog
        val messagePcTextView: TextView
        val titleText: TextView
        val cancel: TextView
        val delete: TextView
        val icon: ImageView
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.ticket_cancel)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setTitle("")
        messagePcTextView = dialog.findViewById<View>(R.id.message) as TextView
        titleText = dialog.findViewById<View>(R.id.titleText) as TextView
        icon = dialog.findViewById<View>(R.id.icon) as ImageView
        icon.visibility = View.VISIBLE
        delete = dialog.findViewById<View>(R.id.no) as TextView
        titleText.text = "Are you sure you want to remove a Wheelchair and Companion seat?"
        messagePcTextView.text =
            "If you will remove wheelchair then corresponding Companion seat will be remove also."
        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        icon.setImageResource(R.drawable.hc_icon)
        delete.text = "No"
        delete.setOnClickListener { dialog.dismiss() }
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.setOnClickListener {

            seat.s = Constant.SEAT_AVAILABEL

            when (seat.st) {
                1 -> {
                    hcSeat = false
                }
                2 -> {
                    caSeat = false
                }
                3 -> {
                    selectBuddy = false
                }
            }

            when (seat.st) {
                1 -> {
                    seatView.text = ""
                    seatView.setBackgroundResource(R.drawable.ic_hcseat)
                }
                2 -> {
                    seatView.text = ""
                    seatView.setBackgroundResource(R.drawable.ic_camp)
                }
                3 -> {
                    seatView.text = ""
                    seatView.setBackgroundResource(R.drawable.buddy)
                }
                else -> {
                    seatView.setBackgroundResource(R.drawable.ic_vacant)
                    seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                }
            }

            removeSelectedSeats(seat)

            println("selectedSeats1--->" + selectedSeats1.size + selectedSeats1)
            for (data in selectedSeats1) {
                flagHc = true
                if (data.st == 1 || data.st == 2) {
                    val seat = data.seatView.tag as SeatTagData
                    seat.s = Constant.SEAT_AVAILABEL
                    if (seat.st == 1) {
                        hcSeat = false
                    } else if (seat.st == 2) {
                        caSeat = false
                    } else if (seat.st == 3) {
                        selectBuddy = false
                    }
                    when (seat.st) {
                        1 -> {
                            data.seatView.text = ""
                            data.seatView.setBackgroundResource(R.drawable.ic_hcseat)
                        }
                        2 -> {
                            data.seatView.text = ""
                            data.seatView.setBackgroundResource(R.drawable.ic_camp)
                        }
                        3 -> {
                            data.seatView.text = ""
                            data.seatView.setBackgroundResource(R.drawable.buddy)
                        }
                        else -> {
                            data.seatView.setBackgroundResource(R.drawable.ic_vacant)
                            data.seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                        }
                    }

                    removeSelectedSeats(seat)
                }
            }
            flagHc = false
            dialog.dismiss()
        }
        cancel.text = "YES"
        dialog.show()
    }
@SuppressLint("SetTextI18n")
    private fun removeBundle() {
        // custom dialog
        val messagePcTextView: TextView
        val titleText: TextView
        val cancel: TextView
        val delete: TextView
        val icon: ImageView
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.ticket_cancel)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val behavior: BottomSheetBehavior<FrameLayout> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setTitle("")
        messagePcTextView = dialog.findViewById<View>(R.id.message) as TextView
        titleText = dialog.findViewById<View>(R.id.titleText) as TextView
        icon = dialog.findViewById<View>(R.id.icon) as ImageView
        icon.visibility = View.VISIBLE
        delete = dialog.findViewById<View>(R.id.no) as TextView
        titleText.text = "Are you sure you want to remove the offer?"
        messagePcTextView.text =
            "This is the first offer of its kind offered\nby a cinema, exclusively on PVR."
        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        icon.setImageResource(R.drawable.ic_caution_shape)
        delete.text = "Cancel"
        delete.setOnClickListener { dialog.dismiss() }
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.text = "Remove Offer"
        cancel.setOnClickListener {

            binding?.llRowName?.removeAllViews()
            selectedSeats.clear()
            selectSeatPriceCode.clear()
            noOfSeatsSelected.clear()
            calculatePrice()
            binding?.constraintLayout60?.hide()
            offerEnable = false
            authViewModel.seatLayout(
                CINEMA_ID, Constant.SESSION_ID, "", "", "", offerEnable, ""
            )

            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showsClick(comingSoonItem: Int, itemView: View, position: Int) {
        //Show  end time
        if (intent.getStringExtra("from") == "cinema") {
            binding?.textView393?.text = cinemaSessionShows[position].et
        } else {
            binding?.textView393?.text = showsArray[position].et
        }

        sessionId = comingSoonItem.toString()
        binding?.llRowName?.removeAllViews()
        //clear List
        selectedSeats.clear()
        selectSeatPriceCode.clear()
        noOfSeatsSelected.clear()
        calculatePrice()
        authViewModel.seatLayout(
            CINEMA_ID, sessionId, "", "", "", offerEnable, ""
        )

    }

    override fun cinemaShowsClick(comingSoonItem: Int, itemView: View, position: Int) {
        //Show  end time
        if (intent.getStringExtra("from") == "cinema") {
            binding?.textView393?.text = cinemaSessionShows[position].et
        } else {
            binding?.textView393?.text = showsArray[position].et
        }

        sessionId = comingSoonItem.toString()
        binding?.llRowName?.removeAllViews()
        //clear List
        selectedSeats.clear()
        selectSeatPriceCode.clear()
        noOfSeatsSelected.clear()
        calculatePrice()
        authViewModel.seatLayout(
            CINEMA_ID, sessionId, "", "", "", offerEnable, ""
        )
        Constant.focusOnView(itemView, binding?.recyclerView27!!)
    }

    //Internet Check
    private fun broadcastIntent() {
        registerReceiver(
            broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    //refresh seat
    override fun onResume() {
        super.onResume()
        if (SeatBack == 1){
            binding?.llRowName?.removeAllViews()
            //clear List
            selectedSeats.clear()
            selectSeatPriceCode.clear()
            noOfSeatsSelected.clear()
            calculatePrice()
            authViewModel.seatLayout(
                CINEMA_ID, sessionId, "", "", "", offerEnable, ""
            )
        }

    }
}