package com.net.pvr1.ui.myBookings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityMyBookingBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.myBookings.adapter.FoodTicketChildAdapter
import com.net.pvr1.ui.myBookings.adapter.GiftCardAdapter
import com.net.pvr1.ui.myBookings.response.FoodTicketResponse
import com.net.pvr1.ui.myBookings.response.GiftCardResponse
import com.net.pvr1.ui.myBookings.viewModel.MyBookingViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyBookingsActivity : AppCompatActivity(), GiftCardAdapter.RecycleViewItemClickListener {

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
            "NO"
        )

        resendMail()
        giftCard()
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
        binding?.include2?.textView108?.text = getString(R.string.booking_history)

        //GiftCard Action
        binding?.textView52?.setOnClickListener {
            //quantity
            binding?.textView3?.hide()

            //GiftCard Api Call
            authViewModel.giftCard(preferences.getUserId(), Constant().getDeviceId(this))

            //gift Card
            binding?.textView52?.textSize = 14F
            binding?.textView52?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
            binding?.textView52?.setTextColor(getColor(R.color.black))

            //  food
            binding?.textView4?.textSize = 12F
            binding?.textView4?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView4?.setTextColor(getColor(R.color.black_40))


            //view ticket food
            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)
            //giftCard
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)
        }

        //Ticket & Food Action
        binding?.textView4?.setOnClickListener {
        //quantity
            binding?.textView3?.show()

            //Ticket & Food Api Call
            authViewModel.foodTicket(
                preferences.getUserId(),
                Constant().getDeviceId(this),
                "",
                preferences.getCityName(), "", "NO"
            )


            //gift Card
            binding?.textView52?.textSize = 12F
            binding?.textView52?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_medium)
            binding?.textView52?.setTextColor(getColor(R.color.black_40))

            //  food
            binding?.textView4?.textSize = 14F
            binding?.textView4?.typeface = ResourcesCompat.getFont(this, R.font.sf_pro_text_bold)
            binding?.textView4?.setTextColor(getColor(R.color.black))


            //view ticket food
            binding?.view17?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.yellow)
            //giftCard
            binding?.view18?.backgroundTintList =
                AppCompatResources.getColorStateList(this, R.color.gray)
        }


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
                        retrieveFoodTicketData(it.data.output.c)
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

    private fun resendMail() {
        authViewModel.resendMailLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {

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
    @SuppressLint("SetTextI18n")
    private fun retrieveFoodTicketData(output: List<FoodTicketResponse.Output.C>) {
        //title
        binding?.textView3?.text = getString(R.string.upcomingBooking) + output.size

        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val foodTicketAdapter = FoodTicketChildAdapter(output, this)
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

    override fun resend(comingSoonItem: GiftCardResponse.Output.Gc) {
        authViewModel.resendMail(
            preferences.getUserId(),
            comingSoonItem.id,
            "GIFTCARD"
        )
    }
}