package com.net.pvr.ui.home.fragment.more.bookingRetrieval

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.net.pvr.R
import com.net.pvr.databinding.ActivityBookingRetrievalBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.bookingSession.response.BookingResponse
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.adapter.BookingRetrievalAdapter
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.response.BookingRetrievalResponse
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.viewModel.BookingRetrievalViewModel
import com.net.pvr.utils.Constant
import com.net.pvr.utils.NetworkResult
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BookingRetrievalActivity : AppCompatActivity(),
    BookingRetrievalAdapter.RecycleViewItemClickListener {
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

        manageFunction()
    }

    private fun manageFunction() {
        authViewModel.bookingRetrieval(
            preferences.getCityName(),
            preferences.getLongitudeData(),
            preferences.getLongitudeData(),
            preferences.getUserId().toString(),
            ""
        )
        binding?.textView36?.text = preferences.getCityName()

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
                authViewModel.bookingRetrieval(
                    preferences.getCityName(),
                    preferences.getLongitudeData(),
                    preferences.getLongitudeData(),
                    preferences.getUserId(),
                    binding?.editText?.text.toString()
                )
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


        //proceed Btn
        binding?.include14?.textView5?.setOnClickListener {
// Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "BOOKING RETRIVAL")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(this, "booking_retrival_proceed_form", bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun bookingRetrievalApi() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output as LinkedTreeMap<*, *>)
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

    private fun retrieveData(output: LinkedTreeMap<*, *>) {

//        Set Filter Data
        val gson = Gson()
        val data = gson.fromJson(
            JSONObject(output).toString(),
            BookingRetrievalResponse.Output::class.java
        )
        filterCityList = data.c

//        Array List
        val gridLayout = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding?.recyclerView32?.layoutManager = LinearLayoutManager(this)
        bookingRetrieval =
            BookingRetrievalAdapter(this, data.c, this, binding?.include14?.textView5)
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