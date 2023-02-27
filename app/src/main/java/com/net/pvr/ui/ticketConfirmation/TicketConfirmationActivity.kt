package com.net.pvr.ui.ticketConfirmation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.databinding.ActivityTicketConfirmatonBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.ticketConfirmation.adapter.TicketFoodAdapter
import com.net.pvr.ui.ticketConfirmation.adapter.TicketPlaceHolderAdapter
import com.net.pvr.ui.ticketConfirmation.adapter.TicketSeatAdapter
import com.net.pvr.ui.ticketConfirmation.response.TicketBookedResponse
import com.net.pvr.ui.ticketConfirmation.viewModel.TicketConfirmationViewModel
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TicketConfirmationActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityTicketConfirmatonBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: TicketConfirmationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketConfirmatonBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        if (Constant.BOOK_TYPE == "BOOKING") {
            if (Constant.TRANSACTION_ID==""){
                authViewModel.singleTicket(
                    Constant.BOOKING_ID,
                    preferences.getUserId(),
                    Constant.BOOK_TYPE
                )
            }else {
                authViewModel.ticketConfirmation(
                    Constant.BOOK_TYPE,
                    Constant.BOOKING_ID,
                    preferences.getUserId(),
                    "no",
                    "no",
                    "",
                    "no",
                    "",
                    ""
                )
            }

        } else if (Constant.BOOK_TYPE == "FOOD") {
            authViewModel.fnbTicket(
                Constant.BOOKING_ID,
                preferences.getUserId(),
                Constant.BOOK_TYPE
            )
        }
        ticketDetails()
        singleTicketPrint()
        fnbTicketPrint()
        movedNext()
        bookParking()
        showParking()
    }

    private fun movedNext() {
        binding?.include31?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun ticketDetails() {
        binding?.include31?.textView108?.text = "Ticket Confirmed"
        authViewModel.liveDataScope.observe(this) {
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
    @SuppressLint("SetTextI18n")
    private fun singleTicketPrint() {
        binding?.include31?.textView108?.text = "Your Order"
        authViewModel.singleTicketDataScope.observe(this) {
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
    @SuppressLint("SetTextI18n")
    private fun fnbTicketPrint() {
        binding?.include31?.textView108?.text = "Your Order"
        authViewModel.fnbTicketDataScope.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        try {
                            retrieveData(it.data.output)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
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

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: TicketBookedResponse.Output) {
        printLog(Constant.BOOK_TYPE+output.toString())

        if (Constant.BOOK_TYPE == "BOOKING") {
            binding?.foodView?.hide()
            binding?.ticketView?.show()
            binding?.bottomView?.show()
            if (output.food.isNotEmpty()){
                binding?.imageView153?.show()
                binding?.textView339?.show()
                binding?.recyclerView48?.show()
            }
            if (output.fa && output.ca_d != "true"){
                binding?.cardView16?.show()
                binding?.imageView157?.show()
            }

            if (output.ca_d == "true"){
                binding?.constraintLayout123?.hide()
                binding?.cushineView?.hide()
                binding?.imageView157?.hide()
                binding?.textView346?.hide()
                binding?.ivCancelimage?.show()
                binding?.llRefundCard?.show()
                binding?.tvDiscountTxt?.show()
                binding?.textView347?.text = "Book Again"
                binding?.tvRefundTxtTitle?.text = output.ca_r_bot_txtb
                binding?.tvRefundTxt?.text = output.ca_r_bot_txt
                binding?.tvDiscountTxt?.text = output.ca_r_txt
            }else{
                binding?.constraintLayout123?.show()
                binding?.cushineView?.show()
                binding?.imageView157?.show()
                binding?.textView346?.show()
                binding?.ivCancelimage?.hide()
                binding?.tvDiscountTxt?.hide()
                binding?.llRefundCard?.hide()
                binding?.textView347?.text = "Add Location"
            }

            binding?.textView347?.setOnClickListener {
                if (binding?.textView347?.text == "Book Again"){
                    launchActivity(
                        HomeActivity::class.java,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                }else{

                }
            }

            // Cancel Ticket

            if (!output.partialCancellationAllowed || output.seat.size == 1)
                binding?.textView363?.text = resources.getString(R.string.cancel_ticket_small)
            else binding?.textView363?.text = resources.getString(R.string.Partially_Cancel)

            if (Constant.TRANSACTION_ID == "" && output.ca_a && output.ca_d != "true") {
                binding?.linearLayout13?.show()
            } else {
                binding?.linearLayout13?.hide()
            }

            binding?.cancelTicket?.setOnClickListener {
//                if (!output.partialCancellationAllowed || output.seat.size == 1){
//                    cancelBookingDialog(output)
//                }else{
                    partialCancelBookingDialog(output)
              //  }
            }

            //title
            binding?.textView131?.text = output.m
            //movie Type
            binding?.textView329?.text = output.cen+" "+output.lg + "(${output.fmt})"
            //address
            binding?.textView331?.text = output.c
            //load Image
            Glide.with(this)
                .load(output.im)
                .error(R.drawable.placeholder_vertical)
                .into(binding?.imageView151!!)
            //map
            binding?.imageView152?.setOnClickListener {
                Constant().openMap(this, output.ltd, output.lngt)
            }
            //Date
            binding?.textView334?.text = output.md
            //time
            binding?.textView336?.text = output.stgs
            //seat
            binding?.textView338?.text = output.audi + output.st
            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.STRETCH
            val adapter = TicketSeatAdapter(output.seat)
            binding?.recyclerView47?.setHasFixedSize(true)
            binding?.recyclerView47?.layoutManager = layoutManager
            binding?.recyclerView47?.adapter = adapter

            //order Id
            binding?.textView342?.text = output.bi
            //kiosk Id
            binding?.textView344?.text = output.qrc
            //Qr Code
            Glide.with(this)
                .load(output.qr)
                .error(R.drawable.pvr_logo)
                .into(binding?.imageView156!!)

            binding?.imageView156?.setOnClickListener {
                oPenDialogQR(output.qr)
            }

            //Pillow Info
            binding?.textView345?.setOnClickListener {
                showChildDialog()
            }

            //food
            val layoutManagerFood = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val ticketFoodAdapter = TicketFoodAdapter(output.food)
            binding?.recyclerView48?.setHasFixedSize(true)
            binding?.recyclerView48?.layoutManager = layoutManagerFood
            binding?.recyclerView48?.adapter = ticketFoodAdapter

            //placeholder
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding?.recyclerView51)
            val layoutManagerPlaceHolder =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val ticketPlaceHolderAdapter = TicketPlaceHolderAdapter(this, output.ph)
            binding?.recyclerView51?.setHasFixedSize(true)
            binding?.recyclerView51?.layoutManager = layoutManagerPlaceHolder
            binding?.recyclerView51?.adapter = ticketPlaceHolderAdapter


            //Direction
            binding?.constraintLayout129?.setOnClickListener {
                Constant().openMap(this, output.ltd, output.lngt)
            }
            // Total Values
            binding?.textView349?.text = output.ft
            binding?.textView360?.text = output.bi

            for (data in output.f) {
                if (data.n == "Tickets") {
                    binding?.textView350?.text = output.f[0].it[0].n
                    binding?.textView351?.text = output.f[0].it[0].v
                }else if (data.n == "Food & Beverages") {
                    if (output.food.isNotEmpty()) {
                        binding?.textView352?.show()
                        binding?.textView353?.show()
                        binding?.textView352?.text = output.f[1].c.toString() + " Food Items"
                        binding?.textView353?.text = output.f[1].v
                    }
                }else if (data.n == "Taxes & Fees "){
                    binding?.textView357?.text = data.it[0].v
                    binding?.textView358?.text = data.it[1].v
                    binding?.textView356?.text = data.it[1].n

                }else if (data.n == "CAUVERY CALLING"){
                    binding?.donTitle?.text = data.n
                    binding?.donVal?.text = data.v
                }
            }

            //Privilege points

            val ls = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)

            if (ls == ""){
                if (output.pe == "") {
                    binding?.ppView?.hide()
                    binding?.view226?.hide()
                } else {
                    binding?.ppView?.show()
                    binding?.view226?.show()
                    val text =
                        "<font color=#000000>You will earn </font> <font color=#000000><b>" + output.pe + "</b></font><font color=#000000> PVR Privilege Points </font>"

                    binding?.textView361?.text =Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                }
            }else{
                binding?.ppView?.show()
                binding?.view226?.show()
                binding?.textView361?.text = getString(R.string.you_privilege)
            }

            //Parking

            if (output.parking){
                binding?.textView368?.text = output.ptext
            }else{
                binding?.textView368?.text = "Back To Home"
            }

            binding?.textView368?.setOnClickListener {
                if (binding?.textView368?.text == "Back To Home"){
                    launchActivity(
                        HomeActivity::class.java,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                }else{
                    if (output.parkbooking) {
                        authViewModel.showParking(output.bi)
                    } else {
                        authViewModel.bookParking(output.bi)
                    }
                }
            }

            // Share
            binding?.shareBtn?.setOnClickListener {
                try{
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, output.ms + " " + output.tu)
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            //Download
            binding?.downloadBtn?.setOnClickListener {
                try{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(output.tu)))
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }


        } else if (Constant.BOOK_TYPE == "FOOD") {
            binding?.foodView?.show()
            binding?.ticketView?.hide()
            binding?.bottomView?.hide()
            Glide.with(this)
                .load(output.im)
                .error(R.drawable.placeholder_vertical)
                .into(binding?.movieImgFood!!)

            binding?.movieNameFood?.text = output.m
            //movie Type
            binding?.cencorId?.text = output.cen + " " + output.lg + "(${output.fmt})"

            //address
            binding?.cinemaFood?.text = output.c
            //Date
            binding?.dateTxt?.text = output.md
            //time
            binding?.timeText?.text = output.stgs
            binding?.foodOrderId?.text = output.bi
            binding?.foodPrice?.text = output.ft
            binding?.foodCount?.text = output.food.size.toString() + " Food Items Ordered"
            //food
            val layoutManagerFood = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val ticketFoodAdapter = TicketFoodAdapter(output.food)
            binding?.foodItems?.setHasFixedSize(true)
            binding?.foodItems?.layoutManager = layoutManagerFood
            binding?.foodItems?.adapter = ticketFoodAdapter

            binding?.direction?.setOnClickListener {
                Constant().openMap(this, output.ltd, output.lngt)
            }

            binding?.viewDetails?.setOnClickListener {
                if (binding?.foodItems?.visibility == View.VISIBLE) {
                    binding?.foodItems?.hide()
                    binding?.viewDetails?.text = "View Details"
                    binding?.viewDetails?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.arrow_down,
                        0
                    )
                } else {
                    binding?.foodItems?.show()
                    binding?.viewDetails?.text = "View Less"
                    binding?.viewDetails?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.arrow_up,
                        0
                    )
                }
            }
        }
    }



    //Parking api

    private fun bookParking() {
        authViewModel.bookParkResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val data =it.data.output.url + "?partner_name=" + it.data.output.partner_name + "&booking_id=" + it.data.output.booking_id + "&location_id=" + it.data.output.location_id + "&date=" + it.data.output.date + "&time=" + it.data.output.time + "&duration=" + it.data.output.duration + "&hmac=" + it.data.output.hmac

                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra("title", "View Parking")
                        intent.putExtra("from", "parking")
                        intent.putExtra("getUrl", data)
                        startActivity(intent)
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun showParking() {
        authViewModel.showParkingResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.putExtra("title", "View Parking")
                        intent.putExtra("from", "parking")
                        intent.putExtra("getUrl", it.data.output.url)
                        startActivity(intent)
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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    // Show full qr

    private fun oPenDialogQR(qrCode:String) {
        val dialogQR = Dialog(this)
        dialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogQR.setCancelable(false)
        dialogQR.setContentView(R.layout.activity_privilege_details)
        dialogQR.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogQR.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogQR.window?.setGravity(Gravity.CENTER)
        dialogQR.setTitle("")
        val pointsPcTextView = dialogQR.findViewById<TextView>(R.id.points_txt)
        val vouchersPcTextView = dialogQR.findViewById<TextView>(R.id.vouchers_txt_)
        val llFirst = dialogQR.findViewById<LinearLayout>(R.id.llFirst)
        val tvUserName: TextView = dialogQR.findViewById<View>(R.id.TVusername) as TextView
        val ivImage = dialogQR.findViewById<View>(R.id.ivImage) as ImageView
        val tvLogo = dialogQR.findViewById<View>(R.id.tvLogo) as TextView
        val tvCross = dialogQR.findViewById<View>(R.id.tvCross) as ImageView
        Glide.with(this).load(qrCode).into(ivImage)
        tvUserName.hide()
        llFirst.hide()
        vouchersPcTextView.hide()
        tvLogo.hide()
        tvCross.setOnClickListener { dialogQR.dismiss() }
        dialogQR.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogQR.dismiss()
            }
            true
        }
        dialogQR.show()
    }

    // Child Pillow info

    private fun showChildDialog() {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.child_info_popup)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val close: TextView = dialog.findViewById<View>(R.id.close) as TextView
        close.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    // Cancellation

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    private fun partialCancelBookingDialog(output: TicketBookedResponse.Output) {
        try {
            val dialogQR = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
            dialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogQR.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialogQR.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogQR.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogQR.setCancelable(false)
            dialogQR.setContentView(R.layout.partial_layout)
            dialogQR.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogQR.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialogQR.window!!.setGravity(Gravity.CENTER)
            dialogQR.setTitle("")
            val rbEntireBooking = dialogQR.findViewById<RadioButton>(R.id.rbEntire_Booking)
            val rbSelected = dialogQR.findViewById<RadioButton>(R.id.rbSelected)
            val tvCounterAdult = dialogQR.findViewById<TextView>(R.id.tvCounter_Adult)
            val tvAdultText = dialogQR.findViewById<TextView>(R.id.tvAdultText)
            val tvCancel = dialogQR.findViewById<TextView>(R.id.tvCancel)
            val tvContinue = dialogQR.findViewById<TextView>(R.id.tvContinue)
            val minusAdult = dialogQR.findViewById<TextView>(R.id.minus_Adult)
            val plusAdult = dialogQR.findViewById<TextView>(R.id.plus_Adult)
            tvCounterAdult?.text = "0"
            val tvRefund = dialogQR.findViewById<TextView>(R.id.tvRefund)
            val tvfoodvoucher = dialogQR.findViewById<TextView>(R.id.tvfood_voucher)
            val rbFoodVoucher = dialogQR.findViewById<RadioButton>(R.id.rbFood_Voucher)
            val rbRefund = dialogQR.findViewById<RadioButton>(R.id.rbRefund)
            val llFull = dialogQR.findViewById<LinearLayout>(R.id.llFull)
            val llNormal = dialogQR.findViewById<LinearLayout>(R.id.llNormal)
            tvContinue?.text = "NEXT"
            rbFoodVoucher?.setOnClickListener {
                rbFoodVoucher.isChecked = true
                rbRefund?.isChecked = false
                tvfoodvoucher?.show()
                tvRefund?.hide()
            }
            rbRefund?.setOnClickListener {
                rbFoodVoucher?.isChecked = false
                rbRefund.isChecked = true
                tvfoodvoucher?.hide()
                tvRefund?.show()
            }
            tvAdultText?.setTextColor(getColor(R.color.gray_))
            tvCounterAdult?.setTextColor(getColor(R.color.gray_))
            minusAdult?.background = getDrawable(R.drawable.ic_minus_grey)
            plusAdult?.background = getDrawable(R.drawable.ic_add_grey)
            rbEntireBooking?.isChecked = true
            rbEntireBooking?.setOnClickListener {
                tvContinue?.text = "NEXT"
                rbSelected?.isChecked = false
                rbEntireBooking.isChecked = true
                tvCounterAdult?.text = "0"
                tvContinue?.isEnabled = true
                tvContinue?.setTextColor(getColor(R.color.pvr_dark_black))
                tvAdultText?.setTextColor(getColor(R.color.gray_))
                tvCounterAdult?.setTextColor(getColor(R.color.gray_))
                minusAdult?.background = getDrawable(R.drawable.ic_minus_grey)
                plusAdult?.background = getDrawable(R.drawable.ic_add_grey)
            }
            minusAdult?.setOnClickListener{
                if (rbSelected?.isChecked == true) {
                    val count: Int = check(tvCounterAdult!!, output.seat.size, true)
                    if (count > 0) changecolor(
                        tvContinue!!,
                        true
                    ) else changecolor(
                        tvContinue!!,
                        false
                    )
                }
            }
            plusAdult?.setOnClickListener{
                if (rbSelected?.isChecked == true) {
                    val count: Int = check(
                        tvCounterAdult!!,
                        output.seat.size,
                        false
                    )
                    if (count > 0) changecolor(
                        tvContinue!!,
                        true
                    ) else changecolor(
                        tvContinue!!,
                        false
                    )
                }
            }
            rbSelected?.setOnClickListener {
                tvContinue?.text = "CONFIRM"
                rbEntireBooking?.isChecked = false
                rbSelected.isChecked = true
                tvContinue?.isEnabled = true
                tvContinue?.setTextColor(getColor(R.color.pvr_dark_black))
                tvAdultText?.setTextColor(getColor(R.color.black_color_data))
                tvCounterAdult?.setTextColor(getColor(R.color.black_color_data))
                minusAdult?.background = getDrawable(R.drawable.ic_minus)
                plusAdult?.background = getDrawable(R.drawable.ic_add)
            }
            tvCancel?.setOnClickListener{ dialogQR.dismiss() }
            if (output.seat.size == 1 || !output.partialCancellationAllowed) {
                tvContinue?.text = "CONFIRM"
                rbFoodVoucher?.isChecked = true
                llFull?.visibility = View.VISIBLE
                llNormal?.visibility = View.GONE
            }
            tvContinue?.setOnClickListener{
                if (rbEntireBooking?.isChecked == true || rbSelected?.isChecked == true) {
                    if (llFull?.visibility == View.VISIBLE && rbFoodVoucher?.isChecked == true && tvContinue.text.toString()==("CONFIRM")) {

                        dialogQR.dismiss()
                    } else if (llFull?.visibility == View.VISIBLE && rbRefund?.isChecked == true) {
                        dialogQR.dismiss()
                    } else if (rbEntireBooking?.isChecked == true && tvContinue.text.toString()==("NEXT")) {
                        tvContinue.text = "CONFIRM"
                        rbFoodVoucher?.isChecked = true
                        dialogQR.dismiss()
                    } else if (rbSelected?.isChecked == true) {

                        // buttonClickedpartial((Activity) context, data, t_type, cancelImageView, bookAgainLinearLayout, cancelLinearLayout, llCustomize, totalAmountView, refundAmt, onlyBookButtonLayout, bookingsLinearLayout, qrCodeLayout, audiAboveImageView, tvCounter_Adult.getText().toString());
                        dialogQR.dismiss()
                    }
                }
            }
            dialogQR.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun cancelBookingDialog(output: TicketBookedResponse.Output) {
        // custom dialog
        val messagePcTextView: TextView
        val cancel: TextView
        val delete: TextView
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setTitle("")
        dialog.setContentView(R.layout.ticket_cancel)

        // set the custom dialog components - text, image and button
        messagePcTextView = dialog.findViewById<View>(R.id.message) as TextView
        delete = dialog.findViewById<View>(R.id.no) as TextView
        messagePcTextView.text = output.ca_msg
        //   messagePcTextView.setText("");
        delete.text = "NO"
        delete.setOnClickListener{ dialog.dismiss() }
        cancel = dialog.findViewById<View>(R.id.yes) as TextView
        cancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

   @SuppressLint("SetTextI18n")
   private fun check(tvCounter_Adult: TextView, seats: Int, isMinus: Boolean): Int {
        var counttext: Int = tvCounter_Adult.text.toString().toInt()
        if (isMinus) {
            if (counttext > 0) {
                counttext--
                tvCounter_Adult.text = counttext.toString() + ""
            }
        } else {
            val seatsint = seats - 1
            if (counttext < seatsint) {
                counttext++
                tvCounter_Adult.text = counttext.toString() + ""
            }
        }
        return counttext
    }

    private fun changecolor(pcTextView: TextView, isEnable: Boolean) {
        if (isEnable) {
            pcTextView.setTextColor(getColor(R.color.pvr_dark_black))
            pcTextView.isEnabled = true
        } else {
            pcTextView.setTextColor(getColor(R.color.gray_))
            pcTextView.isEnabled = false
        }
    }

    override fun onBackPressed() {
        launchActivity(
            HomeActivity::class.java,
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }
}