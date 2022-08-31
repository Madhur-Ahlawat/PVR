package com.net.pvr1.ui.seatLayout

import android.app.Activity
import android.app.Dialog
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
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySeatLayoutBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.seatLayout.response.*
import com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.printLog
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class SeatLayoutActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySeatLayoutBinding? = null
    private val authViewModel: SeatLayoutViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var priceMap: Map<String, SeatResponse.Output.PriceList.Price>? = null
    private var selectedSeats = ArrayList<Seat>()
    private var noOfSeatsSelected = ArrayList<SeatTagData>()
    var isDit = false
    var selectedSeatsdbox:  ArrayList<SeatHC>? = null
    var selectedSeats1: ArrayList<SeatHC>? = null
    var select_buddy = false
    var hc_seat = false
    var hc_seat1 = false
    var ca_seat = false
    var flag_hc = false
    var flag_hc1 = false
    var price_val = 0.0
    var hc_count1 = 0
    var ca_count1 = 0
    var hc_count = 0
    var ca_count = 0
    var Key_data = ""
    var seats_n: ArrayList<String>? = null
    var coupol_seat = 0
    var messahe_text = ""
    private val noOfRows_small: List<SeatResponse.Output.Row>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        authViewModel.seatLayout(
            "DWAR", "33466", "", "", "", false, ""
        )
        seatLayout()
    }

    private fun seatLayout() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        priceMap = it.data.output.priceList
//                        selectedSeatsdbox=it.data.output.rows
                        retrieveData(it.data.output.rows)
                        drowColum(it.data.output.rows)
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


    private fun retrieveData(noOfRows: List<SeatResponse.Output.Row>) {
        binding?.seatInclude?.llSeatlayout?.removeAllViews()
        var areaName = ""
        for (i in noOfRows.indices) {
            val row: SeatResponse.Output.Row = noOfRows[i]
            val noSeats: List<SeatResponse.Output.Row.S> = row.s
            if (noSeats.isNotEmpty()) {
                //Draw seats ==================
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = layoutParams

                binding?.seatInclude?.llSeatlayout?.addView(linearLayout)
                drowRowSmall(noSeats, linearLayout)
            } else {

                val rlLayuout = RelativeLayout(this)
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Constant().convertDpToPixel(10F, this)
                )
                rlLayuout.layoutParams = layoutParams
                if (row.c != null) {
                    var colorCodes: String = if (row.c.contains("#")) row.c else "#" + row.c
                    rlLayuout.setBackgroundColor(Color.parseColor(colorCodes))
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

                rlLayuout.addView(centerLayout)
                binding?.seatInclude?.llSeatlayout?.addView(rlLayuout)
            }
        }
//        if (progressDialog != null) DialogClass.dismissDialog(progressDialog)
    }

    private fun drowColum(noOfRows: List<SeatResponse.Output.Row>) {
        binding?.seatInclude?.llSeatlayout?.removeAllViews()
        var areaName = ""
        for (i in noOfRows.indices) {
            val row: SeatResponse.Output.Row = noOfRows[i]
            val noSeats: List<SeatResponse.Output.Row.S> = row.s
            //    System.out.println("fhjfhjfh-->" + row.getN());
            if (noSeats.size > 0) {
                //draw extras space for row name
                addRowName(row.n, false)
                //Draw seats ==================
                val linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = layoutParams
                binding?.seatInclude?.llSeatlayout?.addView(linearLayout)
                drowRow(linearLayout, noSeats, areaName, i)
            } else {

                //draw extras space for row name
                addRowName("", true)
                //update area
                areaName = row.n

                //Draw Area==================
                val rlLayuout = RelativeLayout(this)
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Constant().convertDpToPixel(50F, this)
                )
                rlLayuout.layoutParams = layoutParams
                if (row.c != null) {
                    var colorCodes: String
                    colorCodes = if (row.c.contains("#")) row.c else "#" + row.c
                    rlLayuout.setBackgroundColor(Color.parseColor(colorCodes))
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
                val downButtonLeft = ImageButton(this)
                val layoutParmatere1 = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                downButtonLeft.layoutParams = layoutParmatere1
                downButtonLeft.setPadding(padding, 0, padding, 0)
                downButtonLeft.setBackgroundColor(Color.TRANSPARENT)
                downButtonLeft.setImageResource(R.drawable.down_arrow)
                centerLayout.addView(downButtonLeft)
                val textView = TextView(this)
                textView.layoutParams = layoutParmatere1
                textView.gravity = Gravity.CENTER
                textView.text = row.n
                textView.setPadding(padding, 0, padding, 0)
                textView.setTextAppearance(this, R.style.H1Size)
                centerLayout.addView(textView)
                val downButtonRight = ImageButton(this)
                downButtonRight.layoutParams = layoutParmatere1
                downButtonRight.setPadding(padding, 0, padding, 0)
                downButtonRight.setImageResource(R.drawable.down_arrow)
                downButtonRight.setBackgroundColor(Color.TRANSPARENT)
                centerLayout.addView(downButtonRight)
                rlLayuout.addView(centerLayout)
                binding?.seatInclude?.llSeatlayout?.addView(rlLayuout)

                // System.out.println("currentContext====================" + row.getN());
            }
        }
//        if (progressDialog != null) DialogClass.dismissDialog(progressDialog)
    }

    private fun drowRow(
        llDarwRow: LinearLayout,
        noSeats: List<SeatResponse.Output.Row.S>,
        areaName: String,
        num: Int
    ) {
        for (i in noSeats.indices) {
            val seat: SeatResponse.Output.Row.S = noSeats[i]
            if (seat.b != null && !seat.b.equals("")) {
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
                    if (seat.s === Constant.BIKE || seat.s === Constant.BIKE_SEAT_BOOKED || seat.s === Constant.SEAT_SELECTED_BIKE) {
                        seatView.setPadding(0, Constant().convertDpToPixel(8F, this), 0, 0)
                        layoutParams = LinearLayout.LayoutParams(
                            Constant().convertDpToPixel(20F, this),
                            Constant().convertDpToPixel(30F, this)
                        )
                    } else {
                        seatView.setPadding(0, Constant().convertDpToPixel(8F, this), 0, 0)
                        layoutParams = LinearLayout.LayoutParams(
                            Constant().convertDpToPixel(20F, this),
                            Constant().convertDpToPixel(40F, this)
                        )
                    }
                }
                val margin: Int = Constant().convertDpToPixel(4F, this)
                layoutParams.setMargins(margin, margin, margin, margin)
                seatView.layoutParams = layoutParams
                if (seat.s === Constant.SEAT_AVAILABEL) {
                    //   System.out.println("Handicaped1-" + seat.getHc());
                    if (seat.st === 1) {
                        hc_count1 = hc_count1 + 1
                        hc_seat = false
                        hc_seat1 = true
                        flag_hc1 = true
                        seatView.text = ""
                        seatView.setBackgroundResource(R.drawable.ic_hcseat)
                    } else if (seat.st === 2) {
                        ca_count1 = ca_count1 + 1
                        seatView.text = ""
                        seatView.setBackgroundResource(R.drawable.ic_camp)
                    } else if (seat.st === 3) {
                        seatView.text = ""
                        seatView.setBackgroundResource(R.drawable.buddy)
                    } else {
                        seatView.setBackgroundResource(R.drawable.ic_vacant)
                        seatView.text = seat.sn.replace("[^\\d.]", "")
                    }
                    seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                } else if (seat.s === Constant.SEAT_SELECTED) {

                    //Add selected seat to list
                    seatView.setTextColor(resources.getColor(R.color.black))
                    val seatTagData = SeatTagData()
                    seatTagData.b=seat.b
//                    seatTagData?.c=seat.c
                    seatTagData.s=seat.s
                    seatTagData.sn=seat.sn
                    seatTagData.st=seat.st
                    seatTagData.area=areaName
                    addSelectedSeats(seatTagData, seatView, num, i)

                    //set desing for selected seat
                    seatView.setBackgroundResource(R.drawable.ic_selected)
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_selected_car)
                } else if (seat.s === Constant.SEAT_BOOKED) {
                    /*  System.out.println("seats=============="+seats_n.contains(seat.getSn()));
                    System.out.println("seats=============="+(seat.getSn()));
                    System.out.println("seats=============="+seats_n);*/
                    if (seats_n != null && seats_n!!.size > 0 && seats_n!!.contains(seat.sn)) {
                        if (seat.st === 1) {
                            if (hc_seat1 == false) hc_seat = true
                            seatView.setBackgroundResource(R.drawable.ic_hco_green)
                        } else if (seat.st === 2) {
                            seatView.setBackgroundResource(R.drawable.ic_cao_green)
                        } else {
                            seatView.setTextColor(resources.getColor(R.color.black))
                            seatView.setBackgroundResource(R.drawable.ic_previous_selected)
                        }
                    } else {
                        if (seat.st === 1) {
                            if (hc_seat1 == false) hc_seat = true
                            seatView.setBackgroundResource(R.drawable.ic_hco)
                        } else if (seat.st === 2) {
                            seatView.setBackgroundResource(R.drawable.ic_cao)
                        } else {
                            seatView.setTextColor(resources.getColor(R.color.white))
                            seatView.setBackgroundResource(R.drawable.ic_fill)
                        }
                    }
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_occupied_car)
                } else if (seat.s === Constant.SEAT_SOCIAL_DISTANCING) {
                    seatView.setBackgroundResource(R.drawable.ic_social_distancing)
                } else if (seat.s === Constant.HATCHBACK) {
                    seatView.text = seat.sn.replace("[^\\d.]", "")
                    seatView.setTextColor(resources.getColor(R.color.red_data))
                    seatView.setBackgroundResource(R.drawable.ic_red_sedan)
                } else if (seat.s === Constant.SUV) {
                    seatView.text = seat.sn.replace("[^\\d.]", "")
                    seatView.setTextColor(resources.getColor(R.color.blue_data))
                    seatView.setBackgroundResource(R.drawable.ic_blue_suv)
                } else if (seat.s === Constant.BIKE) {
                    // seatView.setText(seat.getSn().replaceAll("[^\\d.]", ""));
                    //seatView.setTextColor(getResources().getColor(R.color.blue_data));
                    seatView.setBackgroundResource(R.drawable.ic_bike_normal)
                } else if (seat.s === Constant.BIKE_SEAT_BOOKED) {
                    // seatView.setText(seat.getSn().replaceAll("[^\\d.]", ""));
                    //seatView.setTextColor(getResources().getColor(R.color.blue_data));
                    seatView.setBackgroundResource(R.drawable.ic_bike_booked)
                } else if (seat.s === Constant.SEAT_SELECTED_HATCHBACK || seat.s === Constant.SEAT_SELECTED_SUV) {

                    //Add selected seat to list
                    seatView.setTextColor(resources.getColor(R.color.black))
                    val seatTagData = SeatTagData()
                    seatTagData.b=seat.b
                    seatTagData.c=seat.c
                    seatTagData.s=seat.s
                    seatTagData.sn=seat.sn
                    seatTagData.st=seat.st
                    seatTagData.area=areaName
                    addSelectedSeats(seatTagData, seatView, num, i)


                    //set desing for selected seat
                    seatView.setBackgroundResource(R.drawable.ic_selected)
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_selected_car)
                } else if (seat.s === Constant.SEAT_SELECTED_BIKE) {

                    //Add selected seat to list
                    seatView.setTextColor(resources.getColor(R.color.black))
                    val seatTagData = SeatTagData()
                    seatTagData.b=seat.b
                    seatTagData.c=seat.c
                    seatTagData.s=seat.s
                    seatTagData.sn=seat.sn
                    seatTagData.st=seat.st
                    seatTagData.area=areaName
                    addSelectedSeats(seatTagData, seatView, num, i)


                    //set desing for selected seat
                    seatView.setBackgroundResource(R.drawable.ic_selected)
                    seatView.text = ""
                    if (isDit) seatView.setBackgroundResource(R.drawable.ic_bike_selected)
                } else {
                    seatView.setBackgroundColor(Color.TRANSPARENT)
                }
                //create tag for seat partial_layout
                val seatTagData = SeatTagData()
                seatTagData.b=seat.b
                seatTagData.c=seat.c
                seatTagData.s=seat.s
                seatTagData.sn=seat.sn
                seatTagData.area=areaName
                seatTagData.hc=seat.hc
                seatTagData.bu=seat.bu
                seatTagData.st=seat.st
                seatTagData.cos=seat.cos
                seatView.tag = seatTagData

                //make click listner for seatview
                setClick(seatView, num, i, "1")
                llDarwRow.addView(seatView)
                /* if (seatTagData.getS() == PCConstants.SEAT_AVAILABEL && seatTagData.getBu()==true && seatTagData.getSt() == 3 && !arrayListSeat.contains(seatTagData)) {
                    arrayListSeat.add(seatTagData);

                    Seatselection_class seatselectionClass = new Seatselection_class();
                    seatselectionClass.setTextView(seatView);
                    seatselectionClass.setNum1(num);
                    seatselectionClass.setNum2(i);
                    arrayListSeatText.add(seatselectionClass);
                }*/
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
                llDarwRow.addView(seatView)
            }
        }
    }

    private fun addRowName(rowName: String, space: Boolean) {
        //  System.out.println("rowName========"+rowName+"====="+space);
        if (space) {
            val txtRowName = TextView(this)
            txtRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            val layoutParams: LinearLayout.LayoutParams
            layoutParams = if (isDit) LinearLayout.LayoutParams(
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
            binding?.seatInclude?.llRowName?.addView(txtRowName)
        } else {

            //Area partial_layout design ==========================
            val txtRowName = TextView(this)
            txtRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            txtRowName.textSize = Color.parseColor("#767373").toFloat()
//            FontClass.sethelveticaNeueBold(txtRowName)
            val layoutParams: LinearLayout.LayoutParams
            layoutParams = if (isDit) LinearLayout.LayoutParams(
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
            binding?.seatInclude?.llRowName?.addView(txtRowName)
        }
    }

    private fun drowRowSmall(noSeats: List<SeatResponse.Output.Row.S>, llDarwRow: LinearLayout) {
        for (i in noSeats.indices) {
            val seat: SeatResponse.Output.Row.S = noSeats[i]
            if (seat.b != null && seat.b != "") {
                val seatView = ImageButton(this)
                seatView.setBackgroundColor(Color.WHITE)
                val layoutParams = LinearLayout.LayoutParams(10, 10)
                val margin: Int = Constant().convertDpToPixel(1F, this)
                layoutParams.setMargins(margin, margin, margin, margin)
                seatView.layoutParams = layoutParams
                if (seat.s === Constant.SEAT_AVAILABEL || seat.s === Constant.HATCHBACK || seat.s === Constant.SUV || seat.s === Constant.BIKE) {
                    seatView.setImageResource(R.drawable.group)
                } else if (seat.s === Constant.SEAT_SELECTED || seat.s === Constant.SEAT_SELECTED_HATCHBACK || seat.s === Constant.SEAT_SELECTED_SUV || seat.s === Constant.SEAT_SELECTED_BIKE) {
                    seatView.setBackgroundColor(resources.getColor(R.color.pvr_yellow))
                } else if (seat.s === Constant.SEAT_BOOKED) {
                    seatView.setImageResource(R.drawable.occupied)
                } else {
                    seatView.setBackgroundColor(Color.TRANSPARENT)
                }
                llDarwRow.addView(seatView)
            } else {
                val seatView = ImageButton(this)
                val layoutParams = LinearLayout.LayoutParams(10, 10)
                seatView.layoutParams = layoutParams
                val margin: Int = Constant().convertDpToPixel(1F, this)
                layoutParams.setMargins(margin, margin, margin, margin)
                seatView.setBackgroundColor(Color.TRANSPARENT)
                llDarwRow.addView(seatView)
            }
        }
    }

    private fun setClick(seatView: TextView, num1: Int, num2: Int, tp: String) {
        seatView.setOnClickListener {
            try {
                val seat = seatView.tag as SeatTagData
                if (seat != null) {
                    if (seat.s === Constant.SEAT_AVAILABEL) {
                        if (seat.st === 1) {
                            buttonClicked(this, seatView, num1, num2)
                        } else {
                            if (seat.st === 2) {
                                hc_seat = ca_count1 > hc_count1
                            }
                            //  System.out.println("Handicaped--->" + hc_seat + ca_count + "--" + hc_count + "-" + ca_count1 + "---" + hc_count1);
                            if (seat.st === 2 && hc_seat == false && ca_count >= hc_count) {

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
//                                DialogClass.alertDialog(
//                                    currentContext,
//                                    "Companion seat is only available with a wheelchair seat."
//                                )
                            } else {
                                if (flag_hc1 == true) hc_seat = false
                                if (seat.cos === true && coupol_seat == 0) {
                                    coupol_seat = 1
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
//                                    DialogClass.alertDialog(
//                                        this,
//                                        "These are couple recliners."
//                                    )
                                }
                                println("select_buddy=============$select_buddy")
                                if (select_buddy == true) {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        messahe_text,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
//                                    DialogClass.alertDialog(this, messahe_text)
                                } else {
                                    if (selectedSeats.size < 10) {
                                        if (select_buddy == true && seat.bu === true) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messahe_text,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        } else {
                                            if (selectedSeats.size > 0 && seat.bu === true) {
                                                val dialog = OptionDialog(this,
                                                    R.mipmap.ic_launcher,
                                                    R.string.app_name,
                                                    messahe_text,
                                                    positiveBtnText = R.string.ok,
                                                    negativeBtnText = R.string.no,
                                                    positiveClick = {
                                                    },
                                                    negativeClick = {
                                                    })
                                                dialog.show()
                                            } else {
                                                seat.s=Constant.SEAT_SELECTED
                                                if (noOfRows_small?.size!! > 0) noOfRows_small?.get(
                                                    num1
                                                )
                                                    ?.s?.get(num2)
                                                    ?.s=Constant.SEAT_SELECTED
                                                if (seat.st === 1) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                } else if (seat.st === 2) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                } else if (seat.st === 3) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                } else {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(resources.getColor(R.color.black))
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

                        /*  getTxtSelectedSeat=seatView;
                            num_buddy_1=num1;
                            num_buddy_2=num2;*/
                    } else if (seat.s === Constant.SEAT_SELECTED) {
                        if (seat.st === 1 && flag_hc == false) {
                            removeHC(this, seatView, num1, num2)
                        } else {
                            seat.s=Constant.SEAT_AVAILABEL
                            if (seat.st === 1) {
                                hc_seat = false
                            } else if (seat.st === 2) {
                                ca_seat = false
                            } else if (seat.st === 3) {
                                select_buddy = false
                            }
                            if (seat.st === 1) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            } else if (seat.st === 2) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            } else if (seat.st === 3) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            } else {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                            }
                            if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)?.s?.get(num2)
                                ?.s=Constant.SEAT_AVAILABEL
                            removeSelectedSeats(seat)
                        }
                    } else if (seat.s === Constant.HATCHBACK) {
                        if (seat.st === 1) {
                            buttonClicked(this, seatView, num1, num2)
                        } else {
                            if (seat.st === 2) {
                                hc_seat = ca_count1 > hc_count1
                            }
                            //  System.out.println("Handicaped--->" + hc_seat + ca_count + "--" + hc_count + "-" + ca_count1 + "---" + hc_count1);
                            if (seat.st === 2 && !hc_seat && ca_count >= hc_count) {
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

//                                DialogClass.alertDialog(
//                                    currentContext,
//                                    "Companion seat is only available with a wheelchair seat."
//                                )
//                            } else {
                                if (flag_hc1 == true) hc_seat = false
                                if (seat.cos === true && coupol_seat == 0) {
                                    coupol_seat = 1
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
                                if (select_buddy == true) {
//                                    DialogClass.alertDialog(currentContext, messahe_text)

                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        messahe_text,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()

                                } else {
                                    if (selectedSeats.size < 10) {
                                        if (select_buddy == true && seat.bu === true) {

                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messahe_text,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()

                                        } else {
                                            if (selectedSeats.size > 0 && seat.bu === true) {

                                                val dialog = OptionDialog(this,
                                                    R.mipmap.ic_launcher,
                                                    R.string.app_name,
                                                    messahe_text,
                                                    positiveBtnText = R.string.ok,
                                                    negativeBtnText = R.string.no,
                                                    positiveClick = {
                                                    },
                                                    negativeClick = {
                                                    })
                                                dialog.show()
                                            } else {
                                                seat.s=Constant.SEAT_SELECTED_HATCHBACK
                                                if (noOfRows_small?.size!! > 0) noOfRows_small?.get(
                                                    num1
                                                )
                                                    ?.s?.get(num2)
                                                    ?.s=Constant.SEAT_SELECTED
                                                if (seat.st === 1) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                } else if (seat.st === 2) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                } else if (seat.st === 3) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                } else {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(resources.getColor(R.color.black))
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
                    } else if (seat.s === Constant.BIKE) {
                        if (seat.st === 1) {
                            buttonClicked(this, seatView, num1, num2)
                        } else {
                            if (seat.st === 2) {
                                hc_seat = ca_count1 > hc_count1
                            }
                            //  System.out.println("Handicaped--->" + hc_seat + ca_count + "--" + hc_count + "-" + ca_count1 + "---" + hc_count1);
                            if (seat.st === 2 && hc_seat == false && ca_count >= hc_count) {


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
                                if (flag_hc1 == true) hc_seat = false
                                if (seat.cos === true && coupol_seat == 0) {
                                    coupol_seat = 1
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
                                if (select_buddy == true) {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        messahe_text,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                } else {
                                    if (selectedSeats.size < 10) {
                                        if (select_buddy == true && seat.bu === true) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messahe_text,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        } else {
                                            if (selectedSeats.size > 0 && seat.bu === true) {
                                                val dialog = OptionDialog(this,
                                                    R.mipmap.ic_launcher,
                                                    R.string.app_name,
                                                    messahe_text,
                                                    positiveBtnText = R.string.ok,
                                                    negativeBtnText = R.string.no,
                                                    positiveClick = {
                                                    },
                                                    negativeClick = {
                                                    })
                                                dialog.show()
                                            } else {
                                                seat.s=Constant.SEAT_SELECTED_BIKE
                                                if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)
                                                    ?.s?.get(num2)
                                                    ?.s=Constant.SEAT_SELECTED
                                                if (seat.st === 1) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                } else if (seat.st === 2) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                } else if (seat.st === 3) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                } else {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(resources.getColor(R.color.black))
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
                    } else if (seat.s === Constant.SUV) {
                        if (seat.st === 1) {
                            buttonClicked(this, seatView, num1, num2)
                        } else {
                            if (seat.st === 2) {
                                hc_seat = ca_count1 > hc_count1
                            }
                            //  System.out.println("Handicaped--->" + hc_seat + ca_count + "--" + hc_count + "-" + ca_count1 + "---" + hc_count1);
                            if (seat.st === 2 && hc_seat == false && ca_count >= hc_count) {
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
                                if (flag_hc1 == true) hc_seat = false
                                if (seat.cos === true && coupol_seat == 0) {
                                    coupol_seat = 1
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
                                if (select_buddy == true) {
                                    val dialog = OptionDialog(this,
                                        R.mipmap.ic_launcher,
                                        R.string.app_name,
                                        messahe_text,
                                        positiveBtnText = R.string.ok,
                                        negativeBtnText = R.string.no,
                                        positiveClick = {
                                        },
                                        negativeClick = {
                                        })
                                    dialog.show()
                                } else {
                                    if (selectedSeats.size < 10) {
                                        if (select_buddy && seat.bu === true) {
                                            val dialog = OptionDialog(this,
                                                R.mipmap.ic_launcher,
                                                R.string.app_name,
                                                messahe_text,
                                                positiveBtnText = R.string.ok,
                                                negativeBtnText = R.string.no,
                                                positiveClick = {
                                                },
                                                negativeClick = {
                                                })
                                            dialog.show()
                                        } else {
                                            if (selectedSeats.size > 0 && seat.bu === true) {
                                                val dialog = OptionDialog(this,
                                                    R.mipmap.ic_launcher,
                                                    R.string.app_name,
                                                    messahe_text,
                                                    positiveBtnText = R.string.ok,
                                                    negativeBtnText = R.string.no,
                                                    positiveClick = {
                                                    },
                                                    negativeClick = {
                                                    })
                                                dialog.show()

                                            } else {
                                                seat.s=Constant.SEAT_SELECTED_SUV
                                                if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)
                                                    ?.s?.get(num2)
                                                    ?.s=Constant.SEAT_SELECTED
                                                if (seat.st === 1) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                                                } else if (seat.st === 2) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.ic_campy)
                                                } else if (seat.st === 3) {
                                                    seatView.text = ""
                                                    seatView.setBackgroundResource(R.drawable.buddyw)
                                                } else {
                                                    seatView.setBackgroundResource(R.drawable.ic_selected)
                                                    seatView.setTextColor(resources.getColor(R.color.black))
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
                    } else if (seat.s === Constant.SEAT_SELECTED_HATCHBACK) {
                        if (seat.st === 1 && !flag_hc) {
                            removeHC(this, seatView, num1, num2)
                        } else {
                            seat.s=Constant.HATCHBACK
                            if (seat.st === 1) {
                                hc_seat = false
                            } else if (seat.st === 2) {
                                ca_seat = false
                            } else if (seat.st === 3) {
                                select_buddy = false
                            }
                            if (seat.st === 1) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            } else if (seat.st === 2) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            } else if (seat.st === 3) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            } else {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                            }
                            if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)?.s?.get(num2)
                                ?.s=Constant.SEAT_AVAILABEL
                            removeSelectedSeats(seat)
                            seatView.setBackgroundResource(R.drawable.ic_red_sedan)
                            seatView.setTextColor(resources.getColor(R.color.red_data))
                        }
                    } else if (seat.s === Constant.SEAT_SELECTED_BIKE) {
                        if (seat.st === 1 && flag_hc == false) {
                            removeHC(this, seatView, num1, num2)
                        } else {
                            seat.s=Constant.BIKE
                            if (seat.st === 1) {
                                hc_seat = false
                            } else if (seat.st === 2) {
                                ca_seat = false
                            } else if (seat.st === 3) {
                                select_buddy = false
                            }
                            if (seat.st === 1) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            } else if (seat.st === 2) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            } else if (seat.st === 3) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            } else {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                            }
                            if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)?.s?.get(num2)
                                ?.s=Constant.SEAT_AVAILABEL
                            removeSelectedSeats(seat)
                            seatView.text = ""
                            seatView.setBackgroundResource(R.drawable.ic_bike_normal)
                        }
                    } else if (seat.s === Constant.SEAT_SELECTED_SUV) {
                        if (seat.st === 1 && flag_hc == false) {
                            removeHC(this, seatView, num1, num2)
                        } else {
                            seat.s=Constant.SUV
                            if (seat.st === 1) {
                                hc_seat = false
                            } else if (seat.st === 2) {
                                ca_seat = false
                            } else if (seat.st === 3) {
                                select_buddy = false
                            }
                            if (seat.st === 1) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseat)
                            } else if (seat.st === 2) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_camp)
                            } else if (seat.st === 3) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddy)
                            } else {
                                seatView.setBackgroundResource(R.drawable.ic_vacant)
                                seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
                            }
                            if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)?.s?.get(num2)
                                ?.s=Constant.SEAT_AVAILABEL
                            removeSelectedSeats(seat)
                            seatView.setBackgroundResource(R.drawable.ic_blue_suv)
                            seatView.setTextColor(resources.getColor(R.color.blue_data))
                        }
                    }

                }
            } catch (e: Exception) {
                printLog("exception---${e.message}")
            }
        }
    }

    private fun removeSelectedSeats(seat: SeatTagData) {
        for (i in selectedSeats.indices) {
            if (seat.b.equals(selectedSeats[i].seatBookingId)) {
                if (seat.st === 1) {
                    hc_count--
                    hc_count1 = hc_count1 + 1
                } else if (seat.st === 2) {
                    ca_count--
                    ca_count1 = ca_count1 + 1
                }
                noOfSeatsSelected.removeAt(i)
                selectedSeats.removeAt(i)
                selectedSeatsdbox?.removeAt(i)
                //                    selectedSeats1.remove(i);
                break
            }
        }
        if (noOfSeatsSelected.size == 0) {
            if (!isDit) binding?.txtSelectedSeat?.text =
                "No Seats Selected" else binding?.txtSelectedSeat?.text =
                "No Vehicle Slots Selected"
        }
        calculatePrice()
    }

    private fun calculatePrice() {
        binding?.txtSelectedSeat?.text = ""
        if (noOfSeatsSelected.size > 0) {
            binding?.btnContinue?.isClickable = true
            binding?.btnContinue?.setTextColor(Color.parseColor("#000000"))
            binding?.btnContinue?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_bright,
                0
            )
            binding?.txtSelectedSeat?.text = ""
            if (noOfSeatsSelected.size == 1) {
                binding?.btitem?.text = noOfSeatsSelected.size.toString() + " Seat Selected"
                if (isDit) binding?.btitem?.text =
                    noOfSeatsSelected.size.toString() + " Vehicle Slot Selected"
            } else {
                binding?.btitem?.text = noOfSeatsSelected.size.toString() + " Seats Selected"
                if (isDit) binding?.btitem?.text =
                    noOfSeatsSelected.size.toString() + " Vehicle Slots Selected"
            }
        } else {
//            btnContinue.setText("PAY "+getString(R.string.default_currency));
            binding?.btnContinue?.isClickable = false
            binding?.btnContinue?.setTextColor(Color.parseColor("#80000000"))
            binding?.btnContinue?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_right,
                0
            )
            if (!isDit) binding?.txtSelectedSeat?.text =
                "No Seats Selected" else binding?.txtSelectedSeat?.text =
                "No Vehicle Slots Selected"
            binding?.btitem?.text = ""
        }
        var totalPrice = 0f
        val selecteSeat = java.util.ArrayList<Spannable>()
        var bigDecimal = BigDecimal(0)
        for (i in noOfSeatsSelected.indices) {
            try {
                val seatTagData = noOfSeatsSelected[i]
                val price = priceMap!![seatTagData.getC()]
                var wordtoSpan: Spannable
                var seatNo: String
                if (seatTagData.st === 1) {
                    seatNo =
                        if (binding?.txtSelectedSeat?.text.toString()
                                .equals("", ignoreCase = true)
                        ) {
                            "\uF101 " + seatTagData.sn
                        } else {
                            "," + "\uF101 " + seatTagData.sn
                        }
                    wordtoSpan = SpannableString(seatNo)
                    wordtoSpan.setSpan(
                        ForegroundColorSpan(Color.parseColor("#800080")),
                        0,
                        seatNo.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding?.txtSelectedSeat?.append(wordtoSpan)
                } else if (seatTagData.st === 2) {
                    seatNo =
                        if (binding?.txtSelectedSeat?.text.toString()
                                .equals("", ignoreCase = true)
                        ) {
                            "\uF102 " + seatTagData.sn
                        } else {
                            "," + "\uF102 " + seatTagData.sn
                        }
                    wordtoSpan = SpannableString(seatNo)
                    wordtoSpan.setSpan(
                        ForegroundColorSpan(Color.parseColor("#800080")),
                        0,
                        seatNo.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                   binding?.txtSelectedSeat?.append(wordtoSpan)
                } else {
                    if ( binding?.txtSelectedSeat?.getText().toString().equals("", ignoreCase = true)) {
                        seatNo = seatTagData.sn.toString()
                    } else {
                        seatNo = "," + seatTagData.sn
                    }
                    wordtoSpan = SpannableString(seatNo)
                    wordtoSpan.setSpan(
                        ForegroundColorSpan(Color.parseColor("#333333")),
                        0,
                        seatNo.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding?.txtSelectedSeat?.append(wordtoSpan)
                }
                selecteSeat.add(wordtoSpan)
//                binding?.txtArea?.setText(price.)
                bigDecimal = BigDecimal(totalPrice.toString()).add(BigDecimal(price?.price))
                totalPrice = bigDecimal.toFloat()
            } catch (e: java.lang.Exception) {
                printLog("exception--->${e.message}")
            }
        }

        //txtSelectedSeat.setText(android.text.TextUtils.join(", ", selecteSeat));
        binding?.btnContinue?.text =
            "PAY " + getString(R.string.currency) + " " + Constant().removeTrailingZeroFormatter(
                bigDecimal.toFloat()
            )
        binding?.seatCounterLayout?.seatCounter?.setText(selecteSeat.size.toString())
        price_val = bigDecimal.toFloat().toDouble()
    }

    private fun addSelectedSeats(seat: SeatTagData, seatView: TextView, num1: Int, num2: Int) {
        if (seat.st === 1) {
            hc_seat = true
            hc_count = hc_count + 1
            hc_count1--
        } else if (seat.st === 2) {
            ca_seat = true
            ca_count += 1
            ca_count1--
        } else if (seat.st === 3) {
            select_buddy = true
        }
        try {
            Key_data = seat?.c as String
        } catch (e: Exception) {
        }
        val selectedSeat = Seat()
        val selectedSeat1 = SeatHC()
        selectedSeat.seatBookingId=seat.b
        val price = priceMap!![seat.getC()]
        selectedSeat.priceCode=price?.priceCode
        noOfSeatsSelected.add(seat)
        selectedSeats.add(selectedSeat)
        selectedSeat1.setNum1(num1)
        selectedSeat1.setNum2(num2)
        selectedSeat1.setSeatView(seatView)
        selectedSeat1.setSt(seat.st)
        selectedSeat1.setPriceCode(price?.priceCode)
        selectedSeat1.setSeatBookingId(seat.b)
        selectedSeats1?.add(selectedSeat1)
        selectedSeatsdbox?.add(selectedSeat1)
        calculatePrice()
    }

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
        if (seat.st === 1) {
            titleText.setText("Are you sure you want to book a Wheelchair-friendly seat?")
            messagePcTextView.setText("Please do not book it if you are not on a wheelchair. You may be requested to move to accommodate.")
            titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            icon.setImageResource(R.drawable.hc_icon)
        } else {
            titleText.setText("Are you sure you want to book a Companion seat?")
            messagePcTextView.setText("You have selected a Wheelchair Companion Seat. You may be requested to move to accommodate.")
            titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            icon.setImageResource(R.drawable.ic_caf)
        }
        //        }
        delete.setText("Change Seat")
        delete.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.setText("Confirm Seat")
        cancel.setOnClickListener(View.OnClickListener {
            hc_seat = true
            //                ca_seat = false;
            if (seat.cos === true && coupol_seat == 0) {
                coupol_seat = 1
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
            println("select_buddy=============1===$select_buddy")
            if (select_buddy == true) {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    messahe_text,
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            } else {
                if (selectedSeats.size < 10) {
                    if (select_buddy && seat.bu === true) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            messahe_text,
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    } else {
                        if (selectedSeats.size > 0 && seat.bu === true) {
                            val dialog = OptionDialog(this,
                                R.mipmap.ic_launcher,
                                R.string.app_name,
                                messahe_text,
                                positiveBtnText = R.string.ok,
                                negativeBtnText = R.string.no,
                                positiveClick = {
                                },
                                negativeClick = {
                                })
                            dialog.show()
                        } else {
                            seat.s=Constant.SEAT_SELECTED
                            if (noOfRows_small?.size!! > 0) noOfRows_small?.get(num1)?.s?.get(num2)
                                ?.s=Constant.SEAT_SELECTED
                            if (seat.st === 1) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_hcseaty)
                            } else if (seat.st === 2) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.ic_campy)
                            } else if (seat.st === 3) {
                                seatView.text = ""
                                seatView.setBackgroundResource(R.drawable.buddyw)
                            } else {
                                seatView.setBackgroundResource(R.drawable.ic_selected)
                                seatView.setTextColor(resources.getColor(R.color.black))
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
        })
        dialog.show()
    }

    private fun removeHC(context: Activity, seatView: TextView, num1: Int, num2: Int) {
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
        titleText.text = "Are you sure you want to remove a Wheelchair and Companion seat?"
        messagePcTextView.text =
            "If you will remove wheelchair then corresponding Companion seat will be remove also."
        titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        icon.setImageResource(R.drawable.hc_icon)
        delete.text = "NO"
        delete.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.text = "YES"
//        cancel.setOnClickListener(View.OnClickListener {
//            seat.setS(PCConstants.SEAT_AVAILABEL)
//            //  System.out.println("Handicaped" + seat.getHc());
//            if (seat.st === 1) {
//                hc_seat = false
//            } else if (seat.st === 2) {
//                ca_seat = false
//            } else if (seat.st === 3) {
//                select_buddy = false
//            }
//            if (seat.st === 1) {
//                seatView.text = ""
//                seatView.setBackgroundResource(R.drawable.ic_hcseat)
//            } else if (seat.st === 2) {
//                seatView.text = ""
//                seatView.setBackgroundResource(R.drawable.ic_camp)
//            } else if (seat.st === 3) {
//                seatView.text = ""
//                seatView.setBackgroundResource(R.drawable.buddy)
//            } else {
//                seatView.setBackgroundResource(R.drawable.ic_vacant)
//                seatView.setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
//            }
//            if (noOfRows_small!!.size > 0) noOfRows_small[num1].getS().get(num2)
//                .setS(Constant.SEAT_AVAILABEL)
//            removeSelectedSeats(seat)
//            for (i in 0 until selectedSeats1.size) {
//                //   System.out.println("selectedSeats1--->" + selectedSeats1.get(i).getSt());
//                flag_hc = true
//                if (selectedSeats1[i].getSt() === 1 || selectedSeats1[i].getSt() === 2) {
//                    val seat = selectedSeats1[i].getSeatView().getTag() as SeatTagData
//                    seat.setS(Constant.SEAT_AVAILABEL)
//                    //  System.out.println("Handicaped" + seat.getHc());
//                    if (seat.st === 1) {
//                        hc_seat = false
//                    } else if (seat.st === 2) {
//                        ca_seat = false
//                    } else if (seat.st === 3) {
//                        select_buddy = false
//                    }
//                    if (seat.st === 1) {
//                        selectedSeats1[i].getSeatView().setText("")
//                        selectedSeats1[i].getSeatView().setBackgroundResource(R.drawable.ic_hcseat)
//                    } else if (seat.st === 2) {
//                        selectedSeats1[i].getSeatView().setText("")
//                        selectedSeats1[i].getSeatView().setBackgroundResource(R.drawable.ic_camp)
//                    } else if (seat.st === 3) {
//                        selectedSeats1[i].getSeatView().setText("")
//                        selectedSeats1[i].getSeatView().setBackgroundResource(R.drawable.buddy)
//                    } else {
//                        selectedSeats1[i].getSeatView().setBackgroundResource(R.drawable.ic_vacant)
//                        selectedSeats1[i].getSeatView()
//                            .setTextColor(resources.getColor(R.color.black_with_fifteen_opacity))
//                    }
//                    if (noOfRows_small.size > 0) noOfRows_small[selectedSeats1[i].getNum1()].getS()
//                        .get(
//                            selectedSeats1[i].getNum2()
//                        ).setS(Constant.SEAT_AVAILABEL)
//                    removeSelectedSeats(seat)
//                }
//            }
//            flag_hc = false
//            dialog.dismiss()
//        })
        dialog.show()
    }

}