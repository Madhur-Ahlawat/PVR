package com.net.pvr1.ui.search.searchCinema

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivitySearchCinemaBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.cinemaSession.CinemaSessionActivity
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.search.searchCinema.viewModel.CinemaSearchViewModel
import com.net.pvr1.ui.search.searchHome.adapter.SearchHomeCinemaAdapter
import com.net.pvr1.ui.search.searchHome.response.HomeSearchResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchCinemaActivity : AppCompatActivity(),
    SearchHomeCinemaAdapter.RecycleViewItemClickListenerCity {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivitySearchCinemaBinding? = null
    private val authViewModel: CinemaSearchViewModel by viewModels()
    private var loader: LoaderDialog? = null


    private var filterCinemaList: ArrayList<HomeSearchResponse.Output.T>? = null
    private var searchHomeCinemaAdapter: SearchHomeCinemaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCinemaBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        manageFunction()
    }

    private fun manageFunction() {
        authViewModel.cinemaSearch(preferences.getCityName(), "", "", preferences.getLatitudeData(), preferences.getLongitudeData())
        search()
        movedNext()
    }

    private fun movedNext() {
//        Voice Search
        binding?.include42?.voiceBtn?.setOnClickListener {
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
                resultLauncher.launch(intent)
            } catch (e: Exception) {
                toast(e.message)
            }
        }

//cancel
        binding?.include42?.cancelBtn?.setOnClickListener {
            finish()
        }

//Text Search
        binding?.include42?.editTextTextPersonName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

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
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(this.supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: HomeSearchResponse.Output) {
//      Set data Filter
        filterCinemaList= output.t

//      Set Data
        val gridLayout2 = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        searchHomeCinemaAdapter = SearchHomeCinemaAdapter(
            output.t, this, this)
        binding?.recyclerCinemaSearch?.layoutManager = gridLayout2
        binding?.recyclerCinemaSearch?.adapter = searchHomeCinemaAdapter

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    val result: ArrayList<String>? = data?.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    filter( Objects.requireNonNull(result)!![0])
                    binding?.include42?.editTextTextPersonName?.setText(
                        Objects.requireNonNull(result)!![0]
                    )
                }
            }
        }

    override fun onSearchCinema(selectCityItemList: HomeSearchResponse.Output.T) {
        val intent = Intent(this, CinemaSessionActivity::class.java)
        intent.putExtra("cid", selectCityItemList.id)
        intent.putExtra("lat", selectCityItemList.lat)
        intent.putExtra("lang", selectCityItemList.lng)
        startActivity(intent)

    }

    override fun onSearchCinemaDirection(selectCityItemList: HomeSearchResponse.Output.T) {
        Constant().shareData(this,selectCityItemList.lat,selectCityItemList.lng)
    }

    private fun filter(text: String) {
        val filtered: ArrayList<HomeSearchResponse.Output.T> = ArrayList()
        val filtered1: ArrayList<HomeSearchResponse.Output.T> = ArrayList()
        for (item in filterCinemaList!!) {
            if (item.n.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                filtered.add(item)
            }
        }
        if (filtered.isEmpty()) {
            searchHomeCinemaAdapter?.filterCinemaList(filtered1)
        } else {

            searchHomeCinemaAdapter?.filterCinemaList(filtered)
        }
    }


}