package com.net.pvr1.ui.bookingSession

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityBookingBinding
import com.net.pvr1.ui.bookingSession.adapter.*
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.bookingSession.viewModel.BookingViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BookingActivity : AppCompatActivity(),
    BookingShowsDaysAdapter.RecycleViewItemClickListenerCity,
    BookingShowsLanguageAdapter.RecycleViewItemClickListenerCity,
    BookingTheatreAdapter.RecycleViewItemClickListener,
    BookingPlaceHolderAdapter.RecycleViewItemClickListenerCity,
    BookingShowsParentAdapter.RecycleViewItemClickListener {

    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityBookingBinding? = null
    private val authViewModel: BookingViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var daySessionResponse: BookingResponse.Output? = null
    private var daysClick: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        authViewModel.bookingTicket(
            "Delhi-NCR",
            intent.getStringExtra("mid").toString(),
            "0",
            "0",
            "NA",
            "no",
            "no",
            ""
        )

        authViewModel.bookingTheatre(
            "Delhi-NCR",
            intent.getStringExtra("cid").toString(),
            preferences.getUserId().toString(),
            intent.getStringExtra("mid").toString(),
            "no"
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

    private fun bookingTheatreDataLoad() {
        authViewModel.userResponseTheatreLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveTheatreData(it.data.output)
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


    private fun retrieveTheatreData(output: BookingTheatreResponse.Output) {
        printLog("output---->${output.m}")
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
        printLog("ShowsData--->${output.cinemas}")
        if (!daysClick) {
            Constant.OfferDialogImage = output.mih
            //MovieName
            binding?.textView103?.text = daySessionResponse?.nm

            //language
            val language: String = java.lang.String.join(",", output.lngs)

            //genre
            binding?.textView104?.text =
                daySessionResponse?.gnr+" " + getString(R.string.dots)+" " + language
            //recycler Days
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
            val gridLayout4 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val bookingPlaceHolderAdapter = BookingPlaceHolderAdapter(output.ph, this, this)
            binding?.recyclerView23?.layoutManager = gridLayout4
            binding?.recyclerView23?.adapter = bookingPlaceHolderAdapter

        }
    }

    override fun showsDaysClick(comingSoonItem: BookingResponse.Output.Dy) {
        printLog("DaysClick--->${comingSoonItem}")
        daysClick = true
        authViewModel.bookingTicket(
            "Delhi-NCR",
            intent.getStringExtra("mid").toString(),
            "0",
            "0",
            comingSoonItem.dt,
            "no",
            "no",
            ""
        )
    }

    override fun languageClick(comingSoonItem: String) {

    }

    override fun theatreClick(comingSoonItem: BookingTheatreResponse.Output.M) {

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
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
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
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
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

}