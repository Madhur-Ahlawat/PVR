package com.net.pvr1.ui.search.searchComingSoon

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySearchComingSoonBinding
import com.net.pvr1.di.preference.AppPreferences
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.search.searchComingSoon.adapter.SearchComingSoonAdapter
import com.net.pvr1.ui.search.searchComingSoon.viewModel.ComingSoonSearchViewModel
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchComingSoonActivity : AppCompatActivity(),
    SearchComingSoonAdapter.RecycleViewItemClickListenerCity {
    private val REQUEST_CODE_SPEECH_INPUT = 1

    private lateinit var preferences: AppPreferences
    private var binding: ActivitySearchComingSoonBinding? = null
    private val authViewModel: ComingSoonSearchViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchComingSoonBinding.inflate(layoutInflater, null, false)
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
                        this@SearchComingSoonActivity, " " + e.message,
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
        val otherCityAdapter = SearchComingSoonAdapter(
            output.m,
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


    override fun onSearchMovie(selectCityList: List<HomeSearchResponse.Output.M>) {

    }


}