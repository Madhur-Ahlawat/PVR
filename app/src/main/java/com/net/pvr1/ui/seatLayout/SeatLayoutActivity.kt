package com.net.pvr1.ui.seatLayout

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySeatLayoutBinding
import com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.*
import com.net.pvr1.ui.bookingSession.response.BookingResponse.Output.Cinema.*
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.food.FoodActivity
import com.net.pvr1.ui.seatLayout.adapter.ShowsAdapter
import com.net.pvr1.ui.seatLayout.request.ReserveSeatRequest
import com.net.pvr1.ui.seatLayout.response.*
import com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel
import com.net.pvr1.ui.summery.SummeryActivity
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.Companion.BOOKING_ID
import com.net.pvr1.utils.Constant.Companion.CINEMA_ID
import com.net.pvr1.utils.Constant.Companion.SESSION_ID
import com.net.pvr1.utils.Constant.Companion.TRANSACTION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal


@Suppress("DEPRECATION", "NAME_SHADOWING")
@AndroidEntryPoint
class SeatLayoutActivity : AppCompatActivity(), ShowsAdapter.RecycleViewItemClickListenerCity {
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
    private var selectedSeats1: ArrayList<SeatHC>? = null

    //Shows
    private var showsArray = ArrayList<Child.Sw.S>()
    private var selectSeatPriceCode = ArrayList<ReserveSeatRequest.Seat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        showsArray = intent.getStringArrayListExtra("shows") as ArrayList<Child.Sw.S>
        printLog("shows---->${showsArray}")
        sessionId = SESSION_ID
        authViewModel.seatLayout(
            CINEMA_ID,
            SESSION_ID,
            "",
            "",
            "",
            false,
            ""
        )
        seatLayout()
        reserveSeat()
        initTrans()
        shows()
    }

    //Shows
    private fun shows() {
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding?.recyclerView27?.layoutManager = LinearLayoutManager(this)
        val adapter = ShowsAdapter(showsArray, this, this)
        binding?.recyclerView27?.layoutManager = gridLayout
        binding?.recyclerView27?.adapter = adapter

    }

    //SeatLayout
    private fun seatLayout() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        priceMap = it.data.output.priceList
                        noOfRowsSmall = it.data.output.rows
                        drawColumn(it.data.output.rows)
                        retrieveData(it.data.output)
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

    //reserveSeat
    private fun reserveSeat() {
        authViewModel.reserveSeatResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

                        retrieverReserveSeatData(it.data.output)

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

    private fun retrieverReserveSeatData(output: ReserveSeatResponse.Output) {
        BOOKING_ID = output.bookingid
        if (output.nf == "true") {
            startActivity(Intent(this, FoodActivity::class.java))
        } else {
            startActivity(Intent(this, SummeryActivity::class.java))
        }

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

    //InitResponse
    private fun retrieverInitData(output: InitResponse.Output) {
        TRANSACTION_ID = output.transid
        for (item in selectedSeats) {
            val price = item.priceCode
            val seatId = item.seatBookingId
            println("price--->${price}--->${seatId}")
            selectSeatPriceCode.add(
                ReserveSeatRequest.Seat(
                    price.toString(),
                    seatId.toString()
                )
            )
        }

        val reserve = ReserveSeatRequest(CINEMA_ID, selectSeatPriceCode, sessionId, output.transid)
        val gson = Gson()
        val mMineUserEntity = gson.toJson(reserve, ReserveSeatRequest::class.java)
        println("request--->${mMineUserEntity}")
        authViewModel.reserveSeat(mMineUserEntity)

    }

    private fun retrieveData(data: SeatResponse.Output) {
        //title
        binding?.textView197?.text = data.mn
        //location
        binding?.textView198?.text = data.cn
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
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
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
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Constant().convertDpToPixel(50F, this)
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
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                centerLayoutParameter.addRule(RelativeLayout.CENTER_IN_PARENT)
                centerLayout.gravity = Gravity.CENTER_VERTICAL
                centerLayout.orientation = LinearLayout.HORIZONTAL
                centerLayout.layoutParams = centerLayoutParameter
                val padding: Int = Constant().convertDpToPixel(2F, this)
//                val downButtonLeft = ImageButton(this)
                val layoutParameter1 = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
//                downButtonLeft.layoutParams = layoutParameter1
//                downButtonLeft.setPadding(padding, 0, padding, 0)
//                downButtonLeft.setBackgroundColor(Color.TRANSPARENT)
//                downButtonLeft.setImageResource(R.drawable.down_arrow)
//                centerLayout.addView(downButtonLeft)
                val textView = TextView(this)
                textView.layoutParams = layoutParameter1
                textView.gravity = Gravity.CENTER
                textView.text = row.n
                textView.setPadding(padding, 0, padding, 0)
                textView.setTextAppearance(this, R.style.H1Size)
                centerLayout.addView(textView)
//                val downButtonRight = ImageButton(this)
//                downButtonRight.layoutParams = layoutParameter1
//                downButtonRight.setPadding(padding, 0, padding, 0)
//                downButtonRight.setImageResource(R.drawable.down_arrow)
//                downButtonRight.setBackgroundColor(Color.TRANSPARENT)
//                centerLayout.addView(downButtonRight)
                rlLayout.addView(centerLayout)
                binding?.llSeatLayout?.addView(rlLayout)

            }
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
                    Constant().convertDpToPixel(20F, this),
                    Constant().convertDpToPixel(20F, this)
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
                            seatView.setBackgroundResource(R.drawable.ic_vacant)
                            seatView.text = seat.sn.replace("[^\\d.]".toRegex(), "")
                        }
                    }
                    seatView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.black_with_fifteen_opacity
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
                    /*  System.out.println("seats=========="+seats_n.contains(seat.getSn()));
                    System.out.println("seats=========="+(seat.getSn()));
                    System.out.println("seats=========="+seats_n);*/
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
                    Constant().convertDpToPixel(20F, this),
                    Constant().convertDpToPixel(20F, this)
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
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(40F, this)
            ) else LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(45F, this)
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
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(40F, this)
            ) else LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(20F, this)
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

                        buttonClicked(this, seatView, num1, num2)
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
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
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
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
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        } else {
                                            seat.s = Constant.SEAT_SELECTED
                                            printLog("EnterInSeat-- seat.s->${seat.st}size--->${noOfRowsSmall?.size}")

                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1]
                                                .s[num2]
                                                .s = Constant.SEAT_SELECTED
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
                                                            this,
                                                            R.color.black
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()

                                }
                            }
                        }
                    }

                } else if (seat.s == Constant.SEAT_SELECTED) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this)
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
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2]
                            .s = Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                    }
                } else if (seat.s == Constant.HATCHBACK) {
                    if (seat.st == 1) {
                        buttonClicked(this, seatView, num1, num2)
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
                                dialog.show()

                            }
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
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
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()

                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {

                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        } else {
                                            seat.s = Constant.SEAT_SELECTED_HATCHBACK
                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1]
                                                .s[num2]
                                                .s = Constant.SEAT_SELECTED
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
                                                            this,
                                                            R.color.black
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                }
                            }
                        }
                    }
                } else if (seat.s == Constant.BIKE) {
                    if (seat.st == 1) {
                        buttonClicked(this, seatView, num1, num2)
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
                                dialog.show()

                            }
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
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
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        } else {
                                            seat.s = Constant.SEAT_SELECTED_BIKE
                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1]
                                                .s[num2]
                                                .s = Constant.SEAT_SELECTED
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
                                                            this,
                                                            R.color.black
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                }
                            }
                        }
                    }
                } else if (seat.s == Constant.SUV) {
                    if (seat.st == 1) {
                        buttonClicked(this, seatView, num1, num2)
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
                                positiveClick = {
                                },
                                negativeClick = {
                                })
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
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
                                dialog.show()

                            }
                            if (selectBuddy) {
                                val dialog = OptionDialog(this,
                                    R.mipmap.ic_launcher,
                                    R.string.app_name,
                                    messageText,
                                    positiveBtnText = R.string.ok,
                                    negativeBtnText = R.string.no,
                                    positiveClick = {
                                    },
                                    negativeClick = {
                                    })
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
                                            positiveClick = {
                                            },
                                            negativeClick = {
                                            })
                                        dialog.show()
                                    } else {
                                        if (selectedSeats.size > 0 && seat.bu) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messageText,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()

                                        } else {
                                            seat.s = Constant.SEAT_SELECTED_SUV
                                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1]
                                                .s[num2]
                                                .s = Constant.SEAT_SELECTED
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
                                                            this,
                                                            R.color.black
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
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()

                                }
                            }
                        }
                    }
                } else if (seat.s == Constant.SEAT_SELECTED_HATCHBACK) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this)
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
                                        this,
                                        R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2]
                            .s = Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                        seatView.setBackgroundResource(R.drawable.ic_red_sedan)
                        seatView.setTextColor(ContextCompat.getColor(this, R.color.red_data))
                    }
                } else if (seat.s == Constant.SEAT_SELECTED_BIKE) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this)
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
                                        this,
                                        R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2]
                            .s = Constant.SEAT_AVAILABEL
                        removeSelectedSeats(seat)
                        seatView.text = ""
                        seatView.setBackgroundResource(R.drawable.ic_bike_normal)
                    }
                } else if (seat.s == Constant.SEAT_SELECTED_SUV) {
                    if (seat.st == 1 && !flagHc) {
                        removeHC(this)
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
                                        this,
                                        R.color.black_with_fifteen_opacity
                                    )
                                )
                            }
                        }
                        if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2]
                            .s = Constant.SEAT_AVAILABEL
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

            binding?.constraintLayout56?.setBackgroundResource(R.drawable.yellow_seat_curve)
//            binding?.constraintLayout56?.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.unSelectBg
//                )
//            )

            if (!isDit) binding?.textView195?.text =
                "No Seats Selected"
            else binding?.textView195?.text =
                "No Vehicle Slots Selected"
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
            binding?.constraintLayout56?.setBackgroundResource(R.drawable.yellow_seat_curve)

//            binding?.constraintLayout56?.setBackgroundColor(
//                ContextCompat.getColor(
//                    this,
//                    R.color.unSelectBg
//                )
//            )
            if (!isDit) binding?.textView195?.text =
                "No Seats Selected" else binding?.textView196?.text =
                "No Vehicle Slots Selected"
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
                        seatNo =
                            if (binding?.textView195?.text.toString()
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
                        seatNo =
                            if (binding?.textView195?.text.toString()
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
                printLog("exception--->${e.message}")
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
        printLog("SeatClick--->${seat.st}")
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
        selectedSeats1?.add(selectedSeat1)
        selectedSeatsBox?.add(selectedSeat1)
        calculatePrice()
    }

    @SuppressLint("SetTextI18n")
    private fun buttonClicked(context: Activity, seatView: TextView, num1: Int, num2: Int) {
        // custom dialog
        val messagePcTextView: TextView
        val titleText: TextView
        val cancel: TextView
        val delete: TextView
        val icon: ImageView
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.ticket_cancel)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
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
                    positiveClick = {
                    },
                    negativeClick = {
                    })
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
                    positiveClick = {
                    },
                    negativeClick = {
                    })
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
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    } else {
                        if (selectedSeats.size > 0 && seat.bu) {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                messageText,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        } else {
                            seat.s = Constant.SEAT_SELECTED
                            if (noOfRowsSmall?.size!! > 0) noOfRowsSmall!![num1].s[num2]
                                .s = Constant.SEAT_SELECTED
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
                                            this,
                                            R.color.black
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
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun removeHC(context: Activity) {
        // custom dialog
        val messagePcTextView: TextView
        val titleText: TextView
        val cancel: TextView
        val delete: TextView
        val icon: ImageView
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.ticket_cancel)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.CENTER)
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
        delete.text = "NO"
        delete.setOnClickListener { dialog.dismiss() }
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.text = "YES"
        dialog.show()
    }

    override fun showsClick(comingSoonItem: Int) {
        sessionId = comingSoonItem.toString()
        binding?.llRowName?.removeAllViews()
        authViewModel.seatLayout(
            CINEMA_ID,
            sessionId,
            "",
            "",
            "",
            false,
            ""
        )
    }
}