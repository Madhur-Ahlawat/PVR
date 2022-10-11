package com.net.pvr1.ui.bookingSession

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityBookingBinding
import com.net.pvr1.ui.bookingSession.adapter.BookingShowsDaysAdapter
import com.net.pvr1.ui.bookingSession.adapter.BookingShowsLanguageAdapter
import com.net.pvr1.ui.bookingSession.adapter.BookingShowsParentAdapter
import com.net.pvr1.ui.bookingSession.adapter.BookingTheatreAdapter
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.bookingSession.response.BookingTheatreResponse
import com.net.pvr1.ui.bookingSession.viewModel.BookingViewModel
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingActivity : AppCompatActivity(),
    BookingShowsDaysAdapter.RecycleViewItemClickListenerCity,
    BookingShowsLanguageAdapter.RecycleViewItemClickListenerCity,
    BookingTheatreAdapter.RecycleViewItemClickListener {

//    @Inject
//    lateinit var preferences: AppPreferences
    private var binding: ActivityBookingBinding? = null
    private val authViewModel: BookingViewModel by viewModels()
    private var loader: LoaderDialog? = null

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
            "192",
            "0",
            intent.getStringExtra("mid").toString(),
            "no"
        )

        bookingTicketDataLoad()
        bookingTheatreDataLoad()
    }

    private fun bookingTicketDataLoad() {
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
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val bookingTheatreAdapter = BookingTheatreAdapter(output.m, this, this)
        binding?.recyclerView12?.layoutManager = gridLayout2
        binding?.recyclerView12?.adapter = bookingTheatreAdapter

    }

    private fun retrieveData(output: BookingResponse.Output) {
        //MovieName
        binding?.textView103?.text = output.nm
        //genre
        binding?.textView104?.text = output.gnr
        //recycler Days
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val bookingShowsDaysAdapter = BookingShowsDaysAdapter(output.dys, this, this)
        binding?.recyclerView9?.layoutManager = gridLayout2
        binding?.recyclerView9?.adapter = bookingShowsDaysAdapter

        //recycler Language
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val bookingShowsLanguageAdapter = BookingShowsLanguageAdapter(output.lngs, this, this)
        binding?.recyclerView10?.layoutManager = gridLayout
        binding?.recyclerView10?.adapter = bookingShowsLanguageAdapter

        //Shows
        val gridLayout3 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val bookingShowsParentAdapter = BookingShowsParentAdapter(output.cinemas, this)
        binding?.recyclerView8?.layoutManager = gridLayout3
        binding?.recyclerView8?.adapter = bookingShowsParentAdapter
    }

    override fun dateClick(comingSoonItem: BookingResponse.Output.Dy) {

    }

    override fun languageClick(comingSoonItem: String) {

    }

    override fun theatreClick(comingSoonItem: BookingTheatreResponse.Output.M) {

    }

}