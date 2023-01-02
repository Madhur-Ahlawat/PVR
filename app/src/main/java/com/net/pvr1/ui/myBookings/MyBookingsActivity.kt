package com.net.pvr1.ui.myBookings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityMyBookingBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.myBookings.adapter.FoodTicketAdapter
import com.net.pvr1.ui.myBookings.adapter.GiftCardAdapter
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.myBookings.viewModel.MyBookingViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyBookingsActivity : AppCompatActivity(), GiftCardAdapter.Direction {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityMyBookingBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: MyBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        movedNext()
    }

    //Item Action
    private fun movedNext() {
        binding?.include2?.imageView58?.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
        }

        binding?.include2?.textView108?.text=getString(R.string.booking_history)

//GiftCard Action
        binding?.giftCard?.setOnClickListener {
            //GiftCard Api Call
            authViewModel.giftCard(preferences.getUserId().toString(), Constant().getDeviceId(this))
            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)
        }

//Ticket & Food Action
        binding?.ticketFood?.setOnClickListener {
            //Ticket & Food Api Call
            authViewModel.foodTicket(
                preferences.getUserId(),
                Constant().getDeviceId(this),
                "",
                preferences.getCityName(),
                "",
                "NO"
            )


            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)
        }
//FoodCall
        authViewModel.foodTicket(
            preferences.getUserId(),
            Constant().getDeviceId(this),
            "",
            preferences.getCityName(),
            "",
            "NO"
        )

        giftCard()
        foodTicket()
    }

    //Gift Card Api
    private fun giftCard() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveGiftCardData(it.data.output)
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

    //Food With Ticket Response
    private fun retrieveFoodTicketData(output: FoodTicketResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val foodTicketAdapter = FoodTicketAdapter(output.c, this)
        binding?.recyclerMyBooking?.layoutManager = gridLayout2
        binding?.recyclerMyBooking?.adapter = foodTicketAdapter
    }

    //GiftCard
    private fun retrieveGiftCardData(output: GiftCardResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val giftCardAdapter = GiftCardAdapter(output.gc, this, this)
        binding?.recyclerMyBooking?.layoutManager = gridLayout2
        binding?.recyclerMyBooking?.adapter = giftCardAdapter
    }

    override fun resend(comingSoonItem: CinemaResponse.Output.C) {

    }


}