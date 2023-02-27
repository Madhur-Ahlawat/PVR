package com.net.pvr.ui.myBookings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr.R
import com.net.pvr.databinding.ActivityPastBookingBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.myBookings.adapter.FoodTicketChildAdapter
import com.net.pvr.ui.myBookings.response.FoodTicketResponse
import com.net.pvr.ui.myBookings.viewModel.MyBookingViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PastBookingsActivity : AppCompatActivity(),
    FoodTicketChildAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityPastBookingBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: MyBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        //FoodCall
        authViewModel.foodTicket(
            preferences.getUserId(),
            Constant().getDeviceId(this),
            "",
            preferences.getCityName(),
            "",
            "YES"
        )

        foodTicket()
        movedNext()

    }

    //Item Action
    private fun movedNext() {

        //Back Click
        binding?.include2?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //title
        binding?.include2?.textView108?.text = getString(R.string.booking_past)

    }

    //Food With Ticket Api
    private fun foodTicket() {
        authViewModel.ticketFoodLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveFoodTicketData(it.data.output)
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


    //Food With Ticket Response
    private fun retrieveFoodTicketData(output: FoodTicketResponse.Output) {
        //title
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val foodTicketAdapter = FoodTicketChildAdapter(output.p, this, true, this)
        binding?.recyclerMyBooking?.layoutManager = gridLayout2
        binding?.recyclerMyBooking?.adapter = foodTicketAdapter
    }

    override fun addFood(data: FoodTicketResponse.Output.C) {

    }

    override fun onParkingClick(data: FoodTicketResponse.Output.C) {
    }

    override fun onDirectionClick(data: FoodTicketResponse.Output.C) {
    }


}