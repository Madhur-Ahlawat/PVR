package com.net.pvr1.ui.seatLayout

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySeatLayoutBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.seatLayout.response.Price
import com.net.pvr1.ui.seatLayout.response.Seat
import com.net.pvr1.ui.seatLayout.response.SeatResponse
import com.net.pvr1.ui.seatLayout.response.SeatTagData
import com.net.pvr1.ui.seatLayout.viewModel.SeatLayoutViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class SeatLayoutActivity : AppCompatActivity() {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySeatLayoutBinding? = null
    private val authViewModel: SeatLayoutViewModel by viewModels()
    private var loader: LoaderDialog? = null


    private var priceMap: Map<String, Price>? = null
    private var selectedSeats = ArrayList<Seat>()
    private var noOfSeatsSelected = ArrayList<SeatTagData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatLayoutBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        authViewModel.seatLayout(
            "GURM", "251212", "", "", "", false, ""
        )
        seatLayout()
    }

    private fun seatLayout() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output.rows)
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
//        binding?.seatLayoutUi?.show()
        binding?.llSeatlayout?.removeAllViews()
        binding?.llRowName?.removeAllViews()
        var areaName = ""

        for (i in noOfRows.indices) {
            val row: SeatResponse.Output.Row = noOfRows[i]
            val noSeats: List<SeatResponse.Output.Row.S> = row.s
            if (noSeats.isNotEmpty()) {
                //draw extras space for row name
                addRowName(row.n, false)
                //Draw seats ==================
                val linearLayout = LinearLayout(applicationContext)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linearLayout.layoutParams = layoutParams
                binding?.llSeatlayout?.addView(linearLayout)
                dhowRow(linearLayout, noSeats, areaName)
            } else {
                //update area
                areaName = row.n

                //draw extras space for row name
                addRowName("", true)
                //update area

                //Draw Area==================
                val rlLayout = RelativeLayout(applicationContext)
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, Constant().convertDpToPixel(
                        25f,
                        applicationContext
                    )
                )
                rlLayout.layoutParams = layoutParams

//                if (noSeats.c != null) {
//                    var colorCodes: String?
//                    if (row.c.contains("#")) colorCodes = row.c else colorCodes =
//                        "#" + row.c
//                    rlLayout.setBackgroundColor(Color.parseColor(colorCodes))
//                }
                val centerLayout = LinearLayout(applicationContext)
                val centerLayoutParameter =
                    RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                centerLayoutParameter.addRule(RelativeLayout.CENTER_IN_PARENT)
                centerLayout.gravity = Gravity.CENTER_VERTICAL
                centerLayout.orientation = LinearLayout.HORIZONTAL
                centerLayout.layoutParams = centerLayoutParameter
                val downButtonLeft = ImageButton(applicationContext)
                val layoutParameter1 = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                downButtonLeft.layoutParams = layoutParameter1
                downButtonLeft.setPadding(0, 0, 0, 0)
                downButtonLeft.setBackgroundColor(Color.TRANSPARENT)
                centerLayout.addView(downButtonLeft)
                val textView = TextView(applicationContext)
                textView.layoutParams = layoutParameter1
                textView.gravity = Gravity.CENTER
                textView.setTextColor(getColor(R.color.yellow))
                textView.text = row.n
                val face = ResourcesCompat.getFont(applicationContext, R.font.sf_pro_text_regular)
                textView.typeface = face
                textView.setPadding(0, 0, 0, 15)
                centerLayout.addView(textView)
                val downButtonRight = ImageButton(applicationContext)
                downButtonRight.layoutParams = layoutParameter1
                downButtonRight.setPadding(0, 0, 0, 0)
                downButtonRight.setBackgroundColor(Color.TRANSPARENT)
                centerLayout.addView(downButtonRight)
                rlLayout.addView(centerLayout)
                binding?.llSeatlayout?.addView(rlLayout)
            }
        }
    }

    private fun dhowRow(
        llDrawRow: LinearLayout,
        noSeats: List<SeatResponse.Output.Row.S>,
        areaName: String
    ) {
        for (i in noSeats.indices) {
            val seat: SeatResponse.Output.Row.S = noSeats[i]
            if (seat.b != "") {
                val seatView = TextView(applicationContext)
                seatView.setBackgroundColor(Color.TRANSPARENT)
                val face = ResourcesCompat.getFont(applicationContext, R.font.sf_pro_text_regular)
                seatView.typeface = face
                seatView.gravity = Gravity.CENTER
                seatView.textSize = 10f
                seatView.id = i

                val layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(5, 5, 5, 5)
                seatView.layoutParams = layoutParams
                when (seat.s) {
                    Constant.SEAT_AVAILABEL -> {
                        seatView.text = seat.sn.replace("[^\\d.]".toRegex(), "")
                        seatView.setTextColor(getColor(R.color.white))
                        seatView.setPadding(35, 12, 35, 12)
                    }
                    Constant.SEAT_SELECTED -> {
                        val seatTagData = SeatTagData()
                        seatTagData.b = (seat.b)
                        seatTagData.setC(seat.c)
                        seatTagData.s = (seat.s)
                        seatTagData.sn = (seat.sn)
                        seatTagData.st = (seat.st)
                        seatTagData.area = (areaName)
                        addSelectedSeats(seatTagData)
                        seatView.setTextColor(getColor(R.color.yellow))
                        seatView.setBackgroundResource(R.drawable.seat_btn_corner)
                    }
                    Constant.SEAT_BOOKED -> {
                        seatView.setTextColor(Color.parseColor("#c6c9d0"))
                        seatView.setBackgroundResource(R.drawable.seat_selected_bg)
                    }
                    else -> {
                        seatView.setBackgroundColor(Color.TRANSPARENT)
                    }
                }

                //create tag for seat layout
                val seatTagData = SeatTagData()
                seatTagData.b = (seat.b)
                seatTagData.setC(seat.c)
                seatTagData.s = (seat.s)
                seatTagData.sn = (seat.sn)
                seatTagData.area = (areaName)
                seatTagData.hc = (seat.hc)
                seatTagData.bu = (seat.bu)
                seatTagData.st = (seat.st)
                seatTagData.cos = (seat.cos)
                seatView.tag = seatTagData

                //make click listener for seatView
                setClick(seatView)
                llDrawRow.addView(seatView)
            } else {
                val seatView = TextView(applicationContext)
                val face = ResourcesCompat.getFont(applicationContext, R.font.sf_pro_text_regular)
                seatView.typeface = face
                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    Constant().convertDpToPixel(20f, applicationContext),
                    Constant().convertDpToPixel(20f, applicationContext)
                )
                seatView.layoutParams = layoutParams
                val margin: Int = Constant().convertDpToPixel(2f, applicationContext)
                layoutParams.setMargins(margin, margin, margin, margin)
                seatView.setBackgroundColor(Color.TRANSPARENT)
                llDrawRow.addView(seatView)
            }
        }
    }

    private fun addRowName(rowName: String, space: Boolean) {
        if (space) {
            val txtRowName = TextView(applicationContext)
            val face = ResourcesCompat.getFont(applicationContext, R.font.sf_pro_text_bold)
            txtRowName.typeface = face
            txtRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            val layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(15, 5, 15, 5)
            txtRowName.gravity = Gravity.CENTER
            txtRowName.setPadding(10, 0, 10, 10)
            txtRowName.layoutParams = layoutParams
            binding?.llRowName?.addView(txtRowName)
        } else {
            //Area layout design ==========================
            val txtRowName = TextView(applicationContext)
            val face = ResourcesCompat.getFont(applicationContext, R.font.sf_pro_text_regular)
            txtRowName.typeface = face
            txtRowName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            txtRowName.setTextColor(Color.parseColor("#534d4d"))
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(20f, applicationContext)
            )

            val margin: Int = Constant().convertDpToPixel(4f, applicationContext)
            txtRowName.gravity = Gravity.CENTER
            layoutParams.setMargins(
                0,
                Constant().convertDpToPixel(4f, applicationContext),
                0,
                margin
            )
            txtRowName.layoutParams = layoutParams
            txtRowName.text = rowName
            txtRowName.setTextColor(getColor(R.color.yellow))

            binding?.llRowName?.addView(txtRowName)
        }
    }

    private fun addSelectedSeats(seat: SeatTagData) {
        val selectedSeat = Seat()
        selectedSeat.seatBookingId = (seat.b)
        val price: Price = priceMap!![seat.getC()]!!
        selectedSeat.priceCode = (price.priceCode)
        noOfSeatsSelected.add(seat)
        selectedSeats.add(selectedSeat)

        calculatePrice()
    }


    private fun setClick(seatView: TextView) {
        seatView.setOnClickListener {
            val seat: SeatTagData = seatView.tag as SeatTagData
            if (seat.s == Constant.SEAT_AVAILABEL) {
                if (selectedSeats.size < 10) {
                    seat.s = Constant.SEAT_SELECTED
                    seatView.setTextColor(getColor(R.color.black))
                    seatView.setPadding(35, 12, 35, 12)
                    seatView.setBackgroundResource(R.drawable.seat_select_bg)

                    addSelectedSeats(seat)
                } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        getString(
                            R.string.max_seat_msz
                        ),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
            }
            else if (seat.s == Constant.SEAT_SELECTED) {
                seat.s = Constant.SEAT_AVAILABEL
                seatView.setTextColor(getColor(R.color.white))
                seatView.setPadding(35, 12, 35, 12)
                seatView.setBackgroundResource(R.drawable.select_seat_bg_trans)
                removeSelectedSeats(seat)
            }
        }
    }

    private fun removeSelectedSeats(seat: SeatTagData) {
        for (i in selectedSeats.indices) {
            if (seat.b == selectedSeats[i].seatBookingId) {
                noOfSeatsSelected.removeAt(i)
                selectedSeats.removeAt(i)
                println("selectedSeats-->"+noOfSeatsSelected)

                break
            }
        }

        if (noOfSeatsSelected.size == 0) {
//            binding?.seatSelect?.text = "No Seats Selected"
//            binding?.seatList?.text = ""
        }
        calculatePrice()
    }
    private fun calculatePrice() {
        println("removeSeatSelect--->${noOfSeatsSelected.size}")
//        binding?.seatList?.text = ""
        if (noOfSeatsSelected.size > 0) {
//            binding?.view41?.show()
//            binding?.seatList?.show()
//            binding?.seatSelect?.text = noOfSeatsSelected.size.toString() + " Seats Selected"
        } else {
//            binding?.view41?.hide()
//            binding?.seatList?.hide()
//            selectSeatPriceCode.clear()
//            binding?.seatSelect?.text = "No Seats Selected"
        }
        var totalPrice = 0f
        val selectSeat = ArrayList<String>()
        var bigDecimal = BigDecimal(0)
        for (i in noOfSeatsSelected.indices) {
            try {
                val seatTagData = noOfSeatsSelected[i]
                val price = priceMap!![seatTagData.getC()]
                val seatNo: String =
                    if (binding?.seatList?.text.toString().equals("", ignoreCase = true)) {
                        "Seats: " + seatTagData.sn.toString()
                    } else {
                        "," + seatTagData.sn
                    }
                binding?.seatList?.append(seatNo)

                selectSeat.add(seatNo)
                bigDecimal = BigDecimal(totalPrice.toString()).add(BigDecimal(price?.price))
                totalPrice = bigDecimal.toFloat()

                println("SeatNumberPrice---->${selectSeat}")
            } catch (e: Exception) {
                println("exception--->${e.message}")
            }
        }
//Price Total
        binding?.textView130?.text =
            getString(R.string.currency) + " " + Constant().removeTrailingZeroFormatter(
                bigDecimal.toFloat()
            )

    }

}