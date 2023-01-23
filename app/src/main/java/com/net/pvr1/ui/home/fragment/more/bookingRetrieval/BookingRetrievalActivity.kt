package com.net.pvr1.ui.home.fragment.more.bookingRetrieval

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityBookingRetrievalBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.bookingSession.response.BookingResponse
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.viewModel.BookingRetrievalViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BookingRetrievalActivity : AppCompatActivity(),BookingRetrievalAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityBookingRetrievalBinding? = null
    private val authViewModel: BookingRetrievalViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var filterCityList: ArrayList<BookingRetrievalResponse.Output.C>? = null
    private var bookingRetrieval: BookingRetrievalAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingRetrievalBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        authViewModel.bookingRetrieval(preferences.getCityName(), preferences.getLongitudeData(), preferences.getLongitudeData(), preferences.getUserId().toString(), "")
        binding?.textView36?.text=preferences.getCityName()
        bookingRetrievalApi()
        movedNext()
    }

    private fun movedNext() {
        //back click
        binding?.include13?.imageView58?.setOnClickListener {
            finish()
        }

        binding?.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //do here your stuff f
                authViewModel.bookingRetrieval(preferences.getCityName(), preferences.getLongitudeData(), preferences.getLongitudeData(),
                    preferences.getUserId(), binding?.editText?.text.toString())
                true
            } else false
        }

        binding?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding?.editText?.text?.chars()
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    private fun bookingRetrievalApi() {
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

    private fun retrieveData(output: BookingRetrievalResponse.Output) {
//        Set Filter Data
        filterCityList = output.c

//        Array List
        val gridLayout =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView32?.layoutManager = LinearLayoutManager(this)
        bookingRetrieval = BookingRetrievalAdapter(this, output.c, this,binding?.include14?.textView5)
        binding?.recyclerView32?.layoutManager = gridLayout
        binding?.recyclerView32?.adapter = bookingRetrieval
    }

    override fun dateClick(comingSoonItem: BookingResponse.Output.Dy) {

    }

    private fun filter(text: String) {
        val filtered: ArrayList<BookingRetrievalResponse.Output.C> = ArrayList()
        val filtered1: ArrayList<BookingRetrievalResponse.Output.C> = ArrayList()
        for (item in filterCityList!!) {
            if (item.n.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }

        if (filtered.isEmpty()) {
            bookingRetrieval?.filterList(filtered1)
        } else {
            bookingRetrieval?.filterList(filtered)
        }
    }

}