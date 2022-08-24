package com.net.pvr1.ui.search.searchCinema

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySearchCinemaBinding
import com.net.pvr1.databinding.ActivitySearchHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.search.searchCinema.viewModel.CinemaSearchViewModel
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeCinemaAdapter
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchCinemaActivity : AppCompatActivity(),
    SearchHomeCinemaAdapter.RecycleViewItemClickListenerCity {
    private val REQUEST_CODE_SPEECH_INPUT = 1

    private lateinit var preferences: AppPreferences
    private var binding: ActivitySearchCinemaBinding? = null
    private val authViewModel: CinemaSearchViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCinemaBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()
        authViewModel.cinemaSearch("Delhi-NCR", "", "", "77.04", "28.56")
        search()
        movedNext()
    }

    private fun movedNext() {
        binding?.voiceBtn?.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast
                    .makeText(
                        this@SearchCinemaActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }


        binding?.cancelBtn?.setOnClickListener {
            finish()
        }

        binding?.editTextTextPersonName?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                authViewModel.cinemaSearch("Delhi-NCR", s.toString(), "", "77.04", "28.56")
            }
        })
    }

    private fun search() {
        authViewModel.homeSearchLiveData.observe(this) {
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
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: HomeSearchResponse.Output) {
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        val otherCityAdapter = SearchHomeCinemaAdapter(
            output.t as ArrayList<HomeSearchResponse.Output.T>,
            this,
            this
        )
        binding?.recyclerCinemaSearch?.layoutManager = gridLayout2
        binding?.recyclerCinemaSearch?.adapter = otherCityAdapter

    }

    @Deprecated("Deprecated in Java")
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {

                if (resultCode == RESULT_OK) {
                    val result: ArrayList<String>? = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    binding?.editTextTextPersonName?.setText(
                        Objects.requireNonNull(result)!![0]
                    )
                }
            }
        }
    }


    override fun onSearchCinema(selectCityItemList: HomeSearchResponse.Output.T) {
        val lat = selectCityItemList.lat
        val lang = selectCityItemList.lng
        val strUri =
            "http://maps.google.com/maps?q=loc:$lat,$lang"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName(
            "com.google.android.apps.maps",
            "com.google.android.maps.MapsActivity"
        )
        startActivity(intent)
    }


}