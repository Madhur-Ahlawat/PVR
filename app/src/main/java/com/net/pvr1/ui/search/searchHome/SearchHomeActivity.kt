package com.net.pvr1.ui.search.searchHome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySearchHomeBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeCinemaAdapter
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeMovieAdapter
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.ui.search.searchHome.viewModel.HomeSearchViewModel
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchHomeActivity : AppCompatActivity(),
    SearchHomeMovieAdapter.RecycleViewItemClickListenerCity,
    SearchHomeCinemaAdapter.RecycleViewItemClickListenerCity {
    private lateinit var preferences: AppPreferences
    private var binding: ActivitySearchHomeBinding? = null
    private val authViewModel: HomeSearchViewModel by viewModels()
    private val REQUEST_CODE_SPEECH_INPUT = 1
    private var loader: LoaderDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHomeBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        preferences = AppPreferences()

        authViewModel.homeSearch("Delhi-NCR", "", "", "77.04", "28.56")

        movedNext()
        search()
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
                        this@SearchHomeActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }


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
        val otherCityAdapter = SearchHomeMovieAdapter(output.m, this, this)
        binding?.movieRecycler?.layoutManager = gridLayout2
        binding?.movieRecycler?.adapter = otherCityAdapter

        binding?.moviesBtn?.setOnClickListener {
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = SearchHomeMovieAdapter(output.m, this, this)
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = otherCityAdapter
        }
        binding?.theatresBtn?.setOnClickListener {
            val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            val otherCityAdapter = SearchHomeCinemaAdapter(
                output.t as ArrayList<HomeSearchResponse.Output.T>,
                this,
                this
            )
            binding?.movieRecycler?.layoutManager = gridLayout2
            binding?.movieRecycler?.adapter = otherCityAdapter

        }


    }


    override fun onSearchMovie(selectCityList: List<HomeSearchResponse.Output.M>) {

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