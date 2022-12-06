package com.net.pvr1.ui.ticketConfirmation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityTicketConfirmatonBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.ticketConfirmation.adapter.TicketFoodAdapter
import com.net.pvr1.ui.ticketConfirmation.adapter.TicketSeatAdapter
import com.net.pvr1.ui.ticketConfirmation.response.TicketBookedResponse
import com.net.pvr1.ui.ticketConfirmation.viewModel.TicketConfirmationViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TicketConfirmationActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityTicketConfirmatonBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: TicketConfirmationViewModel by viewModels()
    private var qrBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketConfirmatonBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        if (intent.getStringExtra("type") == "myBooking") {
            authViewModel.ticketConfirmation(
                intent.getStringExtra("bookType").toString(),
                intent.getStringExtra("bookingId").toString(),
                preferences.getUserId(),
                "no",
                "no",
                "",
                "no",
                "",
                ""
            )

        } else {
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
        ticketDetails()
        movedNext()
    }

    private fun movedNext() {
        binding?.include31?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun ticketDetails() {
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun retrieveData(output: TicketBookedResponse.Output) {
        //title
        binding?.textView131?.text = output.m
        //movie Type
        binding?.textView329?.text = output.cen
        //language
        binding?.textView330?.text = output.lg + "(${output.fmt})"
        //address
        binding?.textView331?.text = output.c
        //load Image
        Glide.with(this)
            .load(output.im)
            .error(R.drawable.pvr_logo)
            .into(binding?.imageView151!!)
        //map
        binding?.imageView152?.setOnClickListener {
            Constant().openMap(this,output.ltd,output.lngt)
        }
        //Date
        binding?.textView334?.text=output.md
        //time
        binding?.textView336?.text=output.t
        //seat
        binding?.textView338?.text=output.audi+ output.st
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.alignItems = AlignItems.STRETCH
        val adapter = TicketSeatAdapter(output.seat)
        binding?.recyclerView47?.setHasFixedSize(true)
        binding?.recyclerView47?.layoutManager = layoutManager
        binding?.recyclerView47?.adapter = adapter

        //order Id
        binding?.textView342?.text=output.bi
        //kiosk Id
        binding?.textView344?.text= output.mc
        //Qr Code
        qrBitmap = Constant().createQrCode(output.qr)
        binding?.imageView156?.setImageBitmap(qrBitmap)
        //note
//        binding?.textView365?.text=""
        //food
        val layoutManagerFood = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val ticketFoodAdapter = TicketFoodAdapter(output.food)
        binding?.recyclerView47?.setHasFixedSize(true)
        binding?.recyclerView47?.layoutManager = layoutManagerFood
        binding?.recyclerView47?.adapter = ticketFoodAdapter

    }

}